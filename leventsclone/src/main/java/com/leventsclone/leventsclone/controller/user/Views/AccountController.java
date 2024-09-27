package com.leventsclone.leventsclone.controller.user.Views;
import com.leventsclone.leventsclone.data.dto.UserDto;
import com.leventsclone.leventsclone.data.use.OderUse;
import com.leventsclone.leventsclone.entity.User;
import com.leventsclone.leventsclone.entity.UserVoucher;
import com.leventsclone.leventsclone.security.CustomUserServiceDetail;
import com.leventsclone.leventsclone.service.impl.UserSer;
import com.leventsclone.leventsclone.service.impl.UserVoucherSer;
import com.leventsclone.leventsclone.service.impl.VoucherSer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping(value = "/account")
public class AccountController {
    private final UserSer userSer;
    private final UserVoucherSer userVoucher;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public AccountController(UserSer userSer, UserVoucherSer userVoucherSer) {
        this.userSer = userSer;
        this.authenticationManager = null;
        this.userVoucher = userVoucherSer;
    }



    @GetMapping(value = "/reset/{expire}")
    public String resetPassword(Model model, @PathVariable("expire") Optional<Long> ex, @RequestParam("verify") Optional<String> verify) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ex.orElseThrow());
        if(userSer.getByKeyAndDate(calendar.getTime(), verify.orElseThrow()).isPresent()) {
            if(ex.orElseThrow() < System.currentTimeMillis()) {
                model.addAttribute("errorVerify", "Hết hạn đặt lại mật khẩu vui lòng xác thực lại");
                model.addAttribute("account", "forget");
                model.addAttribute("dataCart", new ArrayList<>());
                return  "user/Page/account";
            } else {
                model.addAttribute("dataCart", new ArrayList<>());
                UserDto userDto = new UserDto();
                model.addAttribute("user", userDto);
                if(Objects.equals(userSer.getByKeyAndDate(calendar.getTime(), verify.orElseThrow()).orElseThrow().getStateResetPassUser(), "success")) {
                    model.addAttribute("account", "forget");
                } else  {
                    User user = userSer.getByKeyAndDate(calendar.getTime(), verify.orElseThrow()).orElseThrow();
                    model.addAttribute("account", "reset");
                    String codeTokenForm =  userSer.updateTokenFrom(user);
                    model.addAttribute("tokenFrom", codeTokenForm);
                    model.addAttribute("time", ex.orElseThrow());
                    model.addAttribute("verify", verify.orElseThrow());
                }
                return  "user/Page/account";
            }
        }else  {
            return "redirect:account/error";
        }
    }

    @GetMapping(value = "/register")
    public String register(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
            return "redirect:/";
        }else  {
            UserDto userDto = new UserDto();
            model.addAttribute("user", userDto);
            model.addAttribute("account", "signup");
            model.addAttribute("dataCart", new ArrayList<>());
            return  "user/Page/account";
        }
    }

    @GetMapping(value = "/login")
    public String login(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
            return "redirect:/";
        }else  {
            UserDto userDto = new UserDto();
            model.addAttribute("user", userDto);
            model.addAttribute("account", "signin");
            model.addAttribute("dataCart", new ArrayList<>());
            return  "user/Page/account";
        }
    }


    @PostMapping(value = "/login-save")
    public String login(@ModelAttribute(value = "user") UserDto userDto,
                        BindingResult result, HttpServletRequest request,
                        Model model
    ) {
        Optional<User> user = userSer.getByEmail(userDto.getEmail());
        if(user.isEmpty()) {
            result.rejectValue("email", null, "Tài khoản không tồn tại");
        } else
        {
            String password = user.get().getPasswordUser();
            if(!encoder.matches(userDto.getPassword(),password)) {
                result.rejectValue("password", null, "Mật khẩu không chính xác");
            } else {
                Authentication authenticationRequest =
                        UsernamePasswordAuthenticationToken.unauthenticated(userDto.getEmail(), userDto.getPassword());
                assert this.authenticationManager != null;
                Authentication authenticationResponse =
                        this.authenticationManager.authenticate(authenticationRequest);
                SecurityContextHolder.getContext().setAuthentication(authenticationResponse);
            }
        }
        if(result.hasErrors()) {
            model.addAttribute("account", "signin");
            return "user/Page/account";
        }
        return "redirect:/";
    }


    @PostMapping(value = "/register-save")
    public String register(@ModelAttribute(value = "user") UserDto userDto,
                           Model model,
                           BindingResult result) {
        List<OderUse> list = new ArrayList<>();
        model.addAttribute("account", "signup");
        model.addAttribute("dataCart", list);
        if(userSer.checkByEmail(userDto.getEmail())) {
            result.rejectValue("email", null, "Email đã được sữ dụng");
        }
        if(userSer.getByPhone(userDto.getPhone())) {
            result.rejectValue("phone", null, "Số điện thoại đã được sữ dụng");
        }
        String patternString = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(userDto.getPassword());
        if(!matcher.matches()) {
            result.rejectValue("password", null, "Mật khẩu từ 8 ký tự và có ít nhất một số và một chữ");
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = formatter.parse(userDto.getDate());
            if(date.getTime() > System.currentTimeMillis()) {
                result.rejectValue("date", null, "Ngày sinh không lớn hơn ngày hiện tại");
            }
        } catch (Exception ex) {
            result.rejectValue("date", null, "Ngày sinh không đúng định dạng");
        }
        if(result.hasErrors()) {
            return "user/Page/account";
        }
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        User user = userSer.save(userDto);
        userVoucher.addVoucherLogin(user);
        return "redirect:/account/login";
    }


    @GetMapping(value = "/login-forget")
    public String forget(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
            return "redirect:/";
        }else  {
            model.addAttribute("account", "forget");
            model.addAttribute("dataCart", new ArrayList<>());
            return  "user/Page/account";
        }
    }

    @GetMapping(value = "/error")
    public String err(Model model) {
        return  "user/fragments/common/test";
    }


    @PostMapping(value = "/resetPassword")
    public String resetPassword(Model model,
                                @RequestParam("password") Optional<String> pass,
                                @RequestParam("token") Optional<String> token,
                                @RequestParam("time") Optional<Long> time ,
                                @RequestParam("verify") Optional<String> verify) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.orElseThrow());
        if(userSer.getByKeyAndDate(calendar.getTime(), verify.orElseThrow()).isPresent()) {
            if(time.orElseThrow() < System.currentTimeMillis()) {
                model.addAttribute("errorVerify", "Hết hạn đặt lại mật khẩu vui lòng xác thực lại");
                model.addAttribute("account", "forget");
                model.addAttribute("dataCart", new ArrayList<>());
                return  "user/Page/account";
            } else {
                User user = userSer.getByKeyAndDate(calendar.getTime(), verify.orElseThrow()).orElseThrow();
                model.addAttribute("dataCart", new ArrayList<>());
                UserDto userDto = new UserDto();
                model.addAttribute("user", userDto);
                if(Objects.equals(user.getStateResetPassUser(), "success")) {
                    model.addAttribute("account", "forget");
                } else  {
                    if(user.getTokenFormPassUser().equals(token.orElseThrow())) {
                        model.addAttribute("account", "signin");
                        String passNew = encoder.encode(pass.orElseThrow());
                        userSer.setNewPassword(passNew, user);
                    } else  {
                        model.addAttribute("account", "signin");
                    }
                }
                return  "user/Page/account";
            }
        }else  {
            return "redirect:account/error";
        }
    }

}
