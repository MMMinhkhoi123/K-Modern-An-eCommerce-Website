package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.request.ProductAddReq;
import com.leventsclone.leventsclone.data.use.EventUse;
import com.leventsclone.leventsclone.data.use.ProductUse;
import com.leventsclone.leventsclone.entity.Event;

import java.util.List;
import java.util.Optional;

public interface IEvent {
    List<EventUse> getAll();
    void save(EventUse eventUse);

   Optional<Event> getByPath(String path);
    boolean checkPath(String path);

    List<ProductUse> getAllProductById(Long id);
    EventUse getById(Long id);
    Event getByIdF(Long id);

    void  delete(List<Long> listId);
}
