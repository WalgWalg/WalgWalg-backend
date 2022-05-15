package com.walgwalg.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name="walk")
@Entity
@Getter
@NoArgsConstructor
public class Walk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "walk_date")
    private Date walkDate; //산책날짜

    @Column(name ="step_count")
    private String step_count; //걸음수

    @Column(name="distance")
    private String distance; //거리

    @Column(name="calorie")
    private String calorie; //칼로리

    @Column(name="walk_time")
    private String walkTime; //소요시간

    @Column(name = "course")
    private String course; //산책 코스 사진 경로

    @Column(name="location")
    private String location; //위치 ex) 공원 이름 등

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 유저

    @OneToOne(mappedBy = "walk")
    private Board board; //게시판

    @OneToMany(mappedBy = "walk", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Gps> gpsList = new ArrayList<>(); // gps

    @Builder
    public Walk(User user, Date walkDate, String step_count, String distance, String calorie, String walkTime,
                String course, String location){
        this.user = user;
        this.walkDate = walkDate;
        this.step_count = step_count;
        this.distance = distance;
        this.calorie = calorie;
        this.walkTime = walkTime;
        this.course = course;
        this.location = location;
    }

    public void addGps(Gps gps){
        this.gpsList.add(gps);
    }
}
