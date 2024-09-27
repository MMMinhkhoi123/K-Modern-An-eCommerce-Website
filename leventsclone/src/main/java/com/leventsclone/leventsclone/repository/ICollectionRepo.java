package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ICollectionRepo extends JpaRepository<Collection, Long> {
    Optional<Collection> findByNameCollection(String name);
}
