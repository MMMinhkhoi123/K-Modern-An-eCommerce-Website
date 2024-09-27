package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.Option;
import com.leventsclone.leventsclone.entity.OptionImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IOptionImageRepo extends JpaRepository<OptionImage, Long> {
    Optional<List<OptionImage>> findAllByOption(Option option);
}
