package com.walgwalg.backend.web.dto;

import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.HashTag;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResponseBoard {
    @Builder
    @Data
    public static class getBoard{
        private String title;
        private String contents;
        private List<String> hashTags;
        //산책 정보
        private Integer step_count;
        private float distance;
        private Integer calorie;
        private String course;
        private String location;
        //작성자
        private String nickname;
        //좋아요
        private Integer likes;

    }

    @Data
    @Builder
    public static class list{
        private String boardId;
        private String title;
        private String image;
        private Date date;
        private List<ResponseHashtag.Hashtag> hashTags;

        public static list of(Board board){
            List<ResponseHashtag.Hashtag> hashtagListDto = new ArrayList<>();
            List<HashTag> hashTags = board.getHashTags();
            if(hashTags != null){
                for(HashTag hashTag : board.getHashTags()){
                    ResponseHashtag.Hashtag hashtagDto = ResponseHashtag.Hashtag.of(hashTag);
                    hashtagListDto.add(hashtagDto);
                }
            }
            return list.builder()
                    .boardId(board.getId())
                    .title(board.getTitle())
                    .image(board.getWalk().getCourse())
                    .date(board.getTimestamp())
                    .hashTags(hashtagListDto)
                    .build();
        }
    }


    @Data
    @Builder
    public static class MyScrap{
        private String title;
        private List<ResponseHashtag.Hashtag> hashTags;

        public static MyScrap of(Board board){
            List<ResponseHashtag.Hashtag> hashtagListDto = new ArrayList<>();
            List<HashTag> hashTags = board.getHashTags();
            if(hashTags != null){
                for(HashTag hashTag : board.getHashTags()){
                    ResponseHashtag.Hashtag hashtagDto = ResponseHashtag.Hashtag.of(hashTag);
                    hashtagListDto.add(hashtagDto);
                }
            }
            return MyScrap.builder()
                    .title(board.getTitle())
                    .hashTags(hashtagListDto)
                    .build();
        }
    }
}
