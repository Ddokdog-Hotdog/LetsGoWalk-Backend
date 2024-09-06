package com.ddokdoghotdog.gowalk.member.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class PointDTO {
    private String description;
    private Long amount;
    private Date date;
    private String type;

    public PointDTO(String description, Long amount, Date date, String type) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }


}

