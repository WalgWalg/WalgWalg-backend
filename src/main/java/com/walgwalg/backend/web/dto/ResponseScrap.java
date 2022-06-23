package com.walgwalg.backend.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.HashTag;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResponseScrap {
    @Data
    @Builder
    public static class MyScrap{
        private String boardId;
        private String title;
        private List<ResponseHashtag.Hashtag> hashTags;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/seoul")
        private Date writeDate;
        private Integer scrapCount;
        public static ResponseScrap.MyScrap of(Board board){
            List<ResponseHashtag.Hashtag> hashtagListDto = new ArrayList<>();
            List<HashTag> hashTags = board.getHashTags();
            if(hashTags != null){
                for(HashTag hashTag : board.getHashTags()){
                    ResponseHashtag.Hashtag hashtagDto = ResponseHashtag.Hashtag.of(hashTag);
                    hashtagListDto.add(hashtagDto);
                }
            }
            return MyScrap.builder()
                    .boardId(board.getId())
                    .title(board.getTitle())
                    .hashTags(hashtagListDto)
                    .writeDate(board.getTimestamp())
                    .scrapCount(board.getScrapList().size())
                    .build();
        }
    }
}
