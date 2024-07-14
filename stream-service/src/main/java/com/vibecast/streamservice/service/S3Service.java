package com.vibecast.streamservice.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class S3Service {
    private final RedisTemplate<String, Object> redisTemplate;
    private final AmazonS3 s3Client;
    @Value("${s3.bucket.name}")
    private String bucketName;

    @Value("${s3.access.key}")
    private String accessKey;

    @Value("${s3.secret.key}")
    private String secretKey;

    @Value("${s3.endpoint.url}")
    private String serviceEndpoint;

    @Value("${s3.region}")
    private String region;

    private static final long URL_EXPIRATION_TIME_MILLIS = 1000 * 60 * 60; // 1 hour
    private static final long CACHE_EXPIRATION_BUFFER_MILLIS = 1000 * 60 * 5; // 5 minutes buffer

    public S3Service(RedisTemplate<String, Object> redisTemplate,
                     @Value("${s3.access.key}") String accessKey,
                     @Value("${s3.secret.key}") String secretKey,
                     @Value("${s3.endpoint.url}") String serviceEndpoint,
                     @Value("${s3.region}") String region) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.serviceEndpoint = serviceEndpoint;
        this.region = region;

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3Client = AmazonS3Client.builder()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(this.serviceEndpoint, this.region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withPathStyleAccessEnabled(true)
                .build();
        this.redisTemplate = redisTemplate;
    }

    public String generatePresignedUrl(String trackId) {
        String cachedUrl = (String) redisTemplate.opsForValue().get(trackId);
        if (cachedUrl != null) {
            return cachedUrl;
        }

        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += URL_EXPIRATION_TIME_MILLIS; // 1 hour
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, trackId)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

        long cacheExpirationMillis = URL_EXPIRATION_TIME_MILLIS - CACHE_EXPIRATION_BUFFER_MILLIS;
        redisTemplate.opsForValue().set(trackId, url.toString(), cacheExpirationMillis, TimeUnit.MILLISECONDS);

        return url.toString();
    }
}