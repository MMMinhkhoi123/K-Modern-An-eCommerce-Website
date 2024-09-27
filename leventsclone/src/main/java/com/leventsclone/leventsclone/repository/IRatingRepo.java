package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.Option;
import com.leventsclone.leventsclone.entity.Orders;
import com.leventsclone.leventsclone.entity.Rating;
import com.leventsclone.leventsclone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IRatingRepo extends JpaRepository<Rating, Long> {
     List<Rating> findAllByOption(Option option);

     List<Rating> findAllByOptionAndUser(Option option, User user);

     List<Rating> findAllByOrders(Orders orders);

     Optional<Rating> findByOrdersAndOptionAndUser(Orders orders, Option option, User user);
}
