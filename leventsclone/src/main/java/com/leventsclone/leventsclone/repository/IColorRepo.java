package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IColorRepo extends JpaRepository<Color, Long> {
    @Query(value = "select * from color where UPPER(name_color) like UPPER(:name)", nativeQuery = true)
     Optional<Color> getColorByName (@Param(value = "name") String name);

    @Query(value = "select * from color where UPPER(code_color) like UPPER(:code)", nativeQuery = true)
    Optional<Color> findColorByCodeColor (@Param(value = "code") String code);

}
