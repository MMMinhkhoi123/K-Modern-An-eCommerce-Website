package com.leventsclone.leventsclone.unstil;


import com.leventsclone.leventsclone.data.use.ProductUse;
import com.leventsclone.leventsclone.entity.Product;

public class ConvertToEntity {

    public Product getProduct(ProductUse productUse) {
        Product product = new Product();
        if(productUse.getId() != null) {
            product.setIdProduct(productUse.getId());
        }
        product.setDescribeProduct(productUse.getDescribe());
        product.setNameProduct(productUse.getName());
        product.setPriceProduct(productUse.getPrice());
        product.setClassicProduct(productUse.isClassic());
        return  product;
    }
}
