package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.entity.User;
import com.leventsclone.leventsclone.entity.UserVoucher;
import com.leventsclone.leventsclone.entity.Voucher;
import com.leventsclone.leventsclone.entity.relationship.RelationShipUserVoucher;
import com.leventsclone.leventsclone.repository.IUserVoucherRepo;
import com.leventsclone.leventsclone.service.inter.IUserVoucher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserVoucherSer implements IUserVoucher {
    private final IUserVoucherRepo USER_VOUCHER_REPO;
    private final  VoucherSer voucherSer;

    public UserVoucherSer(IUserVoucherRepo userVoucher, VoucherSer voucher){
        USER_VOUCHER_REPO = userVoucher;
        voucherSer = voucher;
    }


    @Override
    public void addVoucherLogin(User user) {
        UserVoucher userVoucher = new UserVoucher();
        userVoucher.setUser(user);
        userVoucher.setVoucher(voucherSer.getVoucherLogin());
        userVoucher.setQuantity(1);
        USER_VOUCHER_REPO.save(userVoucher);
    }

    @Override
    public boolean getByUserAndVoucher(User user, Voucher voucher) {
        return  USER_VOUCHER_REPO.findByUserAndVoucher(user, voucher).isPresent();
    }

    @Override
    public void addVoucher(User user, Voucher voucher) {
        Optional<UserVoucher> userVoucher = USER_VOUCHER_REPO.findByUserAndVoucher(user, voucher);
        if(userVoucher.isPresent()) {
            int quantity = userVoucher.get().getQuantity() + 1;
            userVoucher.get().setQuantity(quantity);
            USER_VOUCHER_REPO.save(userVoucher.orElseThrow());
        } else  {
        UserVoucher voucher1  = new UserVoucher();
            voucher1.setQuantity(1);
            voucher1.setUser(user);
            voucher1.setVoucher(voucher);
            USER_VOUCHER_REPO.save(voucher1);
        }

    }

    @Override
    public void updateAfterUseByUserAndVoucher(User user, Voucher voucher) {
        UserVoucher userVoucher = USER_VOUCHER_REPO.findByUserAndVoucher(user, voucher).orElseThrow();
        if (userVoucher.getQuantity() - 1 <= 0) {
            USER_VOUCHER_REPO.deleteByIdVoucherUser(voucher.getIdVoucher(), user.getIdUser());
        } else {
            USER_VOUCHER_REPO.updateByIdVoucherUser(voucher.getIdVoucher(), user.getIdUser());
        }
    }
}
