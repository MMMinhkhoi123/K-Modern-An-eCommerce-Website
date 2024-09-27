package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.use.ProductUse;
import com.leventsclone.leventsclone.entity.EventProduct;
import com.leventsclone.leventsclone.entity.Product;

import java.util.List;
import java.util.Optional;

public interface IProduct {
    ProductUse getByName(String name);
    List<ProductUse> getAllUseEvent();
    List<Product> getAllByListEventProduct(List<EventProduct> eventProducts);
    List<ProductUse> getAllUsingEvent(Long idEvent);
    Optional<Product> getByNameR(String name);
    ProductUse getById(long id);
    Product getByIdEt(long id);
    void delete(Long id);
    boolean save(ProductUse productUse);
    Product save(Product product);
    boolean update(ProductUse productUse);
    List<ProductUse> getAll();
}
