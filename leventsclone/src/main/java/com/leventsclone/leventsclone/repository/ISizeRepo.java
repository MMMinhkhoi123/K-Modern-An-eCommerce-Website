package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.Size;
import com.leventsclone.leventsclone.entity.TypeSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ISizeRepo extends JpaRepository<Size, Long> {

    @Query(value = "select  * from size  where UPPER(name_size) like :nameSize ", nativeQuery = true)
    Size getByName(@Param(value = "nameSize") String nameSize);

    Optional<List<Size>> findAllByTypeSize(TypeSize typeSize);
}
