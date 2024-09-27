package com.leventsclone.leventsclone.data.use;

import lombok.Data;

@Data
public class EstimateMonthlyUse {
    private int month;
    private double percent;
    private int value;
}
