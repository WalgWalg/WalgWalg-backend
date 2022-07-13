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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walk_id")
    private Walk walk;

    @Builder
    public Gps(String latitude, String longitude, Walk walk){
        this.latitude = latitude;
        this.longitude = longitude;
        this.walk = walk;
    }
}
