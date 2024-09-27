package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.response.DirectoryRes;
import com.leventsclone.leventsclone.entity.Directory;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IDirectory {
     List<DirectoryRes> getAll();
     void save(DirectoryRes directoryRes);
     void delete(Long id);
     void update(DirectoryRes directoryRes);
     Optional<Directory> findByName(String name);
     DirectoryRes getById(Long id);
     Directory getByIdDr(Long id);

     Optional<Directory> getByCode(String url);

     void deleteById(Long id);
}
