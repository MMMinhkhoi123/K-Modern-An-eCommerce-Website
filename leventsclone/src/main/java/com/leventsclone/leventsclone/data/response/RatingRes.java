package com.leventsclone.leventsclone.data.response;

import com.leventsclone.leventsclone.data.use.RatingUse;
import lombok.Data;

import java.util.List;

@Data
public class RatingRes {
    private int thisPage;
    private List<RatingUse> list;
    private int countPag;
    private int sumCountStar;
    private int numberAssess;
    private int countAll;
    private int countOne;
    private int countTwo;
    private int countTrue;
    private int countFour;
    private int countFive;
}
