package com.leventsclone.leventsclone.data.use;

import lombok.Data;

@Data
public class CartUse {
    private int quantity;
    private int sumPrice;
    private OptionSizeUSe optionSizeUSe;
    private OptionUse optionUse;
}
