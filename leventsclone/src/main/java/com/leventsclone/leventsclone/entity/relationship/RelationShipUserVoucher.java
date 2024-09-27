package com.leventsclone.leventsclone.entity.relationship;

import com.leventsclone.leventsclone.entity.User;
import com.leventsclone.leventsclone.entity.Voucher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RelationShipUserVoucher implements Serializable {
    private User user;
    private Voucher voucher;
}
