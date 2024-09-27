package com.leventsclone.leventsclone.data.use;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SizeUse {
    private long id;
    private String type;
    private long idType;
    private String name;
    private boolean isUsing;
    private int quantity;
}
