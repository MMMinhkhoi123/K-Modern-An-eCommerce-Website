package com.leventsclone.leventsclone.repository;


import com.leventsclone.leventsclone.entity.Color;
import com.leventsclone.leventsclone.entity.Option;
import com.leventsclone.leventsclone.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOptionRepo extends JpaRepository<Option, Long> {
    List<Option> findAllByProduct(Product product);

    Option findByProductAndColor(Product product, Color color);
}
