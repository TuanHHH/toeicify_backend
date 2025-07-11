package com.toeicify.toeic.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by hungpham on 7/11/2025
 */
public interface MediaService {
    String uploadFile(MultipartFile file, String folder) throws IOException;

    String generateDownloadUrl(String key);
}
