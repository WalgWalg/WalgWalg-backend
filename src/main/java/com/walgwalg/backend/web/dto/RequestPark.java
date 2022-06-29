package com.walgwalg.backend.web.dto;

import lombok.*;

import javax.persistence.Column;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestPark {
    private List<ParkInfo> park;
    }
