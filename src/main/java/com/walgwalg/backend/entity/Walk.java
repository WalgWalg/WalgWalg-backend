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
    private Long stepCount; //걸음수

    @Column(name="distance")
    private Long distance; //거리

    @Column(name="calorie")
    private Long calorie; //칼로리

    @Column(name="walk_time")
    private String walkTime; //소요시간

    @Column(name = "course")
    private String course; //산책 코스 사진 경로

    @Column(name="location")
    private String location; //위치 ex) 공원 이름 등

    @Column(name = "address")
    private String address;

    @ManyToOne(targetEntity = Users.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users; // 유저

    @OneToOne(mappedBy = "walk", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Board board; //게시판

    @OneToMany(mappedBy = "walk", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Gps> gpsList = new ArrayList<>(); // gps

    @Builder
    public Walk(Users users, Date walkDate, Long stepCount, Long distance, Long calorie, String walkTime,
                String course, String location, String address){
        this.users = users;
        this.walkDate = walkDate;
        this.stepCount = stepCount;
        this.distance = distance;
        this.calorie = calorie;
        this.walkTime = walkTime;
        this.course = course;
        this.location = location;
        this.address = address;
    }
    public void updateWalk(Long stepCount, Long distance, Long calorie, String walkTime,
                           String course){
        this.stepCount = stepCount;
        this.distance = distance;
        this.calorie = calorie;
        this.walkTime = walkTime;
        this.course = course;

    }
    public void addGps(Gps gps){
        this.gpsList.add(gps);
    }
    public void addBoard(Board board){
        this.board = board;
    }
}
