package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.data.dto.ColorDto;
import com.leventsclone.leventsclone.data.use.OptionUse;
import com.leventsclone.leventsclone.data.use.ProductSizeColorUSe;
import com.leventsclone.leventsclone.data.use.ColorUse;
import com.leventsclone.leventsclone.entity.Color;
import com.leventsclone.leventsclone.repository.IColorRepo;
import com.leventsclone.leventsclone.service.inter.IColor;
import com.leventsclone.leventsclone.unstil.ConvertToOtherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ColorSer implements IColor {
    private boolean statusColor;
    private final IColorRepo COLOR_REPO;
    private ConvertToOtherData convertToOtherData = new ConvertToOtherData();

    @Autowired
    public  ColorSer(IColorRepo colorRepo) {
        COLOR_REPO = colorRepo;
    }

    @Override
    public List<ColorUse> getColorFilter(List<OptionUse> data) {
        List<ColorUse> colorUse = new ArrayList<>();
        data.forEach((item) -> {
            if(checkColor(colorUse, item.getColorUse())) {
                colorUse.add(item.getColorUse());
            }
        } );
        return colorUse;
    }



    @Override
    public boolean checkColor(List<ColorUse> data, ColorUse color) {
        statusColor = true;
        data.forEach((e) -> {
            if(e.getId().equals(color.getId())) {
              statusColor = false;
            }
        });
        return statusColor;
    }

    @Override
    public ColorUse getColorByName(String name) {
        return convertToOtherData.getColorUse(COLOR_REPO.getColorByName(name).orElseThrow());
    }

    @Override
    public boolean checkColorCode(String code) {
        if(COLOR_REPO.findColorByCodeColor(code).isPresent()) {
            return true;
        } else  {
            return false;
        }
    }

    @Override
    public boolean checkColorName(String name) {
        if(COLOR_REPO.getColorByName(name).isPresent()) {
            return true;
        } else  {
            return false;
        }
    }


    @Override
    public ColorUse getColorById(Long id) {
        return convertToOtherData.getColorUse(COLOR_REPO.findById(id).orElseThrow());
    }

    @Override
    public Color getColorByIdEt(Long id) {
        return COLOR_REPO.findById(id).orElseThrow();
    }

    @Override
    public void save(ColorDto colorDto) {
        Color color = new Color();
        color.setNameColor(colorDto.getName());
        color.setCodeColor(colorDto.getCode());
        COLOR_REPO.save(color);
    }

    @Override
    public void delete(Long id) {
        COLOR_REPO.deleteById(id);
    }

    @Override
    public void update(ColorDto colorDto) {
        Color color = new Color();
        color.setIdColor(colorDto.getId());
        color.setNameColor(colorDto.getName());
        color.setCodeColor(colorDto.getCode());
        COLOR_REPO.save(color);
    }

    private void sort(List<ColorUse> colorUses) {
        Comparator<ColorUse> resComparator = new Comparator<ColorUse>() {
            @Override
            public int compare(ColorUse o1, ColorUse o2) {
                return Double.compare(o2.getId() , o1.getId());
            }
        };
        colorUses.sort(resComparator);
    }

    @Override
    public List<ColorUse> getAll() {
        List<ColorUse> colorUseList = new ArrayList<>();
        COLOR_REPO.findAll().forEach((e) -> {
            ColorUse colorUse = convertToOtherData.getColorUse(e);
            colorUseList.add(colorUse);
        });
        sort(colorUseList);
        return colorUseList;
    }




}
