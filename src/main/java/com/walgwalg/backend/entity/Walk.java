package com.walgwalg.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name="walk")
@Entity
@Getter
@NoArgsConstructor
public class Walk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date")
    private LocalDate date; //산책날짜

    @Column(name ="step_count")
    private String step_count; //걸음수

    @Column(name="distance")
    private String distance; //거리

    @Column(name="calorie")
    private String calorie; //칼로리

    @Column(name="time")
    private String time; //소요시간

    @Column(name = "course")
    private String course; //산책 코스 사진 경로

    @Column(name="start_location")
    private String start_location; //시작 위치

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 유저

    @OneToOne(mappedBy = "walk")
    private Board board; //게시판
}
