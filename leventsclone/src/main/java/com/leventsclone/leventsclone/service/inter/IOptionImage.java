package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.use.ImagesUse;
import com.leventsclone.leventsclone.data.use.OptionImageUse;
import com.leventsclone.leventsclone.entity.Option;

import java.util.List;

public interface IOptionImage {
    List<OptionImageUse> getAllByOption(Option option);

    void  save(List<String> imgs, Option option);
    void  update(List<String> imageNew, Option option);

    void delete(Long id);
}
