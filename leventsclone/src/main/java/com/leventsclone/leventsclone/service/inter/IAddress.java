package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.use.AddressUse;
import com.leventsclone.leventsclone.entity.Address;
import com.leventsclone.leventsclone.entity.User;

import java.util.List;

public interface IAddress {

    Address save(User user, AddressUse addressUse);

    void delete(Long id);


    List<AddressUse>  getAllByUser(User user);

    AddressUse getById(Long idAddress);

    AddressUse getByUserActive(User user);
}
