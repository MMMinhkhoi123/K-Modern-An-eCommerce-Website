package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.use.VoucherUse;
import com.leventsclone.leventsclone.entity.Voucher;

import java.util.List;
import java.util.Optional;

public interface IVoucher {
    public List<VoucherUse> getAll();
    public boolean checkByCode(String code);
    void save(VoucherUse voucherUse);

    void update(VoucherUse voucherUse);
    public Voucher  getByIdR(Long id);
    public void  delete(List<Long> ids);
    public VoucherUse  getById(Long id);
    public VoucherUse  getByCodeR(String code);
    public Voucher getVoucherLogin();
}
