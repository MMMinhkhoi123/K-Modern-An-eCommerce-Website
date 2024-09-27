package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.Cookie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICookieRepo extends JpaRepository<Cookie, String> {
}
