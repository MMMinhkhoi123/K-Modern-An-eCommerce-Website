package com.leventsclone.leventsclone.data.response;

import com.leventsclone.leventsclone.data.use.OptionUse;
import com.leventsclone.leventsclone.data.use.ProductSizeColorUSe;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductRes {
    private int thisPage;
    private int countPage;
    private int countProduct;
    private List<ProductSizeColorUSe> productUses = new ArrayList<>();
    private List<OptionUse> optionUses = new ArrayList<>();
}
