package com.leventsclone.leventsclone.data.use;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OderUse {
    private OptionUse optionUse;
    private SizeUse sizeUse = new SizeUse();

    private double price;
    private int priceAfterDefect;
    private String nameImg;
    private ColorUse color = new ColorUse();
    private int quality;
    private int quantityDefect;
    private String describeDefect;
    private boolean packaging;
}
