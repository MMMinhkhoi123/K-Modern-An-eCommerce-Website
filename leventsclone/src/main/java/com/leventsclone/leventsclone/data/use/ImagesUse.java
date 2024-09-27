package com.leventsclone.leventsclone.data.use;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ImagesUse {
    private String name;
    private String key;
    private boolean save;
    private ProductUse productUse;
    private ColorUse colorUse;
    private String imgProduct;
    private Date dateCreate;
}
