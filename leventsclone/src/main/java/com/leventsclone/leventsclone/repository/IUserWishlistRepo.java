package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.*;
import com.leventsclone.leventsclone.entity.relationship.RelationShipUserProductColor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface IUserWishlistRepo extends JpaRepository<UserWishList, Long> {

    Optional<UserWishList> findByUserAndOption(User user, Option option);

    Optional<List<UserWishList>> findAllByUser(User user);
}
