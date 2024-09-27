package com.leventsclone.leventsclone.data.use;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ProductSizeColorUSe {
    private Date date;
    private ProductUse productUse;
    private ColorUse colorUse;
    private SizeUse sizeUse;
    private int sumCountAssess;
    private Set<ColorUse> colorUses;
    private Set<SizeUse> sizeUses = new HashSet<>();
    private Set<ImagesUse> imagesUses = new HashSet<>();
    private Set<ImagesUse> imagesUsesFeedback;
    private int quantity;
    private int sold;
    private Boolean isUsing;
}
