package com.leventsclone.leventsclone.entity.relationship;

import com.leventsclone.leventsclone.entity.Color;
import com.leventsclone.leventsclone.entity.Product;
import com.leventsclone.leventsclone.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RelationShipUserProductColor implements Serializable {
    private User user;
    private Color color;
    private Product product;
}
