package com.vibecast.viewservice.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class S3Service {
    private final AmazonS3 s3Client;
    private final String bucketName = "vibecast";
    private final String accessKey = "user";
    private final String secretKey = "password";
    private final Logger logger = LoggerFactory.getLogger(S3Service.class);

    public S3Service() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
                accessKey, secretKey);
        this.s3Client = AmazonS3Client.builder()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:9000", "us-east-1"))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withPathStyleAccessEnabled(true)
                .build();
    }

    public void uploadTrack(File file, String trackId) {
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, trackId, file);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.length());
            putObjectRequest.setMetadata(metadata);
            s3Client.putObject(putObjectRequest);
            logger.info("Upload completed with trackId: " + trackId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error uploading to s3");
        }
    }
}

