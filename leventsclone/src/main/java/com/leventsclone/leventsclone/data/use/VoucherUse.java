package com.leventsclone.leventsclone.data.use;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoucherUse {
    private Long id;
    private String code;
    private String name;
    private double priceCondition;
    private double price;
    private double percent;
    private double priceSubtract;
    private String type;
    private String state;
}
