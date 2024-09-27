package com.leventsclone.leventsclone.service.inter;


import com.leventsclone.leventsclone.entity.User;
import com.leventsclone.leventsclone.entity.UserWishList;

import java.util.List;

public interface IUserWishlist {
    void addWishlist(User user, Long idOption);
    void deleteWishlist(User user, Long idOption);

    List<UserWishList> getByUser(User  user);
    Boolean checkProductContain(User user, Long idOption);
}
