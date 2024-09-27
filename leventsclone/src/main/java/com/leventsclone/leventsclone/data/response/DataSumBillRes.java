package com.leventsclone.leventsclone.data.response;

import lombok.Data;

@Data
public class DataSumBillRes {
    private int feeVoucher;
    private int feeProducts;
    private int feeShip;
    private int feeTotal;
}
