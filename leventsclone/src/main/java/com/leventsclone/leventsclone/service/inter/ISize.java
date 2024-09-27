package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.use.*;
import com.leventsclone.leventsclone.entity.Size;

import java.util.List;

public interface ISize {
    SizeUse getById(Long id);
    Size getByIdET(Long id);
    void delete(Long id);
    void save(SizeUse sizeUse);
    void update(SizeUse sizeUse);
    List<SizeUse> getSizeFilter(List<OptionUse> data);

    void updateSize(List<SizeUse> data, Long idProduct, Long idColor);

    boolean checkSize(List<SizeUse> sizeUses, SizeUse sizeUse);

    SizeUse getByName(String name);

    List<SizeUse> getAll();
    List<SizeUse> getByNameTypeSize(String name);
    List<SizeUse> getByIdTypeSize(Long id);
    List<TypeSizeUse> getAllTypeSize();
}
