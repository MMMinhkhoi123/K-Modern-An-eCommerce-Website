package com.leventsclone.leventsclone.data.use;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductUse {
    private Long id;
    private String name;
    private TypeProductUse typeProductUse;
    private Integer price;
    private double priceDiscount;
    private Long idEvent;
    @Size
    private String describe;
    private boolean classic;
    private boolean isUsing;
    private int percent;
    private List<ColorUse> colorUses;
}
