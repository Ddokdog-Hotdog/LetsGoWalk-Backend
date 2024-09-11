package com.ddokdoghotdog.gowalk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;

import com.ddokdoghotdog.gowalk.walks.model.WalkPaths;
import com.ddokdoghotdog.gowalk.walks.util.IntersectionFinder;

public class IntersectionFinderTest {

    private final IntersectionFinder intersectionFinder = new IntersectionFinder();

    @Test
    public void testFindHotspot() {
        // 교차 지점 (핫스팟)
        double hotspotLat = 37.5974621;
        double hotspotLng = 126.98977333;

        WalkPaths doc1 = WalkPaths.builder()
                .id("id1")
                .walkId(123L)
                .paths(Arrays.asList(
                        WalkPaths.PathPoint.from(hotspotLat - 0.0001, hotspotLng - 0.0001),
                        WalkPaths.PathPoint.from(hotspotLat, hotspotLng), // 교차 지점
                        WalkPaths.PathPoint.from(hotspotLat + 0.0001, hotspotLng + 0.0001),
                        WalkPaths.PathPoint.from(hotspotLat + 0.00005, hotspotLng + 0.00005), // 추가 점
                        WalkPaths.PathPoint.from(hotspotLat + 0.00008, hotspotLng + 0.00008), // 추가 점
                        WalkPaths.PathPoint.from(hotspotLat - 0.00002, hotspotLng + 0.00002), // 추가 점
                        WalkPaths.PathPoint.from(hotspotLat + 0.00003, hotspotLng - 0.00003), // 추가 점
                        WalkPaths.PathPoint.from(hotspotLat - 0.00006, hotspotLng + 0.00006), // 추가 점
                        WalkPaths.PathPoint.from(hotspotLat + 0.00007, hotspotLng - 0.00007))) // 추가 점
                .build();

        WalkPaths doc2 = WalkPaths.builder()
                .id("id2")
                .walkId(124L)
                .paths(Arrays.asList(
                        WalkPaths.PathPoint.from(hotspotLat - 0.0001, hotspotLng + 0.0001),
                        WalkPaths.PathPoint.from(hotspotLat, hotspotLng), // 교차 지점
                        WalkPaths.PathPoint.from(hotspotLat + 0.0001, hotspotLng - 0.0001),
                        WalkPaths.PathPoint.from(hotspotLat + 0.00005, hotspotLng + 0.00005), // 추가 점
                        WalkPaths.PathPoint.from(hotspotLat - 0.00003, hotspotLng + 0.00003), // 추가 점
                        WalkPaths.PathPoint.from(hotspotLat + 0.00004, hotspotLng - 0.00004), // 추가 점
                        WalkPaths.PathPoint.from(hotspotLat - 0.00005, hotspotLng + 0.00005), // 추가 점
                        WalkPaths.PathPoint.from(hotspotLat + 0.00002, hotspotLng - 0.00002))) // 추가 점
                .build();

        WalkPaths doc3 = WalkPaths.builder()
                .id("id3")
                .walkId(125L)
                .paths(Arrays.asList(
                        WalkPaths.PathPoint.from(hotspotLat, hotspotLng - 0.0001),
                        WalkPaths.PathPoint.from(hotspotLat, hotspotLng), // 교차 지점
                        WalkPaths.PathPoint.from(hotspotLat, hotspotLng + 0.0001),
                        WalkPaths.PathPoint.from(hotspotLat + 0.00006, hotspotLng + 0.00006), // 추가 점
                        WalkPaths.PathPoint.from(hotspotLat - 0.00004, hotspotLng - 0.00004), // 추가 점
                        WalkPaths.PathPoint.from(hotspotLat + 0.00005, hotspotLng + 0.00005), // 추가 점
                        WalkPaths.PathPoint.from(hotspotLat - 0.00002, hotspotLng + 0.00002))) // 추가 점
                .build();

        List<WalkPaths> walkDocuments = new ArrayList<>();
        walkDocuments.add(doc1);
        walkDocuments.add(doc2);
        walkDocuments.add(doc3);

        List<Coordinate> hotspots = intersectionFinder.findHotspot(walkDocuments);
        System.out.println(hotspots);

        assertNotNull(hotspots, "핫스팟이 null이어서는 안됩니다.");
        assertTrue(hotspots.size() > 0, "최소한 하나 이상의 핫스팟이 존재해야 합니다.");
        Coordinate hotspot = hotspots.get(0);

        assertEquals(hotspotLat, hotspot.getY(), 0.0001, "핫스팟 위도는 예상 값과 일치해야 합니다.");
        assertEquals(hotspotLng, hotspot.getX(), 0.0001, "핫스팟 경도는 예상 값과 일치해야 합니다.");
    }
}
