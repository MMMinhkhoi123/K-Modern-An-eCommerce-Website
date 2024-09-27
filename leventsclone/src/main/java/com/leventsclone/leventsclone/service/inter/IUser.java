package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.dto.UserDto;
import com.leventsclone.leventsclone.data.use.AuthenticatedUse;
import com.leventsclone.leventsclone.entity.User;

import java.util.Date;
import java.util.Optional;

public interface IUser {
    User save(UserDto userDto);
    boolean checkByEmail(String email);
    boolean getByPhone(String email);
    String updateTokenFrom(User user);
    void updateGrade(User user,int price);

    void setNewPassword(String newPassword, User user);
    void updateVerifyEmail(String email, String code, Long time);
    AuthenticatedUse getByUserName(String email);
    Optional<User> getByEmail(String email);
    Optional<User> getByKey(String key);
    Optional<User> getByKeyAndDate(Date date, String code);
}
