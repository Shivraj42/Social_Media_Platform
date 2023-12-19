package com.example.social_media_plateform.Services.Impls;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws IOException {
        String key = generateKey(file.getOriginalFilename());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3Client.putObject(new PutObjectRequest(bucketName, key, file.getInputStream(), metadata));

        return amazonS3Client.getUrl(bucketName, key).toString();
    }

    public InputStream downloadFile(String key) {
        S3Object object = amazonS3Client.getObject(bucketName, key);
        return object.getObjectContent();
    }

    private String generateKey(String filename) {

        return System.currentTimeMillis() + "_" + filename;
    }
}

