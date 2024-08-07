package com.fanclub.zinzin.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 파일 업로드
    private String putS3(File file, String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return URLDecoder.decode(amazonS3.getUrl(bucketName, fileName).toString(), StandardCharsets.UTF_8);
    }

    // 파일 삭제
    private void delete(String fileName) {
        amazonS3.deleteObject(bucketName, fileName);
    }

}
