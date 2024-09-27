package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.entity.*;
import com.leventsclone.leventsclone.repository.IUserWishlistRepo;
import com.leventsclone.leventsclone.service.inter.IUserWishlist;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserWishlistSer implements IUserWishlist {

    private final IUserWishlistRepo USER_WISHLIST_REPO;
    private final OptionSer optionSer;

    public UserWishlistSer(IUserWishlistRepo iUserWishlistRepo, OptionSer optionSer) {
        this.optionSer = optionSer;
        USER_WISHLIST_REPO = iUserWishlistRepo;
    }
    @Override
    public void addWishlist(User user, Long idOption) {
        Option option = optionSer.getEnById(idOption);
        UserWishList list = new UserWishList();
        list.setUser(user);
        list.setOption(option);
        USER_WISHLIST_REPO.save(list);
    }

    @Override
    public void deleteWishlist(User user, Long idOption) {
        Option option = optionSer.getEnById(idOption);
        Optional<UserWishList> userWishList = USER_WISHLIST_REPO.findByUserAndOption(user, option);
        if(userWishList.isPresent()) {
         USER_WISHLIST_REPO.deleteById(userWishList.orElseThrow().getId());
        }
    }

    @Override
    public List<UserWishList> getByUser(User user) {
        return USER_WISHLIST_REPO.findAllByUser(user).orElseThrow();
    }

    @Override
    public Boolean checkProductContain(User user, Long idOption) {
        Option option = optionSer.getEnById(idOption);
        Optional<UserWishList> userWishList = USER_WISHLIST_REPO.findByUserAndOption(user, option);
        return userWishList.isPresent();
    }
}
