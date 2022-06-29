package com.walgwalg.backend.provider.service;

import com.amazonaws.Response;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.walgwalg.backend.core.service.WalkServiceInterface;
import com.walgwalg.backend.entity.Gps;
import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.entity.Walk;
import com.walgwalg.backend.exception.errors.DuplicatedWalkException;
import com.walgwalg.backend.exception.errors.NotFoundUserException;
import com.walgwalg.backend.exception.errors.NotFoundWalkException;
import com.walgwalg.backend.repository.GpsRepository;
import com.walgwalg.backend.repository.UserRepository;
import com.walgwalg.backend.repository.WalkRepository;
import com.walgwalg.backend.web.dto.RequestWalk;
import com.walgwalg.backend.web.dto.ResponseGps;
import com.walgwalg.backend.web.dto.ResponseWalk;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:/secrets/application-s3.properties")
public class WalkService implements WalkServiceInterface {
    private final AmazonS3Client amazonS3Client;
    private final UserRepository userRepository;
    private final WalkRepository walkRepository;
    private final GpsRepository gpsRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    @Transactional
    public Map<String, String> startWalk(String userid, Date walkDate,String location, String address){
        User user = userRepository.findByUserid(userid);
        if(user == null){
            throw new NotFoundUserException();
        }
//        Walk walk = walkRepository.findByUserAndWalkDate(user, walkDate);
//        if(walk != null){
//            throw new DuplicatedWalkException();
//        }
         Walk walk = Walk.builder()
                .user(user)
                .walkDate(walkDate)
                .location(location)
                .address(address)
                .build();
        walk = walkRepository.save(walk);
        user.addWalk(walk);
        Map<String, String> map = new HashMap<>();
        map.put("walkId", walk.getId());
        return map;
    }
    @Override
    @Transactional
    public void addGps(String userid, String walkId, String latitude, String longitude){
        User user = userRepository.findByUserid(userid);
        if(user == null){
            throw new NotFoundUserException();
        }
        Walk walk = walkRepository.findByUserAndId(user, walkId);
        if(walk == null){
            throw new NotFoundWalkException();
        }
        Gps gps = Gps.builder()
                .latitude(latitude)
                .longitude(longitude)
                .walk(walk)
                .build();
        gpsRepository.save(gps);
        walk.addGps(gps);
    }
    @Override
    @Transactional
    public List<ResponseGps.gps> getGps(String walkId){
        Walk walk = walkRepository.findById(walkId).orElseThrow(()-> new NotFoundWalkException());
        List<Gps> gpsList = gpsRepository.findByWalk(walk);

        List<ResponseGps.gps> list = new ArrayList<>();
        for(Gps gps: gpsList){
            ResponseGps.gps gpsDto = ResponseGps.gps.builder()
                    .latitude(gps.getLatitude())
                    .longitude(gps.getLongitude())
                    .build();
            list.add(gpsDto);
        }
        return list;
    }
    @Override
    @Transactional
    public void registerWalk(String userid,MultipartFile course,String walkId, Integer stepCount,
                             Integer distance, Integer calorie, String walkTime) throws ParseException {
        User user = userRepository.findByUserid(userid);
        if(user == null){
            throw new NotFoundUserException();
        }
        Walk walk = walkRepository.findByUserAndId(user, walkId);
        if(walk == null){
            throw new NotFoundWalkException();
        }
        //산책 코스 사진 s3에 등록
        String url="";
        try {
            url = upload(course, "course");
        }catch (IOException e){
            System.out.println("s3 등록 실패");
        }

        //산책 추가 정보 등록
        walk.updateWalk(stepCount, distance, calorie,walkTime, url);
        user.addWalk(walk);
    }
    @Override
    @Transactional
    public List<ResponseWalk.list> getAllMyWalk(String userid){
        User user = userRepository.findByUserid(userid);
        if(user == null){
            throw new NotFoundUserException();
        }
        List<Walk> walkList = walkRepository.findByUser(user);
        List<ResponseWalk.list> list = new ArrayList<>();
        for(Walk walk : walkList){
            ResponseWalk.list responseDto = ResponseWalk.list.builder()
                    .id(walk.getId())
                    .walkDate(walk.getWalkDate())
                    .stepCount(walk.getStepCount())
                    .distance(walk.getDistance())
                    .calorie(walk.getCalorie())
                    .walkTime(walk.getWalkTime())
                    .course(walk.getCourse())
                    .location(walk.getLocation())
                    .build();
            list.add(responseDto);
        }
        return list;
    }

