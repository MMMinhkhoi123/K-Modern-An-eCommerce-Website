package com.leventsclone.leventsclone.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InfoOrderDto {

    private String phone;

    private String lastName;

    private String firstName;

    private String address;

    private String suit;

    private String phoneExtra;

    private String typeTransport;

    private String typePayment;

    private boolean packaging;

    private String contentPackaging;
}
