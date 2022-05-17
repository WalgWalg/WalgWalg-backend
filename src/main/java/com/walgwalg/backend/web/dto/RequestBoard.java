package com.walgwalg.backend.web.dto;

import com.walgwalg.backend.entity.User;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class RequestBoard {
    @Builder
    @Data
    public static class like{
        @NotNull( message = "작성일자가 비어있음")
        private Date writeDate;
        @NotNull(message = "작성자가 비어있음")
        private String writerId;
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
