package com.ttokttak.jellydiary.util;

import com.ttokttak.jellydiary.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;

@Component
@RequiredArgsConstructor
public class S3Uploader {
    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    // S3 단일 객체 업로드
    public String uploadToS3(MultipartFile multipartFile, String s3Path) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Path)
                    .contentType(multipartFile.getContentType())
                    .contentLength(multipartFile.getSize())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(multipartFile.getBytes()));

        } catch (Exception e) {
            throw new CustomException(S3_UPLOAD_FAILED);
        }
        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(s3Path)).toExternalForm();
    }

    // 단일 객체 삭제
    public void deleteObject(String objectPath) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectPath)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            throw new CustomException(S3_DELETE_FAILED);
        }
    }

    // TODO: 수정해야합니다~
    // 다중 객체 업로드
//    public List<String> uploadMultipleFilesToS3(List<MultipartFile> files, String baseS3Path) throws IOException {
//        return files.stream().map(file -> {
//            String individualPath = baseS3Path + "/" + file.getOriginalFilename(); // 각 파일의 S3 경로 생성
//            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(individualPath)
//                    .contentType(file.getContentType())
//                    .contentLength(file.getSize())
//                    .acl(ObjectCannedACL.PUBLIC_READ)
//                    .build();
//            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes())); // 파일을 S3에 업로드
//
//            // 업로드된 파일의 URL 반환
//            return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(individualPath)).toExternalForm();
//        }).collect(Collectors.toList()); // 모든 파일의 URL을 리스트로 수집하여 반환
//    }

    // 주어진 URL에서 S3의 키 부분만 추출
    public String extractKeyFromUrl(String url) {
        // URL에서 도메인을 제거하고 첫번째 '/' 이후의 부분을 반환합니다.
        return url.substring(url.indexOf(".com/") + 5);
    }
}


