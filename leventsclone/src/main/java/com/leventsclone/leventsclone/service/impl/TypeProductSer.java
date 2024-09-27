package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.data.use.TypeProductUse;
import com.leventsclone.leventsclone.entity.Directory;
import com.leventsclone.leventsclone.entity.TypeProduct;
import com.leventsclone.leventsclone.repository.ITypeProductRepo;
import com.leventsclone.leventsclone.service.inter.ITypeProduct;
import com.leventsclone.leventsclone.unstil.ConvertToOtherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TypeProductSer implements ITypeProduct {
    private final ITypeProductRepo TYPE_PRODUCT_REPO;
    private final DirectorySer directorySer;
    private final ConvertToOtherData convertToOtherData = new ConvertToOtherData();

    @Autowired
    public  TypeProductSer(ITypeProductRepo typeProductRepo, DirectorySer directorySer) {
        this.TYPE_PRODUCT_REPO = typeProductRepo;
        this.directorySer = directorySer;
    }
    @Override
    public TypeProduct getByName(String name) {
        return TYPE_PRODUCT_REPO.findByKey(name);
    }

    @Override
    public TypeProductUse getById(Long id) {
        TypeProduct typeProduct = TYPE_PRODUCT_REPO.findById(id).orElseThrow();
        TypeProductUse typeProductUse = convertToOtherData.getTypeProductUse(typeProduct);
        typeProductUse.setIdDirectory(typeProduct.getDirectory().getIdDirectory());
        return typeProductUse;
    }

    @Override
    public TypeProduct getByIdEtt(Long id) {
        return TYPE_PRODUCT_REPO.findById(id).orElseThrow();
    }

    @Override
    public Optional<TypeProduct> getByUrl(String url) {
        return TYPE_PRODUCT_REPO.findByKeyTypeProduct(url);
    }

    @Override
    public List<TypeProductUse> getALlByIdDirectory(long idDirectory) {
        List<TypeProductUse> dataResult = new ArrayList<>();
        TYPE_PRODUCT_REPO.findByIdDirectory(idDirectory).forEach((item) -> {
            dataResult.add(convertToOtherData.getTypeProductUse(item));
        });
        return dataResult;
    }

    public void sort(List<TypeProductUse> list) {
        Comparator<TypeProductUse> type = new Comparator<TypeProductUse>() {
            @Override
            public int compare(TypeProductUse o1, TypeProductUse o2) {
                return Integer.compare((int) o2.getId(), (int) o1.getId());
            }
        };
        list.sort(type);
    }

    @Override
    public List<TypeProductUse> getAll() {
        List<TypeProductUse> dataResult = new ArrayList<>();
        TYPE_PRODUCT_REPO.findAll().forEach((item) -> {
            TypeProductUse typeProductUse = convertToOtherData.getTypeProductUse(item);
            typeProductUse.setNameDirectory(item.getDirectory().getNameDirectory());
            if(!item.getProducts().isEmpty()) {
                typeProductUse.setUsing(true);
            }
            dataResult.add(typeProductUse);
        });
        sort(dataResult);
        return dataResult;
    }

    @Override
    public void save(TypeProductUse typeProductUse) {
        TypeProduct typeProduct = new TypeProduct();
        typeProduct.setKeyTypeProduct(typeProductUse.getKey());
        typeProduct.setNameTypeProduct(typeProductUse.getName());
         Directory directory = directorySer.getByIdDr(typeProductUse.getIdDirectory());
        typeProduct.setDirectory(directory);
        TYPE_PRODUCT_REPO.save(typeProduct);
    }

    @Override
    public void update(TypeProductUse typeProductUse) {
        TypeProduct typeProduct = new TypeProduct();
        typeProduct.setIdTypeProduct(typeProductUse.getId());
        typeProduct.setKeyTypeProduct(typeProductUse.getKey());
        typeProduct.setNameTypeProduct(typeProductUse.getName());
        Directory directory = directorySer.getByIdDr(typeProductUse.getIdDirectory());
        typeProduct.setDirectory(directory);
        TYPE_PRODUCT_REPO.save(typeProduct);
    }

    @Override
    public void delete(Long id) {
        TYPE_PRODUCT_REPO.deleteById(id);
    }
}
