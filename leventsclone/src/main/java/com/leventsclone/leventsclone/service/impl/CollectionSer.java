package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.data.response.CollectionRes;
import com.leventsclone.leventsclone.data.use.ClouUse;
import com.leventsclone.leventsclone.data.use.CollectionUse;
import com.leventsclone.leventsclone.data.use.OptionUse;
import com.leventsclone.leventsclone.data.use.ProductSizeColorUSe;
import com.leventsclone.leventsclone.entity.Collection;
import com.leventsclone.leventsclone.entity.Product;
import com.leventsclone.leventsclone.repository.ICollectionRepo;
import com.leventsclone.leventsclone.service.inter.ICollection;
import com.leventsclone.leventsclone.unstil.ConvertToOtherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CollectionSer implements ICollection {
    private final ICollectionRepo COLLECT_REPO;
    private final ConvertToOtherData convertToOtherData = new ConvertToOtherData();
    private final CloudinarySer cloudinarySer;
    private final ProductSer productSer;

    private final OptionSer optionSer;


    @Autowired
    public CollectionSer(OptionSer optionSer ,ProductSer productSer,ICollectionRepo roRepo, CloudinarySer cloudinarySer) {
        this.COLLECT_REPO = roRepo;
        this.optionSer = optionSer;
        this.productSer = productSer;
        this.cloudinarySer = cloudinarySer;
    }

    @Override
    public List<CollectionUse> getAll() {
        List<CollectionUse>  dataResult = new ArrayList<>();
        COLLECT_REPO.findAll().forEach((item) -> {
            dataResult.add(convertToOtherData.getCollectionUse(item));
        });
        return dataResult;
    }

    @Override
    public CollectionUse getById(Long id) {
        Collection collection = COLLECT_REPO.findById(id).orElseThrow();
        return convertToOtherData.getCollectionUse(collection);
    }

    @Override
    public List<CollectionUse> getALLSwiper(int number) {
        AtomicInteger index = new AtomicInteger();
        List<CollectionUse>  dataResult = new ArrayList<>();
        getAll().forEach((e) -> {
            if(index.get() < number) {
                dataResult.add(e);
            }
            index.getAndIncrement();
        });
        return dataResult;
    }

    @Override
    public void update(String id, String name, Optional<MultipartFile> multipartFil, boolean dark, List<String> Products, String content) {
        Collection collectionUse = COLLECT_REPO.findById(Long.valueOf(id)).orElseThrow();
        if(multipartFil.isPresent()) {
            List<String> ListId = new ArrayList<>();
            ListId.add(collectionUse.getKeyCollection());
            cloudinarySer.delete(ListId);
           ClouUse clouUse =  cloudinarySer.uploadFile(multipartFil.orElseThrow());
           collectionUse.setKeyCollection(clouUse.getKey());
           collectionUse.setImageCollection(clouUse.getUrl());
        }
        collectionUse.setNameCollection(name);
        collectionUse.setDarkColorCollection(dark);
        List<Product> products = new ArrayList<>();
        Products.forEach((e) -> {
            products.add(productSer.getByIdEt(Long.parseLong(e)));
        });
        collectionUse.setProducts(products);
        collectionUse.setDescribeCollection(content);
        COLLECT_REPO.save(collectionUse);
    }


    @Override
    public void save(String name, MultipartFile multipartFile, boolean dark, List<String> Products, String content) {
        ClouUse clouUse = cloudinarySer.uploadFile(multipartFile);
        Collection collection = new Collection();
        collection.setNameCollection(name.trim());
        collection.setDescribeCollection(content);
        collection.setImageCollection(clouUse.getUrl());
        collection.setKeyCollection(clouUse.getKey());
        List<Product> products = new ArrayList<>();
        collection.setDarkColorCollection(dark);
        Products.forEach((e) -> {
            products.add(productSer.getByIdEt(Long.parseLong(e)));
        });
        collection.setProducts(products);
        COLLECT_REPO.save(collection);
    }

    @Override
    public void delete(List<String> list) {
        List<String> list1 = new ArrayList<>();
        list.forEach((e) -> {
            Collection collection = COLLECT_REPO.findById( Long.parseLong(e)).orElseThrow();
            collection.setProducts(new ArrayList<>());
            list1.add(collection.getKeyCollection());
            COLLECT_REPO.save(collection);
            COLLECT_REPO.deleteById( Long.parseLong(e));
        });
        cloudinarySer.delete(list1);
    }

    @Override
    public boolean checkContainCollection(String url) {
        AtomicBoolean state = new AtomicBoolean(false);
        List<Collection> data =  COLLECT_REPO.findAll();
        data.forEach((item) -> {
            String nameLV1 = item.getNameCollection().replaceAll(" ", "-");
            String nameLv2 = nameLV1.replaceAll("/", "-");
            if(nameLv2.equalsIgnoreCase(url)) {
                state.set(true);
            }
        });
        return state.get();
    }

    @Override
    public CollectionRes getAdequate(String nameCollection) {
        CollectionRes dataResult = new CollectionRes();
        AtomicReference<Collection> collection = new AtomicReference<>(new Collection());
        List<Collection> data = COLLECT_REPO.findAll();
        data.forEach((item) -> {
            String nameLV1 = item.getNameCollection().replaceAll(" ", "-");
            String nameLv2 = nameLV1.replaceAll("/", "-");
            if(nameLv2.equalsIgnoreCase(nameCollection)) {
                collection.set(item);
            }
        });
        CollectionUse collectionUse = convertToOtherData.getCollectionUse(collection.get());

        List<ProductSizeColorUSe> productSizeColorUSeList = new ArrayList<>();
        List<OptionUse> optionUses = new LinkedList<>();

        collection.get().getProducts().forEach((e) -> {
            optionUses.addAll(optionSer.getAllByProduct(e));
        });

        dataResult.setOptionUses(optionUses);
        dataResult.setCollectionUse(collectionUse);
        return dataResult;
    }

    @Override
    public CollectionUse getByKey(String key) {
        String keyGet = key.replaceAll("-", " ").trim();
        return convertToOtherData.getCollectionUse(COLLECT_REPO.findByNameCollection(keyGet).orElseThrow());
    }
}
