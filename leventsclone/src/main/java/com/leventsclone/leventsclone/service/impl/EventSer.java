package com.leventsclone.leventsclone.service.impl;
import com.leventsclone.leventsclone.data.use.EventUse;
import com.leventsclone.leventsclone.data.use.ProductUse;
import com.leventsclone.leventsclone.entity.Event;
import com.leventsclone.leventsclone.repository.IEventRepo;
import com.leventsclone.leventsclone.service.inter.IEvent;
import com.leventsclone.leventsclone.unstil.ConvertToOtherData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class EventSer implements IEvent {
    private final IEventRepo EVENT_REPO;
    private final DirectorySer directorySer;

    private  final ProductSer productSer;
    private final ConvertToOtherData convertToOtherData = new ConvertToOtherData();
    public EventSer(IEventRepo iEventRepo, DirectorySer DirectorySer, ProductSer productSer) {
        EVENT_REPO = iEventRepo;
        this.directorySer = DirectorySer;
        this.productSer = productSer;
    }

    @Override
    public List<EventUse> getAll() {
        List<EventUse>eventUses = new ArrayList<>();
        EVENT_REPO.findAll().forEach((e) -> {
            eventUses.add(convertToOtherData.getEventUse(e));
        });
        return eventUses;
    }

    @Override
    public void save(EventUse eventUse) {
        Event event = convertToOtherData.getEvent(eventUse);
        EVENT_REPO.save(event);
    }

    @Override
    public Optional<Event> getByPath(String path) {
        return EVENT_REPO.findByPathEvent(path);
    }


    @Override
    public boolean checkPath(String path) {
        AtomicBoolean state = new AtomicBoolean(true);
        directorySer.getAll().forEach((e) -> {
            if(e.getKey() != null) {
                if(e.getKey().equals(path)) {
                    state.set(false);
                }
            } else  {
                e.getTypeProductUses().forEach((x) -> {
                    if(Objects.equals(x.getKey(), path)) {
                        state.set(false);
                    }
                });
            }
        });
        if(state.get()) {
            getAll().forEach((e) -> {
                if(e.getPath() == path) {
                    state.set(false);
                }
            });
        }
        return state.get();
    }

    @Override
    public List<ProductUse> getAllProductById(Long id) {
        List<ProductUse> productUses = new ArrayList<>();
        Event event = EVENT_REPO.findById(id).orElseThrow();
        event.getEventProduct().forEach((e) -> {
            ProductUse productUse = productSer.getById(e.getProduct().getIdProduct());
            productUse.setPercent(e.getPercentDiscount());
            productUses.add(productUse);
        });
        return productUses;
    }

    @Override
    public EventUse getById(Long id) {
       return convertToOtherData.getEventUse(EVENT_REPO.findById(id).orElseThrow());
    }

    @Override
    public Event getByIdF(Long id) {
        return EVENT_REPO.findById(id).orElseThrow();
    }

    @Override
    public void delete(List<Long> listId) {
        listId.forEach(EVENT_REPO::deleteById);
    }
}
