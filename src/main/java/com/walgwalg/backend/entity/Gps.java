package com.walgwalg.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "gps")
@Entity
@Getter
@NoArgsConstructor
public class Gps {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walk")
    private Walk walk;

    @Builder
    public Gps(double latitude, double longitude, Walk walk){
        this.latitude = latitude;
        this.longitude = longitude;
        this.walk = walk;
    }
}
