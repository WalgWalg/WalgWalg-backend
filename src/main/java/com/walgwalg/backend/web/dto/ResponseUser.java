package com.walgwalg.backend.web.dto;

import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.HashTag;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class ResponseUser {
    @Data
    @Builder
    public static class login{
        private String accessToken;
        private String refreshToken;
    }
    @Data
    @Builder
    public static class MyLike{
        private String title;
        private List<ResponseHashtag.Hashtag> hashTags;

        public static MyLike of(Board board){
            List<ResponseHashtag.Hashtag> hashtagListDto = new ArrayList<>();
            List<HashTag> hashTags = board.getHashTags();
            if(hashTags != null){
                for(HashTag hashTag : board.getHashTags()){
                    ResponseHashtag.Hashtag hashtagDto = ResponseHashtag.Hashtag.of(hashTag);
                    hashtagListDto.add(hashtagDto);
                }
            }
            return MyLike.builder()
                    .title(board.getTitle())
                    .hashTags(hashtagListDto)
                    .build();
        }
    }
}
