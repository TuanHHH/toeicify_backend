package com.toeicify.toeic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

/**
 * Created by hungpham on 7/11/2025
 */
@Configuration
public class S3PresignerConfig {

    @Value("${cloud.endpoint}")
    private String endpoint;

    @Value("${cloud.accessKey}")
    private String accessKey;

    @Value("${cloud.secretKey}")
    private String secretKey;

    @Value("${cloud.region:us-west-004}")
    private String region;

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)))
                .region(Region.of(region))
                .build();
    }
}

