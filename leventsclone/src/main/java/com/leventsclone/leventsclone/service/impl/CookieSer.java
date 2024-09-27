package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.repository.ICookieRepo;
import com.leventsclone.leventsclone.service.inter.ICookie;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class CookieSer implements ICookie {

    private final ICookieRepo COOKIE_REPO;
    private final CartSer oderSer;
    private final int maxAgeInSeconds = 24000;
    private final String keyCart = "CartLevent";

    @Autowired
    public CookieSer(CartSer oderSer, ICookieRepo cookie) {
        this.oderSer = oderSer;
        COOKIE_REPO = cookie;
    }

    @Override
    public Optional<com.leventsclone.leventsclone.entity.Cookie> getByCode(String code) {
        return COOKIE_REPO.findById(code);
    }

    @Override
    public boolean checkCode(String code) {
        if (COOKIE_REPO.findById(code).isEmpty()) {
            return  true;
        } else  {
            return false;
        }
    }

    @Override
    public Cookie createNewCookie(String code) {
        Date x = new Date();
        x.setDate(x.getDate() + 1);
        com.leventsclone.leventsclone.entity.Cookie cookies = new com.leventsclone.leventsclone.entity.Cookie();
        cookies.setIdCookie(code);
        cookies.setExpire(x);
        COOKIE_REPO.save(cookies);
        Cookie cookie = new Cookie(keyCart, code);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAgeInSeconds);
        return cookie;
    }
    @Override
    public boolean checkContainCookie(Cookie[] cookies) {
        Boolean status = false;
        Cookie cookieTam = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(keyCart)) {
                cookieTam = cookie;
                status = true;
            }
        }
        if(cookieTam == null) {
            status = false;
        } else
        if(COOKIE_REPO.findById(cookieTam.getValue()).isEmpty()) {
            status = false;
        }
        return  status;
    }

    @Override
    public boolean checkExpireCookie(Cookie[] cookies) {
        boolean status = true;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(keyCart)) {
                System.out.println(cookie.getValue());
              com.leventsclone.leventsclone.entity.Cookie cookie1 = COOKIE_REPO.findById(cookie.getValue()).orElseThrow();

              if(cookie1.getExpire().getTime() <= System.currentTimeMillis()) {
                  status = false;
              }
            }
        }
        return  status;
    }

    @Override
    public Cookie getCookie(Cookie[] cookies) {
        Cookie cookieGet = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(keyCart)) {
                cookieGet = cookie;
            }
        }
        return cookieGet;
    }

    @Override
    public Cookie updateCookie(Cookie[] cookies) {
        Cookie cookieOld = getCookie(cookies);

        com.leventsclone.leventsclone.entity.Cookie cookie
                = COOKIE_REPO.findById(cookieOld.getValue()).orElseThrow();
        Date x = new Date();
        x.setDate(x.getDate() + 1);
        cookie.setExpire(x);
        COOKIE_REPO.save(cookie);

        Cookie cookieNew = new Cookie(keyCart,cookieOld.getValue());
        cookieNew.setPath("/");
        cookieNew.setHttpOnly(true);
        cookieNew.setMaxAge(maxAgeInSeconds);
        return  cookieNew;
    }
}

