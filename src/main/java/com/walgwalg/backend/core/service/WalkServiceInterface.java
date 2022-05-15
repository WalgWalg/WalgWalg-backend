package com.walgwalg.backend.core.service;

import java.util.Date;

public interface WalkServiceInterface {
    void startWalk(String userid, Date walkDate, String location);
    void addGps(String userid, Date walkDate, Double latitude, Double longitude);
}
