package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.data.use.MemberUse;
import com.leventsclone.leventsclone.data.use.VoucherUse;
import com.leventsclone.leventsclone.entity.Voucher;
import com.leventsclone.leventsclone.repository.IVoucherRepo;
import com.leventsclone.leventsclone.service.inter.IVoucher;
import com.leventsclone.leventsclone.unstil.ConvertToOtherData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class VoucherSer implements IVoucher {
    private final IVoucherRepo VOUCHER_REPO;
    private final ConvertToOtherData convertToOtherData = new ConvertToOtherData();

    public  VoucherSer(IVoucherRepo iVoucherRepo) {
        this.VOUCHER_REPO = iVoucherRepo;
    }


    private void sort(List<VoucherUse> colorUses) {
        Comparator<VoucherUse> resComparator = new Comparator<VoucherUse>() {
            @Override
            public int compare(VoucherUse o1, VoucherUse o2) {
                return Double.compare(o2.getId() , o1.getId());
            }
        };
        colorUses.sort(resComparator);
    }

    @Override
    public List<VoucherUse> getAll() {
        List<VoucherUse> list = new ArrayList<>();
        VOUCHER_REPO.findAll().forEach((e) -> {
            list.add(convertToOtherData.getVoucherUse(e));
        });
        sort(list);
        return list;
    }

    @Override
    public boolean checkByCode(String code) {
        return VOUCHER_REPO.findByCodeVoucher(code).isPresent();
    }

    @Override
    public void save(VoucherUse voucherUse) {
        Voucher voucher = new Voucher();
        voucher.setDescribeVoucher(voucherUse.getName());
        voucher.setCodeVoucher(voucherUse.getCode());
        voucher.setTypeVoucher(voucherUse.getType());
        voucher.setPriceConditionVoucher(voucherUse.getPriceCondition());
        voucher.setPercentDiscountVoucher(voucherUse.getPercent());
        voucher.setPriceDiscountVoucher(voucherUse.getPrice());
        VOUCHER_REPO.save(voucher);
    }

    @Override
    public void update(VoucherUse voucherUse) {
        Voucher voucher = VOUCHER_REPO.findById(voucherUse.getId()).orElseThrow();
        voucher.setDescribeVoucher(voucherUse.getName());
        voucher.setCodeVoucher(voucherUse.getCode());
        voucher.setTypeVoucher(voucherUse.getType());
        voucher.setPriceConditionVoucher(voucherUse.getPriceCondition());
        voucher.setPercentDiscountVoucher(voucherUse.getPercent());
        voucher.setPriceDiscountVoucher(voucherUse.getPrice());
        VOUCHER_REPO.save(voucher);
    }

    @Override
    public Voucher getByIdR(Long id) {
        return VOUCHER_REPO.findById(id).orElseThrow();
    }

    @Override
    public void delete(List<Long> ids) {
        ids.forEach(VOUCHER_REPO::deleteById);
    }

    @Override
    public VoucherUse getById(Long id) {
        return convertToOtherData.getVoucherUse(VOUCHER_REPO.findById(id).orElseThrow());
    }

    @Override
    public VoucherUse getByCodeR(String code) {
        return convertToOtherData.getVoucherUse(VOUCHER_REPO.findByCodeVoucher(code).orElseThrow());
    }

    @Override
    public Voucher getVoucherLogin() {
        String key = "NEWMEMBER50K";
        return VOUCHER_REPO.findByCodeVoucher(key).orElseThrow();
    }

}
