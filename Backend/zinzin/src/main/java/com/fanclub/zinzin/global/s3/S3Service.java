package com.fanclub.zinzin.global.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fanclub.zinzin.global.error.code.FileErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final String PROFILE_IMG_DIR = "profile";
    private final String CARD_IMG_DIR = "card";

    // S3에 프로필 이미지를 업로드하고 URL을 반환한다.
    public String uploadProfile(MultipartFile multipartFile) {
        return upload(multipartFile, PROFILE_IMG_DIR);
    }

    // S3에 카드 이미지를 업로드하고 URL을 반환한다.
    public String uploadCard(MultipartFile multipartFile) {
        return upload(multipartFile, CARD_IMG_DIR);
    }

    // 기존 URL에 해당하는 프로필 이미지를 삭제하고, 새로운 프로필 이미지를 업로드하여 URL을 반환한다.
    public String replaceProfile(MultipartFile multipartFile, String currentURL) {
        return replace(multipartFile, PROFILE_IMG_DIR, currentURL);
    }

    // 기존 URL에 해당하는 카드 이미지를 삭제하고, 새로운 카드 이미지를 업로드하여 URL을 반환한다.
    public String replaceCard(MultipartFile multipartFile, String currentURL) {
        return replace(multipartFile, CARD_IMG_DIR, currentURL);
    }

    // S3에 파일을 업로드하고 URL을 반환한다.
    private String upload(MultipartFile multipartFile, String dirName) {
        String uuid = UUID.randomUUID().toString();
        String pathName = dirName + "/" + uuid;

        File file = convert(multipartFile);
        String newURL = putS3(file, pathName);
        file.delete(); // 로컬에 임시로 저장한 파일을 삭제한다.
        return newURL;
    }

    // 기존 URL에 해당하는 파일을 삭제하고, 새로운 파일을 업로드하여 URL을 반환한다.
    private String replace(MultipartFile multipartFile, String dirName, String currentURL) {
        String newURL = upload(multipartFile, dirName); // 새로운 파일 업로드
        deleteS3(currentURL); // 기존 파일 삭제
        return newURL; // 새로운 파일에 해당하는 URL 반환
    }

    // multipartFile을 로컬에 임시로 저장한다.
    private File convert(MultipartFile multipartFile) {
        String uniqueFileName = createUniqueFileName(multipartFile);
        File file = new File(System.getProperty("user.home") + "/" + uniqueFileName);

        try {
            if (file.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(multipartFile.getBytes());
                } catch (IOException e) {
                    throw new BaseException(FileErrorCode.FILE_UPLOAD_WRITE_FAILED);
                }

                return file;
            }
        } catch (IOException e) {
            throw new BaseException(FileErrorCode.FILE_UPLOAD_CREATE_FAILED);
        }

        throw new BaseException(FileErrorCode.FILE_UPLOAD_CONVERT_FAILED);
    }

    // 중복되지 않는 파일 이름을 생성한다.
    private String createUniqueFileName(MultipartFile multipartFile) {
        String originalFileName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();

        if (originalFileName == null || originalFileName.isEmpty()) {
            originalFileName = "null";
        }

        return uuid + "_" + originalFileName.replaceAll("\\s", "_");
    }

    // S3에 파일을 업로드한다.
    private String putS3(File file, String pathName) {
        amazonS3.putObject(new PutObjectRequest(bucketName, pathName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return URLDecoder.decode(amazonS3.getUrl(bucketName, pathName).toString(), StandardCharsets.UTF_8);
    }

    // S3에서 파일을 삭제한다.
    public void deleteS3(String url) {
        AmazonS3URI s3URI = new AmazonS3URI(url);

        try {
            amazonS3.deleteObject(bucketName, s3URI.getKey());
        } catch (AmazonServiceException e) {
            System.out.println("파일 삭제 실패 : " + s3URI.getKey());
            System.out.println(e.toString());
        }
    }

}
