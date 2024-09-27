package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.data.request.ProductAddReq;
import com.leventsclone.leventsclone.entity.Event;
import com.leventsclone.leventsclone.entity.EventProduct;
import com.leventsclone.leventsclone.entity.Product;
import com.leventsclone.leventsclone.repository.IEventProductRepo;
import com.leventsclone.leventsclone.service.inter.IEventProduct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventProductSer implements IEventProduct {
    private final IEventProductRepo EVENT_PRODUCT_REPO;
    private final ProductSer productSer;
    private final EventSer eventSer;

    public EventProductSer(IEventProductRepo EVENT_PRODUCT, EventSer eventSer, ProductSer productSer) {
        this.EVENT_PRODUCT_REPO = EVENT_PRODUCT;
        this.eventSer = eventSer;
        this.productSer = productSer;
    }


    @Override
    public void save(List<ProductAddReq> list, Long idEvent) {
        Event event = eventSer.getByIdF(idEvent);
        list.forEach((e) -> {
            EventProduct product = new EventProduct();
            Product product1 = productSer.getByIdEt(e.getIdProduct());
            product.setEventEventProduct(event);
            product.setPercentDiscount(e.getPercent());
            EventProduct eventProduct = EVENT_PRODUCT_REPO.save(product);
            product1.setEvent(eventProduct);
            eventProduct.setProduct(productSer.save(product1));
            EVENT_PRODUCT_REPO.save(eventProduct);
        });
    }

    @Override
    public void delete(List<ProductAddReq> list, Long idEvent) {
        Event event = eventSer.getByIdF(idEvent);
        list.forEach((e) -> {
            Product product = productSer.getByIdEt(e.getIdProduct());
            EventProduct eventProduct =  EVENT_PRODUCT_REPO.findByProductAndEventEventProduct(product, event);
            product.setEvent(null);
            productSer.save(product);
            EVENT_PRODUCT_REPO.deleteById(eventProduct.getIdEventProduct());
        });
    }

    @Override
    public void update(List<ProductAddReq> list, Long idEvent) {
        Event event = eventSer.getByIdF(idEvent);
        list.forEach((e) -> {
            Product product = productSer.getByIdEt(e.getIdProduct());
            EventProduct eventProduct =  EVENT_PRODUCT_REPO.findByProductAndEventEventProduct(product, event);
            eventProduct.setPercentDiscount(e.getPercent());
            EVENT_PRODUCT_REPO.save(eventProduct);
        });
    }

    @Override
    public List<EventProduct> findByEvent(Event event) {
        return EVENT_PRODUCT_REPO.findAllByEventEventProduct(event);
    }
}
