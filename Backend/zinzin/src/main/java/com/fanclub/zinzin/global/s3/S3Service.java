package com.fanclub.zinzin.global.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
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

    public String insertProfile(MultipartFile multipartFile) {
        return upload(multipartFile, PROFILE_IMG_DIR);
    }

    public String insertCard(MultipartFile multipartFile) {
        return upload(multipartFile, CARD_IMG_DIR);
    }

    public String updateProfile(MultipartFile multipartFile, String currentPathName) {
        deleteS3(currentPathName);
        return upload(multipartFile, PROFILE_IMG_DIR);
    }

    public String updateCard(MultipartFile multipartFile, String currentPathName) {
        deleteS3(currentPathName);
        return upload(multipartFile, CARD_IMG_DIR);
    }

    // S3에 파일을 업로드하고 파일의 URL을 반환한다.
    private String upload(MultipartFile multipartFile, String dirName) {
        String uuid = UUID.randomUUID().toString();
        String pathName = dirName + "/" + uuid;

        File file = convert(multipartFile);
        String fileUploadUrl = putS3(file, pathName);
        file.delete(); // 로컬에 임시로 저장한 파일을 삭제한다.
        return fileUploadUrl;
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
    private void deleteS3(String pathName) {
        try {
            amazonS3.deleteObject(bucketName, pathName);
        } catch (AmazonServiceException e) {
            System.out.println(e.toString());
        }
    }

}
