package com.leventsclone.leventsclone.data.use;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticatedUse {
    private String email;
    private String fullName;
    private String phone;
    private String role;
    private int grade;
    private String date;
    private String gender;
    private List<VoucherUse> voucherUses;
    private MemberUse memberUse;
    private MemberUse memberUseUp;
    private List<AddressUse> addressUses;
    private List<OptionUse> wishlist;
}
