package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.dto.InfoOrderDto;
import com.leventsclone.leventsclone.data.dto.ResultDto;
import com.leventsclone.leventsclone.data.request.OderDefectReq;
import com.leventsclone.leventsclone.data.response.CustomerRes;
import com.leventsclone.leventsclone.data.response.DataSumBillRes;
import com.leventsclone.leventsclone.data.response.OrderRes;
import com.leventsclone.leventsclone.data.use.CartUse;
import com.leventsclone.leventsclone.data.use.DetailOrderUse;
import com.leventsclone.leventsclone.data.use.OderUse;
import com.leventsclone.leventsclone.data.use.VoucherUse;
import com.leventsclone.leventsclone.entity.*;
import jakarta.servlet.http.Cookie;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IOrder {
    OrderRes getOrderRes(Orders orders);
    OrderRes updateState(String code, String key);
    List<OrderRes> getOrdersByKey(String key, User user);

     List<Orders> getOrderSuccessByUserAndOption(User user, Long idOption);

    void uploadImgPay(String code, MultipartFile multipartFile);

    void uploadImgRefund(String code, MultipartFile multipartFile);
    void updateEmail(String code, String email);

    Boolean getSituation(String code);

    OrderRes updateStateDefect(String code, String key, List<OderDefectReq> oderDefectReqs);
    OrderRes getByCodeF(String code);
    DataSumBillRes getSumByCode(String code);
    List<OrderRes> getAll();

    List<OderUse> getDetailOrder(String code);

    List<VoucherUse> getVouchers(String code);

    List<OrderRes> getOrderByCode(String state);
    List<OrderRes> filterByKey(List<OrderRes> orderRes, String key);
    List<OrderRes> filterByTypePay(List<OrderRes> orderRes, String type);
    List<OrderRes> filterByDate(List<OrderRes> orderRes, String date);
    List<CustomerRes> getInfoCustomer();

    List<CustomerRes> searchInfoByCode(String code, String state);

    List<CustomerRes> getInfoByState(String state);

    ResultDto save(InfoOrderDto infoOrderDto, User user, Cookie cookie);

    boolean checkCode(String code);

    Orders getByCode(String code);

    DetailOrderUse getDetailByCode(String code);

    List<CartUse> getInfoByCode(String code);

    List<VoucherUse> getVoucherUse(String code);
}
