package com.ddokdoghotdog.gowalk.walks.util;

import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Component;

import com.ddokdoghotdog.gowalk.walks.model.WalkPaths;

import org.locationtech.jts.index.strtree.STRtree;
import org.locationtech.jts.index.ItemVisitor;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class IntersectionFinder {
    private static final double BUFFER_SIZE = 0.00100; // 약 100미터에 해당하는 경도/위도 값
    private static final int MINIMUM_POINT = 10;
    private static final int MAXIMUM_POINT = 100;
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public List<Coordinate> findHotspot(List<WalkPaths> walkDocuments) {
        STRtree index = new STRtree();
        Map<Coordinate, Integer> intersectionCounts = new HashMap<>();

        // 모든 점을 인덱스에 추가
        for (WalkPaths doc : walkDocuments) {
            for (WalkPaths.PathPoint point : doc.getPaths()) {
                Coordinate coord = new Coordinate(point.getLocation().getX(), point.getLocation().getY());
                Geometry pointGeom = geometryFactory.createPoint(coord).buffer(BUFFER_SIZE);
                index.insert(pointGeom.getEnvelopeInternal(), coord);
            }
        }

        // 인덱스 구축
        index.build();

        // 각 점에 대해 근처의 점들을 찾아 교차 여부 확인
        for (WalkPaths doc : walkDocuments) {
            for (WalkPaths.PathPoint point : doc.getPaths()) {
                Coordinate coord = new Coordinate(point.getLocation().getX(), point.getLocation().getY());
                Geometry pointGeom = geometryFactory.createPoint(coord).buffer(BUFFER_SIZE);

                index.query(pointGeom.getEnvelopeInternal(), new ItemVisitor() {
                    @Override
                    public void visitItem(Object item) {
                        Coordinate otherCoord = (Coordinate) item;
                        // if (!coord.equals2D(otherCoord)) { 좌표가 같은 경우 무시 -> 사용자가 보내는 좌표가 같을 수 있어 제외
                        Geometry otherPointGeom = geometryFactory.createPoint(otherCoord);
                        if (pointGeom.intersects(otherPointGeom)) {
                            intersectionCounts.put(coord, intersectionCounts.getOrDefault(coord, 0) + 1);
                        }
                        // }
                    }
                });
            }
        }

        // System.out.println("Intersection counts: " + intersectionCounts);

        List<Coordinate> filteredHotspots = filterHotspot(intersectionCounts);
        return filteredHotspots;
    }

    private List<Coordinate> filterHotspot(Map<Coordinate, Integer> hotspots) {
        // 카운트가 높은 순서로 정렬
        List<Map.Entry<Coordinate, Integer>> sortedHotspots = new ArrayList<>(hotspots.entrySet());
        sortedHotspots.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        List<Coordinate> filteredHotspots = new ArrayList<>();
        Set<Coordinate> excludedCoordinates = new HashSet<>();

        boolean isFirstHotspot = true;

        // 핫스팟 선정
        for (Map.Entry<Coordinate, Integer> entry : sortedHotspots) {
            Coordinate coord = entry.getKey();
            int count = entry.getValue();

            if (excludedCoordinates.contains(coord)) {
                continue;
            }

            if (isFirstHotspot) {
                if (count >= MINIMUM_POINT) {
                    filteredHotspots.add(coord);
                    isFirstHotspot = false;
                }
            } else {
                if (count >= MAXIMUM_POINT) {
                    filteredHotspots.add(coord);
                }
            }

            if (!filteredHotspots.isEmpty()) {
                // 현재 핫스팟 주변의 다른 후보들을 제외 목록에 추가
                for (Map.Entry<Coordinate, Integer> otherEntry : sortedHotspots) {
                    Coordinate otherCoord = otherEntry.getKey();
                    if (!coord.equals(otherCoord) && calculateDistance(coord, otherCoord) < BUFFER_SIZE * 2) {
                        excludedCoordinates.add(otherCoord);
                    }
                }
            }
        }

        return filteredHotspots;
    }

    private double calculateDistance(Coordinate c1, Coordinate c2) {
        return c1.distance(c2);
    }
}