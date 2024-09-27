package com.leventsclone.leventsclone.data.response;

import lombok.Data;

@Data
public class MemberRes {
    private String nameMember;
    private int grade;
    private int gradePrizes;
    private int believe;
    private  MemberRes memberUp;
}
