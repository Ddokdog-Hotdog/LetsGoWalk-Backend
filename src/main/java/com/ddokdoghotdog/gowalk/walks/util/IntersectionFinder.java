package com.ddokdoghotdog.gowalk.walks.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.index.strtree.STRtree;
import org.springframework.stereotype.Component;

import com.ddokdoghotdog.gowalk.walks.model.WalkPaths;

@Component
public class IntersectionFinder {
    private static final double BUFFER_SIZE = 0.00100; // 약 100미터에 해당하는 경도/위도 값
    private static final int MINIMUM_POINT = 10;
    private static final int MAXIMUM_POINT = 400;
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public List<Coordinate> findHotspot(List<WalkPaths> walkDocuments) {
        List<Coordinate> allCoordinates = walkDocuments.stream()
                .flatMap(doc -> doc.getPaths().stream())
                .map(point -> new Coordinate(point.getLocation().getX(), point.getLocation().getY()))
                .distinct()
                .collect(Collectors.toList());

        // 주변 모든 포인트 인덱스 생성
        STRtree index = buildIndex(allCoordinates);
        // 주변 포인트 찾기
        Map<Coordinate, Integer> intersectionCounts = countIntersections(index, allCoordinates);
        // 핫스팟 개수 필터링
        return filterHotspot(intersectionCounts);
    }

    private STRtree buildIndex(List<Coordinate> coordinates) {
        STRtree index = new STRtree();
        coordinates.stream()
                .forEach(coord -> index.insert(geometryFactory.createPoint(coord).getEnvelopeInternal(), coord));
        index.build();
        return index;
    }

    private Map<Coordinate, Integer> countIntersections(STRtree index, List<Coordinate> coordinates) {
        Map<Coordinate, Integer> intersectionCounts = new HashMap<>();
        coordinates.stream().forEach(coord -> {
            Envelope queryEnvelope = new Envelope(coord.x - BUFFER_SIZE, coord.x + BUFFER_SIZE,
                    coord.y - BUFFER_SIZE, coord.y + BUFFER_SIZE);
            List<Coordinate> nearbyPoints = index.query(queryEnvelope);
            long count = nearbyPoints.stream()
                    .filter(nearbyCoord -> coord.distance(nearbyCoord) <= BUFFER_SIZE)
                    .count();
            intersectionCounts.put(coord, (int) count);
        });
        return intersectionCounts;
    }

    private List<Coordinate> filterHotspot(Map<Coordinate, Integer> hotspots) {
        // 카운트가 높은 순서로 정렬
        List<Map.Entry<Coordinate, Integer>> sortedHotspots = hotspots.entrySet().stream()
                .sorted(Map.Entry.<Coordinate, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());

        List<Coordinate> filteredHotspots = new ArrayList<>();
        Set<Coordinate> excludedCoordinates = new HashSet<>();

        // 핫스팟 선정
        for (Map.Entry<Coordinate, Integer> entry : sortedHotspots) {
            Coordinate coord = entry.getKey();
            int count = entry.getValue();

            if (excludedCoordinates.contains(coord))
                continue;

            if (filteredHotspots.isEmpty() && count >= MINIMUM_POINT) {
                filteredHotspots.add(coord);
            } else if (count >= MAXIMUM_POINT) {
                filteredHotspots.add(coord);
            }

            if (!filteredHotspots.isEmpty()) {
                excludedCoordinates.addAll(
                        sortedHotspots.stream()
                                .map(Map.Entry::getKey)
                                .filter(otherCoord -> !coord.equals(otherCoord) &&
                                        calculateDistance(coord, otherCoord) < BUFFER_SIZE * 2)
                                .collect(Collectors.toSet()));
            }
        }

        return filteredHotspots;
    }

    private double calculateDistance(Coordinate c1, Coordinate c2) {
        return c1.distance(c2);
    }
}