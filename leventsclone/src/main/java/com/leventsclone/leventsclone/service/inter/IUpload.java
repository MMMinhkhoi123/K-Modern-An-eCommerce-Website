package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.use.ImageUse;
import com.leventsclone.leventsclone.data.use.ImagesUse;
import com.leventsclone.leventsclone.entity.Image;

import java.util.List;

public interface IUpload {
    void save(List<String> list);
    void delete(List<String> list);

    List<ImagesUse> getAll();

    Image getImageByLink(String link);
}
