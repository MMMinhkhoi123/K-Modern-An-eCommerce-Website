package com.leventsclone.leventsclone.security;

import com.leventsclone.leventsclone.repository.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
public class CustomUserServiceDetail implements UserDetailsService {

    @Autowired
   IUserRepo USER_REPO;

    @Override
    public UserDetails loadUserByUsername(String username)  {
        if(USER_REPO.findByEmailUser(username).isPresent()) {
            return new CustomerUserDetail(USER_REPO.findByEmailUser(username).orElseThrow());
        }else  {
            throw new BadCredentialsException("Tài khoản không ton tai");
        }
    }
}
