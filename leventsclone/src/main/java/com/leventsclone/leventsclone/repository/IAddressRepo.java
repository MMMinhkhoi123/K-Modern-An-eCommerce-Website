package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.Address;
import com.leventsclone.leventsclone.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IAddressRepo extends JpaRepository<Address, Long> {
    Optional<List<Address>> findAllByUserAddress(User user);
}
