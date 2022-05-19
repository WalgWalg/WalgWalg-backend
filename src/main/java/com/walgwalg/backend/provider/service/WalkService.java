package com.walgwalg.backend.provider.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.walgwalg.backend.entity.Gps;
import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.entity.Walk;
import com.walgwalg.backend.exception.errors.NotFoundUserException;
import com.walgwalg.backend.exception.errors.NotFoundWalkException;
import com.walgwalg.backend.repository.GpsRepository;
import com.walgwalg.backend.repository.UserRepository;
import com.walgwalg.backend.repository.WalkRepository;
import com.walgwalg.backend.web.dto.RequestWalk;
import com.walgwalg.backend.web.dto.ResponseGps;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:/secrets/application-s3.properties")
public class WalkService {
    private final AmazonS3Client amazonS3Client;
    private final UserRepository userRepository;
    private final WalkRepository walkRepository;
    private final GpsRepository gpsRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public Long startWalk(String userid, Date walkDate, String location){
        User user = userRepository.findByUserid(userid);
        if(user == null){
            throw new NotFoundUserException();
        }
        Walk walk = Walk.builder()
                .user(user)
                .walkDate(walkDate)
                .location(location)
                .build();
        walk = walkRepository.save(walk);
        return walk.getId();
    }
    public void addGps(String userid, Date walkDate, Double latitude, Double longitude){
        User user = userRepository.findByUserid(userid);
        if(user == null){
            throw new NotFoundUserException();
        }
        Walk walk = walkRepository.findByUserAndWalkDate(user, walkDate);
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
    public List<ResponseGps.gps> getGps(Long walkId){
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
    public void registerWalk(String userid,MultipartFile course, RequestWalk.registerWalk requestDto){
        User user = userRepository.findByUserid(userid);
        if(user == null){
            throw new NotFoundUserException();
        }
        Walk walk = walkRepository.findByUserAndWalkDate(user, requestDto.getWalkDate());
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
        walk = Walk.builder()
                .step_count(requestDto.getStep_count())
                .distance(requestDto.getDistance())
                .calorie(requestDto.getCalorie())
                .walkTime(requestDto.getWalkTime())
                .course(url)
                .build();
        walkRepository.save(walk);
        user.addWalk(walk);
    }

    public String upload(MultipartFile multipartFile, String dirName) throws IOException{
        //S3에 Multipartfile 타입은 전송이 안되므로 file로 타입 전환
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
        return upload(uploadFile, dirName);
    }
    private String upload(File uploadFile, String dirName){
        String fileName = dirName + "/"+ uploadFile.getName();
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
