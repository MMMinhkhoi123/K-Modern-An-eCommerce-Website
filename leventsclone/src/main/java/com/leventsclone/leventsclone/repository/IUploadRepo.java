package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUploadRepo extends JpaRepository<Upload, Long> {
    Optional<Upload> findByLinkUpload(String link);
}
