package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.dto.ColorDto;
import com.leventsclone.leventsclone.data.use.OptionUse;
import com.leventsclone.leventsclone.data.use.ProductSizeColorUSe;
import com.leventsclone.leventsclone.data.use.ColorUse;
import com.leventsclone.leventsclone.entity.Color;
import com.leventsclone.leventsclone.entity.Product;

import java.util.List;

public interface IColor {
    List<ColorUse> getColorFilter(List<OptionUse> data);
    boolean checkColor(List<ColorUse> data, ColorUse color);
    ColorUse getColorByName(String name);
    boolean checkColorCode(String code);

    boolean checkColorName(String name);

    ColorUse getColorById(Long id);

    Color getColorByIdEt(Long id);
    void save(ColorDto colorDto);

    void delete(Long id);

    void update(ColorDto colorDto);

    List<ColorUse> getAll();


}
