package com.toeicify.toeic.service.impl;

import com.toeicify.toeic.exception.ResourceInvalidException;
import com.toeicify.toeic.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.Set;
import java.util.UUID;

/**
 * Created by hungpham on 7/11/2025
 */
@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

//    @Value("${cloud.public-url}")
//    private String publicUrl;

    @Value("${cloud.bucket}")
    private String bucket;

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "image/png", "image/jpeg", "image/gif", "image/webp", "image/bmp",
            "audio/mpeg", "audio/wav", "audio/ogg", "audio/x-wav", "audio/mp4", "audio/webm"
    );

    @Override
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        if (file.isEmpty() || file.getSize() == 0) {
            throw new ResourceInvalidException("Uploaded file is empty.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new ResourceInvalidException("File name is missing or invalid.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType)) {
            throw new ResourceInvalidException("Only image and audio files are allowed.");
        }

        if (!originalFilename.matches("(?i).+\\.(png|jpe?g|gif|webp|bmp|mp3|wav|ogg|mp4|webm)$")) {
            throw new ResourceInvalidException("Unsupported file extension.");
        }

        String filename = UUID.randomUUID() + "-" + originalFilename;
        String key = String.format("%s/%s", folder.replaceAll("^/+", "").replaceAll("/+$", ""), filename);

        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        s3Client.putObject(req, RequestBody.fromBytes(file.getBytes()));

//        return String.join("/", publicUrl.replaceAll("/+$", ""), key);
        return key;
    }
    @Override
    public String generateDownloadUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofDays(7))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
}
