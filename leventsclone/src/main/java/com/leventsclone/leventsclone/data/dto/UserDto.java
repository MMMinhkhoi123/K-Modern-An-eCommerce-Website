package com.leventsclone.leventsclone.data.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String fullName;
    private String phone;
    private String email;
    private String date;
    private String password;
    private String gender;
}
