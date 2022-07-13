package com.walgwalg.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "hashtag")
@Entity
@Getter
@NoArgsConstructor
public class HashTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag")
    private String tag; //태그명

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board; //게시물

    @Builder
    public HashTag(String tag, Board board){
        this.tag = tag;
        this.board = board;
    }
}
