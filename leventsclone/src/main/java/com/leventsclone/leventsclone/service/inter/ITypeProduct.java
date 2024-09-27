package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.use.TypeProductUse;
import com.leventsclone.leventsclone.entity.TypeProduct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ITypeProduct {
    TypeProduct getByName(String name);
    TypeProductUse getById(Long id);
    TypeProduct getByIdEtt(Long id);
    Optional<TypeProduct> getByUrl(String url);
    List<TypeProductUse> getALlByIdDirectory(long id);
    List<TypeProductUse> getAll();

    void save(TypeProductUse typeProductUse);
    void update(TypeProductUse typeProductUse);
    void delete(Long id);
}
