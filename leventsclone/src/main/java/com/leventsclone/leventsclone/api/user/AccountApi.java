package com.leventsclone.leventsclone.api.user;
import com.leventsclone.leventsclone.data.dto.LoginDto;
import com.leventsclone.leventsclone.data.use.AddressUse;
import com.leventsclone.leventsclone.entity.User;
import com.leventsclone.leventsclone.security.CustomAuthenticationProvider;
import com.leventsclone.leventsclone.service.impl.AddressSer;
import com.leventsclone.leventsclone.service.impl.EmailSer;
import com.leventsclone.leventsclone.service.impl.UserSer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
public class AccountApi {

    private final UserSer userSer;
    private final AddressSer addressSer;
    private final EmailSer emailSer;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    public AccountApi( CustomAuthenticationProvider customAuthenticationProvider, AddressSer AddressSer, EmailSer emailSer, UserSer userSer) {
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.addressSer = AddressSer;
        this.emailSer = emailSer;
        this.userSer = userSer;
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<AddressUse> getAddress(@PathVariable("id")Optional<Long> id) {
        return  ResponseEntity.ok(addressSer.getById(id.orElseThrow()));
    };

    @PostMapping("/login-check")
    public ResponseEntity<Void> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginDto.getUserName(), loginDto.getPassword());
        SecurityContextHolder.getContext().setAuthentication(customAuthenticationProvider.authenticate(authentication));
        return  ResponseEntity.ok(null);
    };

    @GetMapping("/authen")
    public Map<String, Object> getData(Authentication authentication) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("id", UUID.randomUUID().toString());
        model.put("role", authentication.getAuthorities().toArray());
        model.put("content", "Hello " +authentication.getName());
        return model;
    };


    @GetMapping("/check")
    public ResponseEntity<String> getcheck() {
        return  ResponseEntity.ok("dang nhap thanh cong");
    };

    @PostMapping("/resetPass-account")
    public String resetPassport(@RequestParam("email") Optional<String> email) {
        if(userSer.getByEmail(email.orElseThrow()).isEmpty()) {
            return  "Email không tồn tại";
        }
        return emailSer.SendEmailVerify(email.orElseThrow());
    };
}
