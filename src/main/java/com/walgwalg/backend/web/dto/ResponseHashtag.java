package com.walgwalg.backend.web.dto;

import com.walgwalg.backend.entity.HashTag;
import lombok.Builder;
import lombok.Data;

public class ResponseHashtag {
    @Data
    @Builder
    public static class Hashtag{
        private String tag;
        public static Hashtag of(HashTag hashTag){
            return Hashtag.builder()
                    .tag(hashTag.getTag())
                    .build();
        }
    }
}
