package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.data.use.OutfitUse;
import com.leventsclone.leventsclone.entity.Color;
import com.leventsclone.leventsclone.entity.Outfit;
import com.leventsclone.leventsclone.entity.Product;
import com.leventsclone.leventsclone.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IOutfitRepo extends JpaRepository<Outfit, Long> {
}
