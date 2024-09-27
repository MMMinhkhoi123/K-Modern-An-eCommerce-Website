package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.User;
import com.leventsclone.leventsclone.entity.UserVoucher;
import com.leventsclone.leventsclone.entity.Voucher;
import com.leventsclone.leventsclone.entity.relationship.RelationShipUserVoucher;
import com.leventsclone.leventsclone.service.impl.VoucherSer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IUserVoucherRepo extends JpaRepository<UserVoucher, RelationShipUserVoucher> {
    public Optional<UserVoucher> findByUserAndVoucher(User user, Voucher voucher);
    @Modifying
    @Transactional
    @Query(value = "update user_voucher set quantity = quantity - 1 where id_user=:idUSer and id_voucher = :idVoucher", nativeQuery = true)
    void  updateByIdVoucherUser(@Param("idVoucher") Long idVoucher, @Param("idUSer") Long idUser);

    @Modifying
    @Transactional
    @Query(value = "delete from user_voucher where id_user=:idUSer and id_voucher = :idVoucher", nativeQuery = true)
    void deleteByIdVoucherUser(@Param("idVoucher") Long idVoucher, @Param("idUSer") Long idUser);
}
