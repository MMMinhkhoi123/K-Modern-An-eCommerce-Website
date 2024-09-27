package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.entity.User;
import com.leventsclone.leventsclone.entity.Voucher;

public interface IUserVoucher {
    void addVoucherLogin(User user);

    boolean getByUserAndVoucher(User user, Voucher voucher);

    void addVoucher(User user, Voucher voucher);

    void  updateAfterUseByUserAndVoucher(User user, Voucher voucher);
}
