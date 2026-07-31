package com.findingjob.storage.controller;

import com.findingjob.common.dto.ApiResponse;
import com.findingjob.common.exception.BusinessException;
import com.findingjob.common.exception.ErrorCode;
import com.findingjob.storage.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/storage")
@Tag(name = "Storage", description = "File upload and download")
public class StorageController {

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${storage.type:local}")
    private String storageType;

    @PostMapping("/upload")
    @Operation(summary = "Upload a file")
    public ApiResponse<Map<String, String>> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "general") String folder) {

        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "File is empty");
        }

        String fileKey = fileStorageService.upload(file, folder);
        String url = fileStorageService.getUrl(fileKey);

        return ApiResponse.success(Map.of(
                "fileKey", fileKey,
                "url", url,
                "storageType", storageType
        ));
    }
}
