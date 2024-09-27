package com.leventsclone.leventsclone.data.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRes {
    private String name;
    private String address;
    private String phone;
    private String phoneExtra;
    private String codeOrder;
    private int price;
    private String state;
}
