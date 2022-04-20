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

    //경로 사진

    //유저

    @Column(name="startlocation")
    private String startlocation; //시작 위치

    //게시판
}
