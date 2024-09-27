package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.data.dto.UserDto;
import com.leventsclone.leventsclone.data.use.*;
import com.leventsclone.leventsclone.entity.*;
import com.leventsclone.leventsclone.repository.IUserRepo;
import com.leventsclone.leventsclone.service.inter.IUser;
import com.leventsclone.leventsclone.unstil.ConvertToOtherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

@Service
public class UserSer implements IUser {
    private  final IUserRepo USER_REPO;
    private final MemberSer memberSer;
    private final RoleSer roleSer;
    private final OptionSer optionSer;
    private final ConvertToOtherData convertToOtherData = new ConvertToOtherData();
    private final AddressSer addressSer;
    private final UserWishlistSer userWishlistSer;
    @Autowired
    public UserSer(OptionSer optionSer,UserWishlistSer userWishlistSer,IUserRepo userRepo,RoleSer roleSer, MemberSer memberSer, AddressSer addressSer) {
        this.USER_REPO = userRepo;
        this.roleSer = roleSer;
        this.memberSer = memberSer;
        this.addressSer = addressSer;
        this.optionSer = optionSer;
        this.userWishlistSer = userWishlistSer;
    }

    @Override
    public User save(UserDto userDto) {
        List<Role> roles = new ArrayList<>();
        roles.add(roleSer.getRoleCustomer());
        User user = convertToOtherData.getUser(userDto);
        user.setRoles(roles);
        user.setMember(memberSer.getMemberInit());
       return  USER_REPO.save(user);
    }

    @Override
    public boolean checkByEmail(String email) {
        return USER_REPO.findByEmailUser(email).isPresent();
    }

    @Override
    public boolean getByPhone(String phone) {
        return  USER_REPO.findByPhoneUser(phone).isPresent();
    }


