package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.request.ProductAddReq;
import com.leventsclone.leventsclone.entity.Event;
import com.leventsclone.leventsclone.entity.EventProduct;

import java.util.List;

public interface IEventProduct {

    void save(List<ProductAddReq> list, Long idEvent);

    void delete(List<ProductAddReq> list, Long idEvent);

    void update(List<ProductAddReq> list, Long idEvent);

    List<EventProduct> findByEvent(Event event);
}
