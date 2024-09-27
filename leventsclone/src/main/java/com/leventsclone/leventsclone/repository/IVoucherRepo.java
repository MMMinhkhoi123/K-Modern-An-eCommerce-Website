package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IVoucherRepo extends JpaRepository<Voucher, Long> {
    public Optional<Voucher> findByCodeVoucher(String code);
}
