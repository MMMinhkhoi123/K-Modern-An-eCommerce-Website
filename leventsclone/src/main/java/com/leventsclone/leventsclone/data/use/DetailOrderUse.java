package com.leventsclone.leventsclone.data.use;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailOrderUse {
    private String phone;
    private String firstName;
    private String lastName;
    private String address;
    private String typeTrans;
    private String typePay;
    private Integer sumPay;
}
