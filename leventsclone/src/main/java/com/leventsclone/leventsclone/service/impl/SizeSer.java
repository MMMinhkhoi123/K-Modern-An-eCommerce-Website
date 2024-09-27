package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.data.use.*;
import com.leventsclone.leventsclone.entity.Size;
import com.leventsclone.leventsclone.entity.TypeSize;
import com.leventsclone.leventsclone.repository.ISizeRepo;
import com.leventsclone.leventsclone.repository.ITypeSizeRepo;
import com.leventsclone.leventsclone.service.inter.ISize;
import com.leventsclone.leventsclone.unstil.ConvertToOtherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SizeSer implements ISize {
    private final  ISizeRepo SIZE_REPO;
    private final ITypeSizeRepo TYPE_SIZE_REPO;
    private  final ConvertToOtherData convertToOtherData = new ConvertToOtherData();

    private  boolean status;

    @Autowired
    public  SizeSer(ISizeRepo sizeRepo, ITypeSizeRepo iTypeSizeRepo) {
        this.SIZE_REPO = sizeRepo;
        this.TYPE_SIZE_REPO = iTypeSizeRepo;
    }

    @Override
    public SizeUse getById(Long id) {
        Size size = SIZE_REPO.findById(id).orElseThrow();
        SizeUse sizeUse = convertToOtherData.getSizeUSe(size);
        sizeUse.setId(id);
        return sizeUse;
    }

    @Override
    public Size getByIdET(Long id) {
        return SIZE_REPO.findById(id).orElseThrow();
    }

    @Override
    public void delete(Long id) {
        SIZE_REPO.deleteById(id);
    }

    @Override
    public void save(SizeUse sizeUse) {
        Size size = new Size();
        size.setNameSize(sizeUse.getName());
        size.setTypeSize(TYPE_SIZE_REPO.findById(sizeUse.getIdType()).orElseThrow());
        SIZE_REPO.save(size);
    }

    @Override
    public void update(SizeUse sizeUse) {
        Size size = new Size();
        size.setIdSize(sizeUse.getId());
        size.setNameSize(sizeUse.getName());
        size.setTypeSize(TYPE_SIZE_REPO.findById(sizeUse.getIdType()).orElseThrow());
        SIZE_REPO.save(size);
    }

    @Override
    public List<SizeUse> getSizeFilter(List<OptionUse> data) {
        List<SizeUse> sizeUses = new ArrayList<>();
        data.forEach(optionUse -> {
            optionUse.getOptionSizeUSes().forEach(optionSizeUSe ->
            {
                if(checkSize(sizeUses, optionSizeUSe.getSizeUse())) {
                    sizeUses.add(optionSizeUSe.getSizeUse());
                }
            });

        });
        return sizeUses;
     }

    @Override
    public void updateSize(List<SizeUse> data, Long idProduct, Long idColor) {

    }


    @Override
    public boolean checkSize(List<SizeUse> sizeUses, SizeUse sizeUse) {
        status = true;
        sizeUses.forEach((e) -> {
            if (e.getName().equals(sizeUse.getName())) {
                status = false;
            }
        });
        return status;
    }

    @Override
    public SizeUse getByName(String name) {
        return  convertToOtherData.getSizeUSe(SIZE_REPO.getByName(name));
    }



    public void sort(List<SizeUse> sizeUses) {
        Comparator<SizeUse> sizeUseComparator = new Comparator<SizeUse>() {
            @Override
            public int compare(SizeUse o1, SizeUse o2) {
                return Integer.compare((int) o2.getId(), (int) o1.getId());
            }
        };
        sizeUses.sort(sizeUseComparator);
    }

    @Override
    public List<SizeUse> getAll() {
        List<SizeUse> list = new ArrayList<>();
        SIZE_REPO.findAll().forEach((e) -> {
            SizeUse sizeUse = new SizeUse();
            sizeUse.setId(e.getIdSize());
            sizeUse.setName(e.getNameSize());
            sizeUse.setType(e.getTypeSize().getNameTypeSize());
            list.add(sizeUse);
        });
        sort(list);
        return list;
    }

    @Override
    public List<SizeUse> getByNameTypeSize(String name) {
        System.out.println(name);
        TypeSize typeSize  = TYPE_SIZE_REPO.findByNameTypeSize(name);
        List<SizeUse> list = new ArrayList<>();
        SIZE_REPO.findAllByTypeSize(typeSize).orElseThrow().forEach((e) -> {
            list.add(convertToOtherData.getSizeUSe(e));
        });
        return list;
    }

    @Override
    public List<SizeUse> getByIdTypeSize(Long id) {
        TypeSize typeSize  = TYPE_SIZE_REPO.findById(id).orElseThrow();
        List<SizeUse> list = new ArrayList<>();
        SIZE_REPO.findAllByTypeSize(typeSize).orElseThrow().forEach((e) -> {
            list.add(convertToOtherData.getSizeUSe(e));
        });
        return list;
    }

    @Override
    public List<TypeSizeUse> getAllTypeSize() {
        List<TypeSizeUse> typeSizeUses = new ArrayList<>();
        TYPE_SIZE_REPO.findAll().forEach((e) -> {
            TypeSizeUse typeSizeUse = new TypeSizeUse();
            typeSizeUse.setId(e.getIdTypeSize());
            typeSizeUse.setName(e.getNameTypeSize());
            typeSizeUses.add(typeSizeUse);
        });
        return typeSizeUses;
    }
}
