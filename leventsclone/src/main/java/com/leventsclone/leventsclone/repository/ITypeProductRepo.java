package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.TypeProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ITypeProductRepo extends JpaRepository<TypeProduct,Long> {
    @Query(value = "select * from  type_product where key_type_product = :key", nativeQuery = true)
    TypeProduct findByKey(@Param("key") String key);

    @Query(value = "select * from  type_product where id_directory = :idDirectory", nativeQuery = true)
    List<TypeProduct> findByIdDirectory(@Param("idDirectory") long id);

    Optional<TypeProduct> findByKeyTypeProduct(String key);
}
