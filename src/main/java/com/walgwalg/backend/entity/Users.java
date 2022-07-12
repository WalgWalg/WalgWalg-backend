package com.walgwalg.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name="users")
@Entity
@Getter
@NoArgsConstructor
public class Users {
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

    @Column(name="refresh_token")
    private String refreshToken;

    @Column(name = "profile")
    private String profile; //프로필 사진 경로
    
    @OneToMany(mappedBy = "users")
    private List<Board> boardList = new ArrayList<>(); //게시판 리스트
    
    @OneToMany(mappedBy = "users")
    private List<Walk> walkList = new ArrayList<>(); // 산책 리스트
    
    @OneToMany(mappedBy = "users")
    private List<Likes> likeList = new ArrayList<>(); //좋아요 리스트

    @OneToMany(mappedBy = "users")
    private List<Scrap> scrapList = new ArrayList<>(); //스크랩 리스트

    @Builder
    public Users(String userid, String password, String nickname, String address, String salt){
        this.userid = userid;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        this.salt = salt;
    }
    public void addBoard(Board board){
        this.boardList.add(board);
    }
    public void addWalk(Walk walk){
        this.walkList.add(walk);
    }
    public void addLikes(Likes likes){
        this.likeList.add(likes);
    }
    public void addScrap(Scrap scrap){
        this.scrapList.add(scrap);
    }
    public void changeRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    };
    public void changeUserInfo(String password, String nickname, String address, String salt, String profile){
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        this.salt = salt;
        this.profile = profile;
    }
}
