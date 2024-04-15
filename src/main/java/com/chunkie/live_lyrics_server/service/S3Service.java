package com.chunkie.live_lyrics_server.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.chunkie.live_lyrics_server.common.Constants;
import com.chunkie.live_lyrics_server.dto.LyricDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class S3Service {

    @Resource
    private AmazonS3 amazonS3;

    @Value(Constants.AWS.BUCKET)
    private String bucketName;

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

    public boolean uploadFile(String key, MultipartFile file) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            logger.info(file.getContentType());
            key = key.replace(getExtension(file), "");
            PutObjectResult result = amazonS3.putObject(bucketName, key, file.getInputStream(), metadata);
            return result.getMetadata() != null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public void deleteFile(String key) {
        amazonS3.deleteObject(bucketName, key);
    }

    public String getFile(String key) {
        try {
            S3Object object = amazonS3.getObject(bucketName, key);
            return getS3Url(object);
        } catch (AmazonS3Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public List<LyricDTO> getLyricsById(String folder) {
        try {
            ObjectListing objectListing = amazonS3.listObjects(bucketName, folder);
            List<LyricDTO> result = new ArrayList<>();
            for (S3ObjectSummary object : objectListing.getObjectSummaries()) {
                String key = object.getKey();
                if (key.equals(folder)) continue;
                String filename = key.substring(key.lastIndexOf("/") + 1);
                // Lyric files stored in S3 bucket are named with pattern "lyric_{language}_{id}"
                String language = filename.substring(filename.indexOf("_") + 1, filename.indexOf("_") + 3);
                String content = getObjectContent(bucketName, key);
                result.add(new LyricDTO(language, content));
            }
            return result;
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value())
                return null;
            else
                throw e;
        }
    }

    public List<Bucket> listBuckets() {
        return amazonS3.listBuckets();
    }

    private String getS3Url(S3Object s3Object) {
        Date expiration = new Date(System.currentTimeMillis() + (1000 * 60 * 60)); // 1 hour from now
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, s3Object.getKey())
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    private String getExtension(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null) {
            int dotIndex = originalName.lastIndexOf(".");
            if (dotIndex > 0) {
                extension = originalName.substring(dotIndex);
            }
        }
        return extension;
    }

    private String getObjectContent(String bucketName, String key) {
        S3Object s3Object = amazonS3.getObject(bucketName, key);
        try (InputStream inputStream = s3Object.getObjectContent()) {
            return IOUtils.toString(inputStream);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

}
