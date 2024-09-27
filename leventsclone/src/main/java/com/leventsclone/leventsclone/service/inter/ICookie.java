package com.leventsclone.leventsclone.service.inter;

import jakarta.servlet.http.Cookie;

import java.util.Optional;

public interface ICookie {
     Optional<com.leventsclone.leventsclone.entity.Cookie> getByCode(String code);

    boolean checkCode(String code);

    Cookie createNewCookie(String code);

    boolean checkContainCookie(Cookie[] cookies);

    boolean checkExpireCookie(Cookie[] cookies);

    Cookie getCookie(Cookie[] cookies);

    Cookie updateCookie(Cookie[] cookies);
}
