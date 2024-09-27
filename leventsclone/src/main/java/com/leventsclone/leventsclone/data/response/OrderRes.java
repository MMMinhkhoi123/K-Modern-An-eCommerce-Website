package com.leventsclone.leventsclone.data.response;

import lombok.Data;

@Data
public class OrderRes {
    private String code;
    private Long date;
    private String name;
    private String state;
    private int countState;
    private String imgPay;
    private String imgRefund;
    private String typePay;
    private Integer sumPrice;
    private String emailNotify;
}
