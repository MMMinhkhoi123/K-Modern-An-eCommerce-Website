package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.Orders;
import com.leventsclone.leventsclone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IOrderRepo extends JpaRepository<Orders, Long> {

    @Query(value = "select * from orders where code_order = :code", nativeQuery = true)
    Optional<Orders> findByCodeOrder(String code);

    Optional<List<Orders>> findAllByUser(User user);
}