    public String getCode() {
        int length = 20;

        // Chuỗi ký tự có thể chọn
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // Tạo một đối tượng SecureRandom
        SecureRandom random = new SecureRandom();

        // StringBuilder để xây dựng chuỗi ngẫu nhiên
        StringBuilder randomString = new StringBuilder();

        // Tạo chuỗi ngẫu nhiên
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length()); // Chọn một vị trí ngẫu nhiên trong chuỗi ký tự
            char randomChar = characters.charAt(randomIndex); // Lấy ký tự ở vị trí đã chọn
            randomString.append(randomChar); // Thêm ký tự vào chuỗi ngẫu nhiên
        }
        return  randomString.toString();
    }

    @Override
    public String updateTokenFrom(User user) {
        String code = getCode();
        user.setTokenFormPassUser(code);
        USER_REPO.save(user);
        return  code;
    }

    @Override
    public void updateGrade(User user, int price) {

        int gradeSetMember = price / 10;


        int GradeMemberNew = user.getGradeSetMemberUser() + gradeSetMember;
        user.setGradeSetMemberUser(GradeMemberNew);

        Predicate<MemberUse> streamsPredicate = item -> item.getGrade() <= user.getGradeSetMemberUser();

        List<MemberUse> memberUses = new ArrayList<>(memberSer.getAll().stream()
                .filter(streamsPredicate)
                .toList());

        Comparator<MemberUse> resComparator = new Comparator<MemberUse>() {
            @Override
            public int compare(MemberUse o1, MemberUse o2) {
                return Double.compare(o2.getGrade(), o1.getGrade());
            }
        };

        memberUses.sort(resComparator);
        MemberUse memberUse = memberUses.get(0);
        user.setMember(memberSer.getByIdF(memberUse.getId()));
        USER_REPO.save(user);
    }

    @Override
    public void setNewPassword(String newPassword, User user) {
        user.setPasswordUser(newPassword);
        user.setStateResetPassUser("success");
        USER_REPO.save(user);
    }

    @Override
    public void updateVerifyEmail(String email, String code, Long time) {
        User user = getByEmail(email).orElseThrow();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        user.setExpireForGetPassUser(date);
        user.setKeyForGetPassUser(code);
        user.setStateResetPassUser("wait");
        USER_REPO.save(user);
    }


    @Override
    public AuthenticatedUse getByUserName(String email) {

        User user = USER_REPO.findByEmailUser(email).orElseThrow();

        AuthenticatedUse authenticatedUse = new AuthenticatedUse();
        authenticatedUse.setRole(user.getRoles().get(0).getNameRole());
        authenticatedUse.setFullName(user.getNameUser());
        authenticatedUse.setEmail(email);

        List<VoucherUse> voucherUses = new ArrayList<>();
        user.getUserVouchers().forEach((e) -> {
            if(e.getQuantity() > 0) {
                voucherUses.add(convertToOtherData.getVoucherUse(e.getVoucher()));
            }
        });
        authenticatedUse.setMemberUse(convertToOtherData.getMemberUse(user.getMember()));
        authenticatedUse.setVoucherUses(voucherUses);
        authenticatedUse.setPhone(user.getPhoneUser());
        authenticatedUse.setGrade(user.getGradeSetMemberUser());
        authenticatedUse.setAddressUses(addressSer.getAllByUser(user));

        Predicate<MemberUse> streamsPredicate = item -> item.getGrade() > user.getGradeSetMemberUser();
        List<MemberUse> memberUses = new ArrayList<>(memberSer.getAll().stream()
                .filter(streamsPredicate)
                .toList());
        Comparator<MemberUse> resComparator = new Comparator<MemberUse>() {
            @Override
            public int compare(MemberUse o1, MemberUse o2) {
                return Double.compare(o2.getGrade(), o1.getGrade());
            }
        };

        List<OptionUse> optionUses = new ArrayList<>();

        userWishlistSer.getByUser(user).forEach((e) -> {
            OptionUse optionUse = optionSer.getDetailById(e.getOption().getId());
            optionUses.add(optionUse);
        });

        memberUses.sort(resComparator);
        if(!memberUses.isEmpty()) {
            authenticatedUse.setMemberUseUp(memberUses.get(0));
        }

        String gender = user.getGenderUser().trim();
        authenticatedUse.setGender(gender);
        authenticatedUse.setWishlist(optionUses);
        Date date =  user.getBirthdayUser();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0 nên cần cộng thêm 1
        int year = calendar.get(Calendar.YEAR);
        String dateConvert = day + "/" + month + "/" + year;
        authenticatedUse.setDate(dateConvert);
        return authenticatedUse;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return USER_REPO.findByEmailUser(email);
    }

    @Override
    public Optional<User> getByKey(String key) {
        return USER_REPO.findByKeyForGetPassUser(key);
    }

    @Override
    public Optional<User> getByKeyAndDate(Date date, String code) {
        return USER_REPO.findByExpireForGetPassUserAndKeyForGetPassUser(date, code);
    }

    public EstimateUse estimateUser(String type) {
        List<User> list = USER_REPO.findAll();

        List<User> orderLastWeek = list.stream().filter(filterEstimate("LAST_"+ type)).toList();
        List<User> orderThisWeek = list.stream().filter(filterEstimate("THIS_" + type)).toList();

        return  getDataCompareEstimate(orderThisWeek.size(), orderLastWeek.size());
    }

    private EstimateUse getDataCompareEstimate(int recent , int last) {
        EstimateUse estimateUse = new EstimateUse();
        if(recent >= last) {
            int size = recent - last;
            if(recent == last) {
                estimateUse.setValue(recent);
            } else  {
                estimateUse.setValue(size);
            }
            estimateUse.setIncrease(true);
            double percent = 0;
            if(last == 0) {
                percent = 100;
            }else  {
                double x =  ((double) last /  (double) recent) * 100;
                percent = 100 - x;
            }
            estimateUse.setPercent(Math.round(percent));
        } else  {
            estimateUse.setValue(recent);
            estimateUse.setIncrease(false);
            double percent = 0;
            double x = ((double) recent / (double) last) * 100;
            percent = 100 - x;
            estimateUse.setPercent(Math.round(percent));
        }
        return  estimateUse;
    }

    private Predicate<User> ordersPredicateYear(int years) {
        Predicate<User> predicate = null;
        predicate  = item -> GetYear(item.getCreatedDate().getTime()) == years;
        return  predicate;
    }
    private Predicate<User> ordersPredicateMonthly(int month) {
        Predicate<User> predicate = null;
        predicate  = item -> GetMonth(item.getCreatedDate().getTime()) == month;
        return  predicate;
    }


    public List<EstimateMonthlyUse> estimateMonthlyUser(int year) {
        int key = 300;

        List<EstimateMonthlyUse> estimateMonthlyUses = new LinkedList<>();
        List<User> list = USER_REPO.findAll();
        List<User> orders = list.stream().filter(ordersPredicateYear(year)).toList();

        for (int i = 1; i <= 12; i ++ ) {
            List<User> users = orders.stream().filter(ordersPredicateMonthly(i)).toList();
            if(orders.isEmpty()) {
                EstimateMonthlyUse use = new EstimateMonthlyUse();
                use.setMonth(i);
                use.setPercent(0);
                use.setValue(0);
                estimateMonthlyUses.add(use);
            }else  {

                EstimateMonthlyUse use = new EstimateMonthlyUse();
                double percent =( (double) users.size() / (double)  key) * 100;
                use.setPercent(Math.round(percent));
                use.setValue(users.size());
                use.setMonth(i);
                estimateMonthlyUses.add(use);
            }
        }
        return  estimateMonthlyUses;
    }

    private Predicate<User> filterEstimate(String type) {
        Calendar calendar = Calendar.getInstance();
        Predicate<User> predicate = null;
        switch (type) {
            case "LAST_WEEK" -> {
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
                int year = calendar.get(Calendar.YEAR);
                predicate = item -> GetWeekOfYear(item.getCreatedDate().getTime()) == weekOfYear
                        && GetYear(item.getCreatedDate().getTime()) == year;
            }
            case "THIS_WEEK" -> {
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                predicate = item -> GetWeek(item.getCreatedDate().getTime()) == dayOfWeek;
            }
            case "THIS_DAY" -> {
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                predicate = item -> GetDate(item.getCreatedDate().getTime()) == day
                        && GetMonth(item.getCreatedDate().getTime()) == month
                        && GetYear(item.getCreatedDate().getTime()) == year;
            }
            case "LAST_DAY" -> {
                calendar.add(Calendar.DATE, -1);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                predicate = item -> GetDate(item.getCreatedDate().getTime()) == day
                        && GetMonth(item.getCreatedDate().getTime()) == month
                        && GetYear(item.getCreatedDate().getTime()) == year;
            }
            case "THIS_MONTH" -> {
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                predicate  = item -> GetYear(item.getCreatedDate().getTime()) == year && GetMonth(item.getCreatedDate().getTime()) == month;
            }
            case "LAST_MONTH" -> {
                calendar.add(Calendar.MONTH, -1);
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                predicate = item -> GetMonth(item.getCreatedDate().getTime())  ==  month && GetYear(item.getCreatedDate().getTime()) == year;
            }
            case "THIS_YEAR" -> {
                int year = calendar.get(Calendar.YEAR);
                predicate  = item -> GetYear(item.getCreatedDate().getTime()) == year;
            }
            case "LAST_YEAR" -> {
                calendar.add(Calendar.YEAR, -1);
                int year = calendar.get(Calendar.YEAR);
                predicate = item -> GetYear(item.getCreatedDate().getTime())  == year;
            }
        }
        return  predicate;
    }


    private int GetYear(Long data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(data);
        return  calendar.get(Calendar.YEAR);
    }

    private int GetMonth(Long data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(data);
        return   calendar.get(Calendar.MONTH) + 1;
    }

    private int GetWeek(Long data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(data);
        return   calendar.get(Calendar.DAY_OF_WEEK);
    }

    private int GetWeekOfYear(Long data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(data);
        return   calendar.get(Calendar.WEEK_OF_YEAR);
    }

    private int GetDate(Long data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(data);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
