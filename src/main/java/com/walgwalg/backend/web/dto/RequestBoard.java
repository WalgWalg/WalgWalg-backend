package com.walgwalg.backend.web.dto;

import com.walgwalg.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestBoard {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class register{
        @NotNull( message = "산책번호가 비어있음")
        private Long walkId;
        @NotNull( message = "제목이 비어있음")
        private String title;
        @Builder.Default
        private List<String> hashTags = new ArrayList<>();
        @NotNull( message = "내용이 비어있음")
        private String contents;
    }

    @Builder
    @Data
    public static class scrap{
        @NotNull( message = "작성일자가 비어있음")
        private Date writeDate;
        @NotNull(message = "작성자가 비어있음")
        private String writerId;
    }
}
