package com.findingjob.storage.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileStorageService {

    /**
     * Upload a file to the specified folder.
     * @return the file key (unique identifier for retrieval)
     */
    String upload(MultipartFile file, String folder);

    /**
     * Get an input stream for downloading the file.
     */
    InputStream download(String fileKey);

    /**
     * Delete the file.
     */
    void delete(String fileKey);

    /**
     * Get a URL to access the file.
     */
    String getUrl(String fileKey);

    /**
     * Get a pre-signed URL with expiration time (for temporary access).
     */
    String getPresignedUrl(String fileKey, int expiryMinutes);
}
