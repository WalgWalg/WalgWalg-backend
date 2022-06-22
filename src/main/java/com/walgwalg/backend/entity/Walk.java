package com.walgwalg.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Table(name="walk")
@Entity
@Getter
@NoArgsConstructor
public class Walk {
    @Id
    @Column(name = "walk_id")
    private String id = UUID.randomUUID().toString();

    @Column(name = "walk_date")
    private Date walkDate; //산책날짜

    @Column(name ="step_count")
    private Integer step_count; //걸음수

    @Column(name="distance")
    private float distance; //거리

    @Column(name="calorie")
    private Integer calorie; //칼로리

    @Column(name="walk_time")
    @Temporal(TemporalType.TIME)
    private Date walkTime; //소요시간

    @Column(name = "course")
    private String course; //산책 코스 사진 경로

    @Column(name="location")
    private String location; //위치 ex) 공원 이름 등

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 유저

    @OneToOne(mappedBy = "walk", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Board board; //게시판

    @OneToMany(mappedBy = "walk", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Gps> gpsList = new ArrayList<>(); // gps

    @Builder
    public Walk(User user, Date walkDate, Integer step_count, float distance, Integer calorie, Date walkTime,
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
    public void updateWalk(Integer step_count, float distance, Integer calorie, Date walkTime,
                           String course){
        this.step_count = step_count;
        this.distance = distance;
        this.calorie = calorie;
        this.walkTime = walkTime;
        this.course = course;

    }
    public void addGps(Gps gps){
        this.gpsList.add(gps);
    }
}
