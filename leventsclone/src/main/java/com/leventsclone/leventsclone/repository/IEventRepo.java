package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IEventRepo extends JpaRepository<Event, Long> {
    public Optional<Event> findByPathEvent(String path);
}
