package com.leventsclone.leventsclone.security;

import com.leventsclone.leventsclone.entity.User;
import com.leventsclone.leventsclone.service.impl.UserSer;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserSer userSer;

    private final UserDetailsService detailsService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public CustomAuthenticationProvider(UserSer userSer, UserDetailsService userDetailsService) {
        this.userSer = userSer;
        this.detailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication){
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();
        if(!userSer.checkByEmail(userName)) {
            System.out.println("tai khoan khong ton tai" + userName);
           throw new BadCredentialsException("Khong co tai khoan");
        }else  {
            User user = userSer.getByEmail(userName).orElseThrow();
            if(passwordEncoder.matches(password,user.getPasswordUser())) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                UserDetails userDetails = detailsService.loadUserByUsername(userName);
                authorities.add(new SimpleGrantedAuthority(user.getRoles().get(0).getNameRole()));
                return new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
            } else  {
                System.out.println("sai mat khau");
                throw new BadCredentialsException("sai mat khau");
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
