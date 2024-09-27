package com.leventsclone.leventsclone.data.use;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberUse {
    private Long id;
    private String name;
    private double grade;
    private  int price;
    private int percent;
    private boolean isUsing;
    private int priceMemberUp;
    private int gradeMemberUp;
}
