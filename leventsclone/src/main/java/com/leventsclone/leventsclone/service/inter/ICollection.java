package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.response.CollectionRes;
import com.leventsclone.leventsclone.data.use.CollectionUse;
import com.leventsclone.leventsclone.entity.Collection;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ICollection {
    List<CollectionUse> getAll();

    CollectionUse getById(Long id);

    List<CollectionUse> getALLSwiper(int number);

    void update(String id, String name,Optional<MultipartFile> multipartFile, boolean dark , List<String> Products, String content);
    void save(String name, MultipartFile multipartFile, boolean dark , List<String> Products, String content);

    void delete(List<String> list);
    boolean checkContainCollection(String nameCollection);

    CollectionRes getAdequate(String nameCollection);

    CollectionUse getByKey(String key);
}
