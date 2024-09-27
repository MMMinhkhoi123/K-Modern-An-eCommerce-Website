package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.Directory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IDirectoryRepo extends JpaRepository<Directory,Long> {
    Optional<Directory> findByNameDirectory(String name);

    Optional<Directory> findByIdDirectory(Long id);

    Optional<Directory> findByKeyDirectory(String url);
}
