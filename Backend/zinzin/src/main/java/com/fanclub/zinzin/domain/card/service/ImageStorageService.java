package com.fanclub.zinzin.domain.card.service;

import com.fanclub.zinzin.global.error.code.CardErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageStorageService {

    private final Path fileStorageLocation;

    public ImageStorageService(@Value("${spring.file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new BaseException(CardErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    public String storeFile(MultipartFile file, Long memberId) {
        try {
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")){
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            String newFileName = memberId.toString() + '-' + UUID.randomUUID().toString() + fileExtension;

            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation.toString();
        } catch (IOException ex) {
            throw new BaseException(CardErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }
}
