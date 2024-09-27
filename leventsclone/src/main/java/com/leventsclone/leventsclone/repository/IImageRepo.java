package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.Image;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IImageRepo extends JpaRepository<Image, Long> {
}
