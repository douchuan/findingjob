package com.findingjob.storage.service;

import com.findingjob.common.exception.BusinessException;
import com.findingjob.common.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileStorage implements FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(LocalFileStorage.class);

    @Value("${storage.local-path:./uploads}")
    private String localPath;

    @Override
    public String upload(MultipartFile file, String folder) {
        try {
            String fileKey = folder + "/" + UUID.randomUUID() + "_" + sanitizeFileName(file.getOriginalFilename());
            Path targetPath = Paths.get(localPath, fileKey);

            Files.createDirectories(targetPath.getParent());
            Files.write(targetPath, file.getBytes());

            log.info("File uploaded: {}", fileKey);
            return fileKey;
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED, e.getMessage());
        }
    }

    @Override
    public InputStream download(String fileKey) {
        try {
            Path path = Paths.get(localPath, fileKey);
            return new FileInputStream(path.toFile());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "File not found: " + fileKey);
        }
    }

    @Override
    public void delete(String fileKey) {
        try {
            Path path = Paths.get(localPath, fileKey);
            Files.deleteIfExists(path);
            log.info("File deleted: {}", fileKey);
        } catch (IOException e) {
            log.warn("Failed to delete file: {}", fileKey, e);
        }
    }

    @Override
    public String getUrl(String fileKey) {
        return "/api/storage/files/" + fileKey;
    }

    @Override
    public String getPresignedUrl(String fileKey, int expiryMinutes) {
        long expiresAt = System.currentTimeMillis() + (expiryMinutes * 60L * 1000L);
        return "/api/storage/files/" + fileKey + "?expires=" + expiresAt;
    }

    private String sanitizeFileName(String fileName) {
        if (fileName == null) {
            return "unnamed";
        }
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
