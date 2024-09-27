package com.leventsclone.leventsclone.data.response;

import com.leventsclone.leventsclone.data.use.ImagesUse;
import com.leventsclone.leventsclone.data.use.OutfitUse;
import com.leventsclone.leventsclone.data.use.ProductSizeColorUSe;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class OutfitRes {
    private OutfitUse outfitsUse;
    private Set<ImagesUse> imagesUses;
    private ProductSizeColorUSe productSizeColorUSe;
    private List<ProductSizeColorUSe> productSizeColorUSes;
}
