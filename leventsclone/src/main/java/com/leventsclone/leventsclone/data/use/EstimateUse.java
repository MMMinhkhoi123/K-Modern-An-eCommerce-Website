package com.leventsclone.leventsclone.data.use;
import lombok.Data;

@Data
public class EstimateUse {
    private Long key;
    private int value;
    private double percent;
    private boolean increase;
}
