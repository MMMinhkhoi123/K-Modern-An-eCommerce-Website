package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface IUserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmailUser(String email);
    Optional<User> findByKeyForGetPassUser(String key);


    Optional<User> findByExpireForGetPassUserAndKeyForGetPassUser(Date date, String code);
    Optional<User> findByPhoneUser(@Param(value = "phone") String phone);
}
