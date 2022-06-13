package com.walgwalg.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
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

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date timestamp = new Date();

    @OneToMany(mappedBy = "board")
    private List<HashTag> hashTags = new ArrayList<>(); //해시태그

    @Column(name = "contents")
    private String contents; //내용

    @OneToOne
    @JoinColumn(name = "walk_id")
    private Walk walk; // 산책 정보

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Likes> likesList = new ArrayList<>(); //좋아요

    @OneToMany(mappedBy = "board",cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Scrap> scrapList = new ArrayList<>(); //스크랩

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 유저

    @Builder
    public Board(String title,String contents,Walk walk, User user){
        this.title = title;
        this.contents = contents;
        this.walk = walk;
        this.user = user;
    }
    public void addLikes(Likes likes){
        this.likesList.add(likes);
    }
    public void addScrap(Scrap scrap){
        this.scrapList.add(scrap);
    }
    public void addHashTag(HashTag hashTag){this.hashTags.add(hashTag);}
}
