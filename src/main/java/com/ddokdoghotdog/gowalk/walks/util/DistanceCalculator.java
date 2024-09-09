package com.ddokdoghotdog.gowalk.walks.util;

import java.util.List;

import com.ddokdoghotdog.gowalk.walks.model.WalkPaths.PathPoint;

public class DistanceCalculator {
    private static final double EARTH_RADIUS = 6371000; // 지구의 반지름 (미터 단위)

    // 전체 거리를 계산
    public static Long calculateDistance(List<PathPoint> locations) {
        double totalDistance = 0.0;

        for (int i = 1; i < locations.size(); i++) {
            PathPoint prevLocation = locations.get(i - 1);
            PathPoint currLocation = locations.get(i);

            totalDistance += calculateDistanceBetween(prevLocation, currLocation);
        }

        return (long) totalDistance;
    }

    public static double calculateDistanceBetween(PathPoint loc1, PathPoint loc2) {
        double lat1 = Math.toRadians(loc1.getLocation().getY());
        double lon1 = Math.toRadians(loc1.getLocation().getX());
        double lat2 = Math.toRadians(loc2.getLocation().getY());
        double lon2 = Math.toRadians(loc2.getLocation().getX());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c; // 미터 단위 거리
    }

    public static double calculateAverageSpeed(List<PathPoint> locations) {
        if (locations.size() < 2) {
            return 0.0; // 데이터가 부족할 경우
        }

        double totalDistance = calculateDistance(locations);
        long startTime = locations.get(0).getRecordTime().getTime();
        long endTime = locations.get(locations.size() - 1).getRecordTime().getTime();
        double durationInSeconds = (endTime - startTime) / 1000.0; // 초 단위

        return durationInSeconds > 0 ? totalDistance / durationInSeconds : 0.0; // m/s 단위
    }
}
