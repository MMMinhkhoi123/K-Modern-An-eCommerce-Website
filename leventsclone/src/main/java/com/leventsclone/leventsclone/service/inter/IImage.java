package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.use.ImageUse;
import com.leventsclone.leventsclone.data.use.ImagesUse;
import com.leventsclone.leventsclone.entity.*;

import java.util.List;
import java.util.Set;

public interface IImage {

    Image save(Image image);
    void deleteById(Long idImage);

    Image SaveTypeFeedBack(ImagesUse imagesUse);

    ImageUse getById(Long id);

}
