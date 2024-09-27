package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.data.use.OptionSizeUSe;
import com.leventsclone.leventsclone.entity.Option;
import com.leventsclone.leventsclone.entity.OptionSize;
import com.leventsclone.leventsclone.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOptionSizeRepo extends JpaRepository<OptionSize, Long> {
    List<OptionSize> findAllByOption(Option option);

    OptionSize findByOptionAndSize(Option option, Size size);
 }
