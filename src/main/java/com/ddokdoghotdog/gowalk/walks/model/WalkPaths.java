package com.ddokdoghotdog.gowalk.walks.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "walk_paths")
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
    }
}
