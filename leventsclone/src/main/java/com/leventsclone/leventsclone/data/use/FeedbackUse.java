package com.leventsclone.leventsclone.data.use;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FeedbackUse {
    private Long id;
    private ImageUse imageUse;
    private OptionUse optionUse;

    private int sumCountAssess;
    private String imgProduct;
    private List<ImagesUse> imagesUses = new ArrayList<>();
    private List<ColorUse> colorUses = new ArrayList<>();
}
