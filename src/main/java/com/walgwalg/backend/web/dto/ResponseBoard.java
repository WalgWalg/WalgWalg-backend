package com.walgwalg.backend.web.dto;

import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.HashTag;
import com.walgwalg.backend.entity.Walk;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResponseBoard {
    @Builder
    @Data
    public static class getBoard{
        private String boardId;
        private Date date;
        private String title;
        private String contents;
        private List<String> hashTags;
        //산책 정보
        private Long step_count;
        private Long distance;
        private Long calorie;
        private String course;
        private String location;
        //작성자
        private String nickname;
        //좋아요
        private Integer likes;

        public static getBoard of(Board board){
            Walk walk = board.getWalk();
            List<String> hashTagList = new ArrayList<>();
            if(!board.getHashTags().isEmpty()){//해시태그가 있으면
                for(HashTag tag : board.getHashTags()){
                    hashTagList.add(tag.getTag());
                }
            }
            return getBoard.builder()
                    .boardId(board.getBoardId())
                    .date(board.getBoardDate())
                    .title(board.getTitle())
                    .contents(board.getContents())
                    .hashTags(hashTagList)
                    .step_count(walk.getStepCount())
                    .distance(walk.getDistance())
                    .calorie(walk.getCalorie())
                    .course(walk.getCourse())
                    .location(walk.getLocation())
                    .nickname(board.getUsers().getNickname())
                    .likes(board.getLikesList().size())
                    .build();
        }
    }

    @Data
    @Builder
    public static class list{
        private String boardId;
        private String title;
        private String image;
        private Date date;
        private List<String> hashTags;
        //좋아요
        private Integer likes;

        public static list of(Board board){
            List<String> hashTagList = new ArrayList<>();
            if(!board.getHashTags().isEmpty()){//해시태그가 있으면
                for(HashTag tag : board.getHashTags()){
                    hashTagList.add(tag.getTag());
                }
            }
            return list.builder()
                    .boardId(board.getBoardId())
                    .title(board.getTitle())
                    .image(board.getWalk().getCourse())
                    .date(board.getBoardDate())
                    .hashTags(hashTagList)
                    .likes(board.getLikesList().size())
                    .build();
        }
    }

    @Builder
    @Data
    public static class top{
        private String parkName; //공원이름
        private String image;
    }
}
