package com.leventsclone.leventsclone.entity.relationship;

import com.leventsclone.leventsclone.entity.Color;
import com.leventsclone.leventsclone.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RelationShipProductImage implements Serializable {
    private Product product;
    private Color color;
    private String nameImage;
}
