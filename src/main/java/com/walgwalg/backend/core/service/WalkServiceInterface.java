package com.walgwalg.backend.core.service;

import java.util.Date;

public interface WalkServiceInterface {
    void startWalk(String userid, Date walkDate, String location);
}
