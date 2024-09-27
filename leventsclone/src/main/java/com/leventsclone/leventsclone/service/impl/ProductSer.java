package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.data.use.ProductUse;
import com.leventsclone.leventsclone.entity.EventProduct;
import com.leventsclone.leventsclone.entity.Product;
import com.leventsclone.leventsclone.entity.TypeProduct;
import com.leventsclone.leventsclone.repository.IProductRepo;
import com.leventsclone.leventsclone.service.inter.IProduct;
import com.leventsclone.leventsclone.unstil.ConvertToEntity;
import com.leventsclone.leventsclone.unstil.ConvertToOtherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;

@Service
public class ProductSer implements IProduct {
    private final IProductRepo PRODUCT_REPO;
    private final  TypeProductSer typeProductSer;
    private final ConvertToEntity convertToEntity = new ConvertToEntity();
    private final ConvertToOtherData convertToOtherData = new ConvertToOtherData();

    @Autowired
    public ProductSer(IProductRepo productRepo, TypeProductSer typeProductSer) {
        PRODUCT_REPO = productRepo;
        this.typeProductSer = typeProductSer;
    }
    @Override
    public ProductUse getByName(String name) {
        return convertToOtherData.getProductUse(PRODUCT_REPO.findByName(name).orElseThrow());
    }

    @Override
    public List<ProductUse> getAllUseEvent() {
        Predicate<Product> streamsPredicate = item -> item.getEvent() == null;
        List<Product>  listProduct = PRODUCT_REPO.findAll().stream()
                .filter(streamsPredicate)
                .toList();

        List<ProductUse> productUses = new ArrayList<>();
        listProduct.forEach((e) -> {
            productUses.add(convertToOtherData.getProductUse(e));
        });
        return productUses;
    }

    @Override
    public List<Product> getAllByListEventProduct(List<EventProduct> eventProducts) {
        List<Product> products = PRODUCT_REPO.findAll();
        Predicate<Product> predicate = product ->  {
            Predicate<EventProduct> predicate1 = eventProduct -> Objects.equals(product.getEvent().getIdEventProduct(), eventProduct.getIdEventProduct());
            List<EventProduct> eventProducts1 = eventProducts.stream().filter(predicate1).toList();
            return  !eventProducts1.isEmpty();
        };
        return products.stream().filter(predicate).toList();
    }

    @Override
    public List<ProductUse> getAllUsingEvent(Long idEvent) {
        Predicate<Product> streamsPredicateInit = item -> item.getEvent()  != null;
        Predicate<Product> streamsPredicate = item -> Objects.equals(item.getEvent().getEventEventProduct().getIdEvent(), idEvent);
       List<Product>  listProduct = PRODUCT_REPO.findAll().stream()
                .filter(streamsPredicateInit)
                .toList();

        List<Product>  listProductSet = listProduct.stream()
                .filter(streamsPredicate)
                .toList();

        List<ProductUse> productUses = new ArrayList<>();
        listProductSet.forEach((e) -> {
            productUses.add(convertToOtherData.getProductUse(e));
        });
        return productUses;
    }

    @Override
    public Optional<Product> getByNameR(String name) {
        return PRODUCT_REPO.findByName(name);
    }

    @Override
    public ProductUse getById(long id) {
        return convertToOtherData.getProductUse(PRODUCT_REPO.findById(id).orElseThrow());
    }

    @Override
    public Product getByIdEt(long id) {
        return PRODUCT_REPO.findById(id).orElseThrow();
    }

    @Override
    public void delete(Long id) {
        PRODUCT_REPO.deleteById(id);
    }

    @Override
    public boolean save(ProductUse productUse) {
        Product product = convertToEntity.getProduct(productUse);
        TypeProduct typeProduct = typeProductSer.getByIdEtt(productUse.getTypeProductUse().getId());
        product.setTypeProduct(typeProduct);
        PRODUCT_REPO.save(product);
        return true;
    }

    @Override
    public Product save(Product product) {
       return  PRODUCT_REPO.save(product);
    }

    @Override
    public boolean update(ProductUse productUse) {
        Product product = convertToEntity.getProduct(productUse);
        TypeProduct typeProduct = typeProductSer.getByIdEtt(productUse.getTypeProductUse().getId());
        product.setTypeProduct(typeProduct);
        PRODUCT_REPO.save(product);
        return true;
    }

    private void sort(List<ProductUse> productUseList) {
        Comparator<ProductUse> useComparator = new Comparator<ProductUse>() {
            @Override
            public int compare(ProductUse o1, ProductUse o2) {
                return Integer.compare(Math.toIntExact(o2.getId()), Math.toIntExact(o1.getId()));
            }
        };
        productUseList.sort(useComparator);
    }



    @Override
    public List<ProductUse> getAll() {
        List<ProductUse> dataResult = new ArrayList<>();
        PRODUCT_REPO.findAll().forEach((e) -> {
            ProductUse productUse = convertToOtherData.getProductUse(e);
            if( !e.getListCollectionProduct().isEmpty() || !e.getOptions().isEmpty()) {
                productUse.setUsing(true);
            }
            dataResult.add(productUse);
        });
        sort(dataResult);
        return dataResult;
    }
}
