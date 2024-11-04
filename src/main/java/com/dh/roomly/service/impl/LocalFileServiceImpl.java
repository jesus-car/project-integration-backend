package com.dh.roomly.service.impl;
import com.dh.roomly.entity.FileEntity;
import com.dh.roomly.repository.IFileRepository;
import com.dh.roomly.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class LocalFileServiceImpl implements IFileService {

    @Value("${upload.directory}")
    private String uploadDirectory;

    private final IFileRepository fileRepository;

    public LocalFileServiceImpl(IFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<FileEntity> uploadFiles(List<MultipartFile> files) throws IOException {
        List<FileEntity> savedFiles = new ArrayList<>();

        Path directoryPath = Paths.get(uploadDirectory);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        for (MultipartFile file : files) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = directoryPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(fileName);
            fileEntity.setUrl(filePath.toString());
            savedFiles.add(fileEntity);
            fileRepository.save(fileEntity);
        }

        return savedFiles;
    }
}
