package com.leventsclone.leventsclone.data.use;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class OutfitUse {
    private Long id;
    private String height;
    private String weight;
    private OptionSizeUSe optionSizeUSe;
    private OptionUse optionUse;
    private List<OptionUse> optionUses;
    private List<ImageUse> imagesUses;
}
