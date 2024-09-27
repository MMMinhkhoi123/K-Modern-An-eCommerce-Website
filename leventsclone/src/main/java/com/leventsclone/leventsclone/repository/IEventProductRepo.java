package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.Event;
import com.leventsclone.leventsclone.entity.EventProduct;
import com.leventsclone.leventsclone.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface IEventProductRepo extends JpaRepository<EventProduct, Long> {
    EventProduct findByProductAndEventEventProduct(Product product, Event event);

    List<EventProduct> findAllByEventEventProduct(Event event);


}
