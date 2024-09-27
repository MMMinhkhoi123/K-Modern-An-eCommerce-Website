package com.leventsclone.leventsclone.repository;


import com.leventsclone.leventsclone.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IProductRepo extends JpaRepository<Product, Long> {
    @Query(value = "select * from product where UPPER(name_product) like upper(:name)", nativeQuery = true)
     Optional<Product> findByName(@Param(value = "name") String name);
}
