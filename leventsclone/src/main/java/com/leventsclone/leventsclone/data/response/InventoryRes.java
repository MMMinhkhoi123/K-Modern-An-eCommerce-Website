package com.leventsclone.leventsclone.data.response;

import com.leventsclone.leventsclone.data.use.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRes {
    private OptionUse optionUse;
    private SizeUse sizeUse;
    private int quantityOld;
    private int quantity;
    private String img;
}
