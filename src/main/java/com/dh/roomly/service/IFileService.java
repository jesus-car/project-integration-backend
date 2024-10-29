package com.dh.roomly.service;

import com.dh.roomly.entity.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IFileService {
    List<FileEntity> saveFiles(List<MultipartFile> files) throws IOException;
}
