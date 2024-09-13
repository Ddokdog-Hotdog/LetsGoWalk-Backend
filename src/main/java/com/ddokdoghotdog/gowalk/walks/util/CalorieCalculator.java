package com.ddokdoghotdog.gowalk.walks.util;

import java.util.List;

import com.ddokdoghotdog.gowalk.walks.model.WalkPaths.PathPoint;

public class CalorieCalculator {
    // https://www.sciencedirect.com/science/article/pii/S221133552400130X
    // https://sites.google.com/site/compendiumofphysicalactivities/Activity-Categories/walking
    // when using average walking speed (4.29 ± 0.8 km/hr) and metabolic equivalents
    // (3.53 ± 0.6METs), the walk was classified as moderate intensity.
    private static final double MET = 3.53;

    public static double calculateCalories(double weightInKg, List<PathPoint> locations) {
        double totalDistance = DistanceCalculator.calculateDistance(locations);

        // 칼로리 계산: MET * 몸무게 * 운동 시간 kcal
        double caloriesBurned = MET * weightInKg * totalDistance / 1000;
        return caloriesBurned;
    }

    public static double calculateCalories(double weightInKg, Long distance) {
        double caloriesBurned = MET * weightInKg * distance / 1000;
        return caloriesBurned;
    }
}
