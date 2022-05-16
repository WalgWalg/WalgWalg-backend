package com.walgwalg.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name="user")
@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="userid")
    private String userid;

    @Column(name="password")
    private String password;

    @Column(name="nickname")
    private String nickname;

    @Column(name="address")
    private String address;

    @Column(name="salt")
    private String salt;

    @Column(name="refreshToken")
    private String refreshToken;

//    @Column(name = "profile")
//    private String profile; //프로필 사진 경로
    
    @OneToMany(mappedBy = "user")
    private List<Board> boardList = new ArrayList<>(); //게시판 리스트
    
    @OneToMany(mappedBy = "user")
    private List<Walk> walkList = new ArrayList<>(); // 산책 리스트
    
    @OneToMany(mappedBy = "user")
    private List<Likes> likeList = new ArrayList<>(); //좋아요 리스트

    @OneToMany(mappedBy = "user")
    private List<Scrap> scrapList = new ArrayList<>(); //스크랩 리스트

    @Builder
    public User(String userid, String password, String nickname, String address, String salt){
        this.userid = userid;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        this.salt = salt;
    }
    public void addWalk(Walk walk){
        this.walkList.add(walk);

    }
    public void changeRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    };
    public void changeUserInfo(String password, String nickname, String address, String salt){
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        this.salt = salt;
    }
}
