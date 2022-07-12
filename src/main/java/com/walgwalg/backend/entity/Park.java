package com.walgwalg.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "Park",indexes = {
        @Index(name = "idx_park_number_address", columnList = "number_address")
}
)
@Getter
@Entity
@NoArgsConstructor
public class Park {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "park_name")
    private String parkName;

    @Column(name = "road_address")
    private String roadAddress;

    @Column(name = "number_address")
    private String numberAddress;

    @Column(name = "latitude")
    private String latitude; //위도

    @Column(name = "longitude")
    private String longitude; //경도

    @Builder
    public Park(String parkName, String roadAddress, String numberAddress, String latitude, String longitude){
        this.parkName = parkName;
        this.roadAddress = roadAddress;
        this.numberAddress = numberAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
