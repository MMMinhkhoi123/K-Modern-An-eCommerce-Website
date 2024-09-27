package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.TypeSize;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITypeSizeRepo extends JpaRepository<TypeSize, Long> {
    public TypeSize findByNameTypeSize(String name);
}
