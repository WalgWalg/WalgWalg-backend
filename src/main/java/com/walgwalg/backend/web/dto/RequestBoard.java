package com.walgwalg.backend.web.dto;

import com.walgwalg.backend.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

public class RequestBoard {
    @Builder
    @Data
    public static class like{
        private Date timestamp;
        private User user;
    }
}
