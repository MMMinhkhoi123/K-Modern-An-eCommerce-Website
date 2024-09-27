package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.data.use.AddressUse;
import com.leventsclone.leventsclone.entity.Address;
import com.leventsclone.leventsclone.entity.User;
import com.leventsclone.leventsclone.repository.IAddressRepo;
import com.leventsclone.leventsclone.service.inter.IAddress;
import com.leventsclone.leventsclone.unstil.ConvertToOtherData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


@Service
public class AddressSer implements IAddress {

    private final IAddressRepo ADDRESS_REPO;
    private final ConvertToOtherData convertToOtherData = new ConvertToOtherData();

    public AddressSer(IAddressRepo iAddressRepo) {
        ADDRESS_REPO = iAddressRepo;
    }

    @Override
    public Address save(User user, AddressUse addressUse) {
        Address address = convertToOtherData.getAddress(addressUse);
        if(address.isUsingAddress()) {
            List<AddressUse> addressUses = getAllByUser(user);
            addressUses.forEach((e) -> {
                if(e.isUsingAddress()) {
                    Address address1 = ADDRESS_REPO.findById(e.getId()).orElseThrow();
                    address1.setUsingAddress(false);
                    ADDRESS_REPO.save(address1);
                }
            });
        }
        address.setUserAddress(user);
      return   ADDRESS_REPO.save(address);
    }

    @Override
    public void delete(Long id) {
        ADDRESS_REPO.deleteById(id);
    }

    @Override
    public List<AddressUse> getAllByUser(User user) {
        List<AddressUse> addressUses = new ArrayList<>();
        ADDRESS_REPO.findAllByUserAddress(user).orElseThrow().forEach((e) -> {
            AddressUse addressUse = convertToOtherData.getAddressUse(e);
            addressUses.add(addressUse);
        });
        return addressUses;
    }


    @Override
    public AddressUse getById(Long id) {
        Address address = ADDRESS_REPO.findById(id).orElseThrow();
        return convertToOtherData.getAddressUse(address);
    }



    @Override
    public AddressUse getByUserActive(User user) {
        if(user.getAddresses().isEmpty()) {
            return  null;
        }else  {
            Predicate<Address> streamsPredicate = Address::isUsingAddress;
            List<Address> addresses = user.getAddresses().stream().filter(streamsPredicate).toList();
            return  convertToOtherData.getAddressUse(addresses.get(0));
        }
    }
}
