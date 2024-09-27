package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.response.OutfitRes;
import com.leventsclone.leventsclone.data.use.OutfitUse;
import com.leventsclone.leventsclone.data.use.ProductSizeColorUSe;
import com.leventsclone.leventsclone.entity.Outfit;

import java.util.List;
import java.util.Optional;

public interface IOutfit {
    List<OutfitUse> getAllR();
    List<OutfitUse> getAllSwiper();
    Outfit Save(String height, String weight, List<String> images, Long idOptionSize);
    Outfit update(String height, String weight, List<String> images, Long idOutfit);
    void  delete(List<Long> list);

    void  deleteMix(List<Long> list, Long idOutfit);
    OutfitUse getById(Long id);

    Outfit getEnById(Long id);
    List<OutfitUse> getByType(String type);
    OutfitRes getByNameAndColor(String data);

    void addProductMix(Long idOutfit, List<Long> list);

    Optional<Outfit> getByIdOfProDuctAndColorAndSize(Long idProduct, Long idSize,Long idColor);
}
