package org.gordeser.backend.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gordeser.backend.configuration.AwsConfig;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsService {
    private final AmazonS3 s3client;

    private final AwsConfig awsConfig;

    public String uploadFile(String keyName, Long contentLength, String contentType, InputStream value) throws AmazonClientException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        metadata.setContentType(contentType);

        s3client.putObject(awsConfig.getBucketName(), keyName, value, metadata);
        log.info("File uploaded to bucket: {}", keyName);

        return String.format("https://%s.s3.%s.amazonaws.com/%s", awsConfig.getBucketName(), awsConfig.getRegion(), keyName);
    }
}
