package com.leventsclone.leventsclone.data.use;

import lombok.Data;

@Data
public class AddressUse {

    private Long id;

    private String lastName;

    private String firstName;

    private String phone;

    private String city;

    private String district;

    private String commune;

    private String detail;

    private boolean usingAddress;
}
