package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.OptionSize;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductOptionDetailRepo extends JpaRepository<OptionSize, Long> {
}
