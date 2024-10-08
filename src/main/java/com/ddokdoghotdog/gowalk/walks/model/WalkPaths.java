package com.ddokdoghotdog.gowalk.walks.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "walk_paths")
@CompoundIndex(name = "location_2dsphere", def = "{'paths.location': '2dsphere'}")
public class WalkPaths {
    @Id
    private String id;
    private Long walkId;
    private List<PathPoint> paths;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PathPoint {
        private Date recordTime;
        private GeoJsonPoint location;

        // GeoJsonPoint longitude latitude 반대로!
        public static PathPoint from(double latitude, double longitude) {
            GeoJsonPoint geoJsonPoint = new GeoJsonPoint(longitude, latitude);
            return new PathPoint(new Date(), geoJsonPoint);
        }
    }
}
