package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.data.response.DirectoryRes;
import com.leventsclone.leventsclone.data.use.AuthenticatedUse;
import com.leventsclone.leventsclone.entity.Directory;
import com.leventsclone.leventsclone.repository.IDirectoryRepo;
import com.leventsclone.leventsclone.service.inter.IDirectory;
import com.leventsclone.leventsclone.unstil.ConvertToOtherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class DirectorySer implements IDirectory {
    private  final IDirectoryRepo DIRECTORY_REPO;

    @Autowired
    public DirectorySer(IDirectoryRepo directoryRepo) {
        this.DIRECTORY_REPO = directoryRepo;
    }

    private void sort(List<DirectoryRes> directoryRes) {
        Comparator<DirectoryRes> directoryResComparator = new Comparator<DirectoryRes>() {
            @Override
            public int compare(DirectoryRes o1, DirectoryRes o2) {
                return Integer.compare((int) o2.getId(), (int) o1.getId());
            }
        };
        directoryRes.sort(directoryResComparator);
    }



    @Override
    public List<DirectoryRes> getAll() {
        List<DirectoryRes> dataUse = new LinkedList<>();
        ConvertToOtherData convertToOtherData = new ConvertToOtherData();
        DIRECTORY_REPO.findAll().forEach(directory -> {
            DirectoryRes directoryRes  = convertToOtherData.getDirectory(directory);
            if(!directoryRes.getTypeProductUses().isEmpty()) {
                directoryRes.setUsing(true);
            }
            dataUse.add(directoryRes);
        } );

        sort(dataUse);
        return dataUse;
    }

    @Override
    public void save(DirectoryRes directoryRes) {
        Directory directory = new Directory();
        directory.setKeyDirectory(directoryRes.getKey());
        directory.setNameDirectory(directoryRes.getName());
        DIRECTORY_REPO.save(directory);
    }

    @Override
    public void delete(Long id) {
        DIRECTORY_REPO.deleteById(id);
    }

    @Override
    public void update(DirectoryRes directoryRes) {
        Directory directory =  DIRECTORY_REPO.findById(directoryRes.getId()).orElseThrow();
        directory.setKeyDirectory(directoryRes.getKey());
        directory.setNameDirectory(directoryRes.getName());
        DIRECTORY_REPO.save(directory);
    }

    @Override
    public Optional<Directory> findByName(String name) {
        return DIRECTORY_REPO.findByNameDirectory(name);
    }

    @Override
    public DirectoryRes getById(Long id) {
       Optional<Directory> directory =  DIRECTORY_REPO.findByIdDirectory(id);
        DirectoryRes directoryRes = new DirectoryRes();
        directoryRes.setName(directory.orElseThrow().getNameDirectory());
        directoryRes.setKey(directory.orElseThrow().getKeyDirectory());
        directoryRes.setId(directory.orElseThrow().getIdDirectory());
        return  directoryRes;
    }

    @Override
    public Directory getByIdDr(Long id) {
        return DIRECTORY_REPO.findById(id).orElseThrow();
    }

    @Override
    public Optional<Directory> getByCode(String url) {
        return DIRECTORY_REPO.findByKeyDirectory(url);
    }

    @Override
    public void deleteById(Long id) {
        DIRECTORY_REPO.deleteById(id);
    }

}
