package com.leventsclone.leventsclone.data.response;

import com.leventsclone.leventsclone.data.use.CollectionUse;
import com.leventsclone.leventsclone.data.use.OptionUse;
import com.leventsclone.leventsclone.data.use.ProductSizeColorUSe;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CollectionRes {
    CollectionUse collectionUse;
    List<OptionUse> optionUses;
}
