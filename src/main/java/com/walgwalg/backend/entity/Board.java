package com.walgwalg.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "board")
@Entity
@Getter
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title; //제목

    @OneToMany(mappedBy = "board")
    private List<HashTag> hashTags = new ArrayList<>(); //해시태그

    @Column(name = "contents")
    private String contents; //내용

    //산책

    @OneToMany(mappedBy = "board")
    private List<Like> likes = new ArrayList<>(); //좋아요

    @OneToMany(mappedBy = "board")
    private List<Scrap> scraps = new ArrayList<>(); //스크랩


}
