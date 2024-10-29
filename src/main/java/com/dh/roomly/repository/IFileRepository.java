package com.dh.roomly.repository;

import com.dh.roomly.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFileRepository extends JpaRepository<FileEntity, Long> {
}
