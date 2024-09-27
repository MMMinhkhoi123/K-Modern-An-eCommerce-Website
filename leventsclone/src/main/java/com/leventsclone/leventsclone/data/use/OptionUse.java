package com.leventsclone.leventsclone.data.use;

import lombok.Getter;
import lombok.Setter;


import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class OptionUse {
    private Long idOpt;
    private int price;
    private Date date;
    private ProductUse productUse;
    private ColorUse colorUse;
    private int countRating;
    private boolean isUsing;
    private Set<OptionSizeUSe> optionSizeUSes;
    private Set<OptionImageUse> optionImageUses;
    public  int getPriceDiscount() {
        if(productUse == null) {
            return  0;
        }
        int priceOriginal = price + productUse.getPrice();
        return (int) (priceOriginal  -  (priceOriginal* ((double)productUse.getPercent() / (double)100)));
    }

    public int getPriceCorrect() {
        if(productUse == null) {
            return  0;
        }
        if(productUse.getIdEvent() == null) {
            return  getPriceNormal();
        }else  {
            return getPriceDiscount();
        }
    }
    public  int getPriceNormal() {
        if(productUse == null) {
            return  0;
        }
        return price + productUse.getPrice();
    }
}
//