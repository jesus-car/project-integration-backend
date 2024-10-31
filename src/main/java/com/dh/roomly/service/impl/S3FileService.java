package com.dh.roomly.service.impl;

import com.dh.roomly.entity.FileEntity;
import com.dh.roomly.repository.IFileRepository;
import com.dh.roomly.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class S3FileService implements IFileService {

    private final S3Client s3Client;

    private final IFileRepository fileRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public S3FileService(S3Client s3Client, IFileRepository fileRepository) {

        this.s3Client = s3Client;
        this.fileRepository = fileRepository;
    }

    @Override
    public List<FileEntity> uploadFiles(List<MultipartFile> files) throws IOException {
        List<FileEntity> fileEntities = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); // Genera el nombre del archivo
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );

            String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;

            FileEntity fileEntity = new FileEntity();
            fileEntity.setUrl(fileUrl);
            fileEntity.setName(fileName);
            fileEntities.add(fileEntity);
            fileRepository.save(fileEntity);
        }
        return fileEntities;
    }
}