    @Override
    @Transactional
    public void deleteWalk(String userId, String walkId){
        User user = userRepository.findByUserid(userId);
        if(user == null){
            throw new NotFoundUserException();
        }
        Walk walk = walkRepository.findByUserAndId(user, walkId);
        if(walk == null){
            throw new NotFoundWalkException();
        }
        walkRepository.delete(walk);
    }
    @Override
    @Transactional
    public Map<Date, ResponseWalk.calendar> getWalkForCalendar(String userId){
        Map<Date, ResponseWalk.calendar> map = new HashMap<>();

        User user = userRepository.findByUserid(userId);
        if(user == null){
            throw new NotFoundUserException();
        }
        List<Walk> walkList = walkRepository.findByUser(user);
        for(Walk walk : walkList){
            ResponseWalk.calendar response = ResponseWalk.calendar.builder()
                    .distance(walk.getDistance())
                    .stepCount(walk.getStepCount())
                    .walkTime(walk.getWalkTime())
                    .build();
            map.put(walk.getWalkDate(),response);
        }
        return map;
    }

    @Transactional
    @Override
    public ResponseWalk.mainInfo getTotalWalk(String userid) throws ParseException{
        Map<String, ResponseWalk.total> map = new HashMap<>();
        User user = userRepository.findByUserid(userid);
        if(user == null){
            throw new NotFoundUserException();
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //일 총합
        LocalDate now = LocalDate.now();
        LocalDate next = LocalDate.now().plusDays(1);
        Date today = format.parse(now.toString());
        Date nextDate = format.parse(next.toString());
        List<Walk> todayWalkList = walkRepository.findByUserAndWalkDateBetween(today, nextDate, user);

        ResponseWalk.total daily = ResponseWalk.total.builder()
                .stepCount(walkRepository.findByStepCount(today, today, user))
                .distance(walkRepository.findByDistance(today,today,user))
                .walkTime(getWalkTime(todayWalkList))
                .build();
        map.put("today", daily);
        //월 총합

        LocalDate start = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
        Date startDate = format.parse(start.toString());
        Date endDate = format.parse(end.toString());
        List<Walk> monthWalkList = walkRepository.findByUserAndWalkDateBetween(startDate, endDate, user);
        if(!monthWalkList.isEmpty()){
            ResponseWalk.total month = ResponseWalk.total.builder()
                    .stepCount(walkRepository.findByStepCount(startDate, endDate, user))
                    .distance(walkRepository.findByDistance(startDate,endDate,user))
                    .walkTime(getWalkTime(monthWalkList))
                    .build();
            map.put("month", month);
        }

        return ResponseWalk.mainInfo.builder()
                .nickName(user.getNickname())
                .walkTotal(map)
                .build();
    }
    private String getWalkTime(List<Walk> walkList){
        Integer hour =0;
        Integer minute =0;
        for(Walk walk :walkList){
            String[] time = walk.getWalkTime().split(":");
            hour+= Integer.parseInt(time[0]);
            minute+= Integer.parseInt(time[1]);
        }
        if(minute != 0){
            hour += minute/60;
            minute = minute%60;
        }
        return hour.toString()+"시간 "+minute.toString()+"분";
    }
    // S3 업로드
    public String upload(MultipartFile multipartFile, String dirName) throws IOException{
        //S3에 Multipartfile 타입은 전송이 안되므로 file로 타입 전환
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
        return upload(uploadFile, dirName);
    }
    private String upload(File uploadFile, String dirName){
        String fileName = dirName + "/"+ UUID.randomUUID().toString()+ uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }
    private String putS3(File uploadFile, String fileName){
        //외부에서 정적 파일을 읽을 수 있도록 public 읽기 권한으로 put
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        //업로드 된 파일의 S3 URL 주소 반환
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
    private void removeNewFile(File targetFile){
        // Multipartfile -> file로 전환되면서 로컬에 파일 생성된 것을 삭제
        if(targetFile.delete()){
            System.out.println("파일이 삭제되었습니다.");
        }else
        {
            System.out.println("파일이 삭제되지 않았습니다.");
        }
    }
    private Optional<File> convert(MultipartFile file) throws IOException{
     File convertFile = new File(file.getOriginalFilename());
     if(convertFile.createNewFile()){
         try (FileOutputStream fileOutputStream = new FileOutputStream(convertFile)){
             fileOutputStream.write(file.getBytes());
         }

         return Optional.of(convertFile);
     }
     return Optional.empty();
    }


}
