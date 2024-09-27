package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.data.dto.InfoOrderDto;
import com.leventsclone.leventsclone.data.dto.ResultDto;
import com.leventsclone.leventsclone.data.request.OderDefectReq;
import com.leventsclone.leventsclone.data.response.CustomerRes;
import com.leventsclone.leventsclone.data.response.DataSumBillRes;
import com.leventsclone.leventsclone.data.response.OrderRes;
import com.leventsclone.leventsclone.data.use.*;
import com.leventsclone.leventsclone.entity.*;
import com.leventsclone.leventsclone.repository.IOrderRepo;
import com.leventsclone.leventsclone.service.inter.IOrder;
import com.leventsclone.leventsclone.unstil.ConvertToOtherData;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

@Service
public class OrderSer implements IOrder {

    private final IOrderRepo ORDER_REPO;
    private static final int CODE_LENGTH = 8;
    private final UserVoucherSer userVoucherSer;
    private final VoucherSer voucherSer;
    private final ProductSer productSer;
    private final CloudinarySer cloudinarySer;
    private final ConvertToOtherData convertToOtherData = new ConvertToOtherData();

    private final UserSer userSer;

    private final  EmailSer emailSer;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final SocketSer socketSer;

    private  final  OptionSer optionSer;
    private final OptionSizeSer optionSizeSer;
    private final CartSer cartSer;


    @Autowired
    public OrderSer(CartSer cartSer,
                    OptionSer optionSer
            ,OptionSizeSer optionSize1,
                    SocketSer socketSer,
                    EmailSer emailSer,
                    UserSer userSer,
                    CloudinarySer cloudinarySer,
                    ProductSer productSer,
                    IOrderRepo iOrderRepo,
                    VoucherSer voucherSer, UserVoucherSer userVoucherSer) {
        ORDER_REPO = iOrderRepo;
        this.optionSer = optionSer;
        this.cloudinarySer = cloudinarySer;
        this.voucherSer = voucherSer;
        this.userVoucherSer = userVoucherSer;
        this.productSer = productSer;
        this.userSer = userSer;
        this.emailSer = emailSer;
        this.socketSer = socketSer;
        this.optionSizeSer = optionSize1;
        this.cartSer = cartSer;
    }


    private InfoOrder infoOrder(InfoOrderDto infoOrderDto) {
        InfoOrder infoOrder = new InfoOrder();
        infoOrder.setAddressOrder(infoOrderDto.getAddress());
        infoOrder.setPhoneOrder(infoOrderDto.getPhone());
        infoOrder.setPhoneExtraOrder(infoOrderDto.getPhoneExtra());
        infoOrder.setPackagingOrder(infoOrderDto.isPackaging());
        infoOrder.setSuitOrder(infoOrderDto.getSuit());
        infoOrder.setContentPackagingOrder(infoOrderDto.getContentPackaging());
        infoOrder.setTypeTransportOrder(infoOrderDto.getTypeTransport());
        infoOrder.setTypePaymentOrder(infoOrderDto.getTypePayment());
        infoOrder.setLastNameOrder(infoOrderDto.getLastName());
        infoOrder.setFirstName(infoOrderDto.getFirstName());
        return  infoOrder;
    }

    public int getTotalByCode(String code) {
        Orders orders = getByCode(code);
        AtomicInteger total = new AtomicInteger();
        orders.getOrderDetails().forEach((e) -> {
            total.set(total.get() + e.getPrice());
        });
        return  total.get();
    }

    public  int getPriceShipByCode(String code) {
        Orders orders = getByCode(code);
        return (Integer) (int) getPriceTransport(orders.getInfoOrder().getTypeTransportOrder());
    }

    public  int getTotalOldByCode(String code) {
        Orders orders = getByCode(code);
        int total = getTotalByCode(code);
        int ship = getPriceShipByCode(code);
        AtomicInteger priceVoucherSubtract = new AtomicInteger();
        orders.getVouchers().forEach((e) -> {
            if(Objects.equals(e.getTypeVoucher(), "PERCENT")) {
                int priceGet = (int) (total * Double.valueOf (e.getPercentDiscountVoucher() / 100));
                priceVoucherSubtract.set((int) (priceVoucherSubtract.get() + priceGet));
            }else  {
                priceVoucherSubtract.set((int) (priceVoucherSubtract.get() + e.getPriceDiscountVoucher()));
            }
        });
        return (total + ship) - priceVoucherSubtract.get();
    }

    public int getTotalSetByCode(String code) {
        int ship = getPriceShipByCode(code);
        int total_old = getTotalOldByCode(code);
        Orders orders = getByCode(code);
        int totalSet = 0;
        if(orders.getUser() != null) {
            totalSet =  (int) (total_old - (total_old * ((double) orders.getUser().getMember().getPercentDiscountMember() / 100)));
        }else  {
            totalSet = totalSet + ship;
        }
        return  totalSet;
    }




    public double getPriceTransport(String value) {
        switch (value) {
            case "FAST" -> {
                return  60000;
            }
            case "STANDARD" -> {
                 return 35000;
                }
            default -> {
                return  0;
            }
        }
    }

    public int getStep(String value) {
        switch (value) {
            case "CHỜ", "TỪ CHỐI" -> {
                return 1;
            }
            case "XÁC NHẬN ĐƠN HÀNG" , "ĐƠN HÀNG GẶP LỖI" -> {
                return  2;
            }
            case "ĐANG CHUẨN BỊ" -> {
                return 3;
            }
            case "ĐANG GIAO" -> {
                return 4;
            }
            case "THÀNH CÔNG", "THẤT BẠI" -> {
                return 5;
            }
            default -> {
                return  0;
            }
        }
    }




    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        Random random = new Random();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(index);
            sb.append(randomChar);
        }
        if(ORDER_REPO.findByCodeOrder(sb.toString()).isPresent()) {
            generateRandomCode();
        }
        return sb.toString();
    }


    private Set<OrderDetail> getDetailOrder(List<CartUse> list, Orders orders) {
        Set<OrderDetail> list1 = new HashSet<>();
        list.forEach((e) -> {
            OrderDetail orderDetail = new OrderDetail();
               OptionSize optionSize = optionSizeSer.getEnById(e.getOptionSizeUSe().getId());
                orderDetail.setQuantity(e.getQuantity());
                orderDetail.setOrder(orders);
                orderDetail.setOptionSize(optionSize);
                orderDetail.setPrice(e.getSumPrice());
            list1.add(orderDetail);
        });
        return list1;
    }


    @Override
    public OrderRes getOrderRes(Orders orders) {
        return convertToOtherData.getOrderRes(orders);
    }

    @Override
    public OrderRes updateState(String code, String key) {
        Orders orders = ORDER_REPO.findByCodeOrder(code).orElseThrow();
        orders.setStateOrder(key);
        String content = "";
        if(key.equals("THÀNH CÔNG"))  {

            if(orders.getInfoOrder().getEmailNotifyOrder() != null && !orders.getInfoOrder().getEmailNotifyOrder().isEmpty()) {
                String emailNotify = orders.getInfoOrder().getEmailNotifyOrder();
                emailSer.SendEmailNotifyOrder(emailNotify, "your order has been delivered , Thanks you , have a nice day !", "success");
            }

            // update inventory
            orders.getOrderDetails().forEach((e) -> {
               if(e.getOptionSize() != null) {
                   int quantity = e.getOptionSize().getQuantity() - e.getQuantityDefect();
                   optionSizeSer.updateQuantity(e.getOptionSize() , quantity);
               }
            });
            // if user => update grade user
            if(orders.getUser() != null ) {
                userSer.updateGrade(orders.getUser(), orders.getSumOrder());
            }
            // if voucher not use return user
            getVoucherUse(code).forEach((e) -> {
                if(e.getPriceSubtract() ==  0) {
                    // return this voucher
                    userVoucherSer.addVoucher(orders.getUser(), voucherSer.getByIdR(e.getId()));
                }
            });
        } else if(key.equals("XÁC NHẬN ĐƠN HÀNG")) {
            if(orders.getInfoOrder().getEmailNotifyOrder() != null && !orders.getInfoOrder().getEmailNotifyOrder().isEmpty()) {
                String emailNotify = orders.getInfoOrder().getEmailNotifyOrder();
                emailSer.SendEmailNotifyOrder(emailNotify, "your order has been verified !", "handling");
            }
        }else if(key.equals("ĐANG CHUẨN BỊ")) {
            if(orders.getInfoOrder().getEmailNotifyOrder() != null && !orders.getInfoOrder().getEmailNotifyOrder().isEmpty()) {
                String emailNotify = orders.getInfoOrder().getEmailNotifyOrder();
                emailSer.SendEmailNotifyOrder(emailNotify, "your order has been handing !", "handling");
            }
        }
        else if(key.equals("ĐANG GIAO")) {
            if(orders.getInfoOrder().getEmailNotifyOrder() != null && !orders.getInfoOrder().getEmailNotifyOrder().isEmpty()) {
                String emailNotify = orders.getInfoOrder().getEmailNotifyOrder();
                emailSer.SendEmailNotifyOrder(emailNotify, "your order is delivering !", "delivery");
            }
        }else if(key.equals("THẤT BẠI")) {
            if(orders.getInfoOrder().getEmailNotifyOrder() != null && !orders.getInfoOrder().getEmailNotifyOrder().isEmpty()) {
                String emailNotify = orders.getInfoOrder().getEmailNotifyOrder();
                emailSer.SendEmailNotifyOrder(emailNotify, "Your order failed to be delivered !", "fail");
            }
        }
        ORDER_REPO.save(orders);
        return getByCodeF(code);
    }

    @Override
    public List<OrderRes> getOrdersByKey(String key, User user) {
        List<OrderRes> oderUses = new ArrayList<>();
        user.getOrders().forEach((e) -> {
          OrderRes orderRes = convertToOtherData.getOrderRes(e);
            oderUses.add(orderRes);
        });
        Predicate<OrderRes> streamsPredicate  = null ;
        if(key.equals("handling")) {
            streamsPredicate = item ->
                    !Objects.equals(item.getState(), "ĐƠN HÀNG GẶP LỖI")
                            && !Objects.equals(item.getState(), "THẤT BẠI")
                              && !Objects.equals(item.getState(), "THÀNH CÔNG")
                                  && !Objects.equals(item.getState(), "TỪ CHỐI");
        } else if(key.equals("finish")) {
            streamsPredicate = item -> Objects.equals(item.getState(), "THÀNH CÔNG");
        } else  {
            streamsPredicate = item -> Objects.equals(item.getState(), "ĐƠN HÀNG GẶP LỖI") || Objects.equals(item.getState(), "THẤT BẠI") || Objects.equals(item.getState(), "TỪ CHỐI");
        }
        return oderUses.stream()
                .filter(streamsPredicate)
                .toList();
    }

    @Override
    public List<Orders> getOrderSuccessByUserAndOption(User user, Long idOption) {

        List<Orders> orderRes = new ArrayList<>();
        if(ORDER_REPO.findAllByUser(user).isPresent()) {
            orderRes = new ArrayList<>(ORDER_REPO.findAllByUser(user).orElseThrow());
            Predicate<Orders> streamsPredicate  =
                    item -> Objects.equals(item.getStateOrder(), "THÀNH CÔNG")
                            && getExistOrderDetail(item.getOrderDetails(), idOption);
            List<Orders> res = orderRes.stream().filter(streamsPredicate).toList();
            if(res.isEmpty()) {
                return  new ArrayList<>();
            } else  {
                return  res;
            }
        } else  {
            return  new ArrayList<>();
        }
    }



    private boolean getExistOrderDetail(Set<OrderDetail> orderDetails, Long idOption) {
        Predicate<OrderDetail> streamsPredicate  = item ->
                item.getOptionSize().getOption().getId().equals(idOption);

        List<OrderDetail> res = orderDetails.stream().filter(streamsPredicate).toList();
        return !res.isEmpty();
    }

    @Override
    public void uploadImgPay(String code, MultipartFile multipartFile) {
        Orders orders = ORDER_REPO.findByCodeOrder(code).orElseThrow();
        ClouUse imagesUse =  cloudinarySer.uploadFilePay(multipartFile);
        if(orders.getKeyImagePaymentOrder() != null) {
            List<String> list = new ArrayList<>();
            list.add(orders.getKeyImagePaymentOrder());
            cloudinarySer.delete(list);
        }
        orders.setImgPaymentOrder(imagesUse.getUrl());
        orders.setKeyImagePaymentOrder(imagesUse.getKey());
        ORDER_REPO.save(orders);
    }

    @Override
    public void uploadImgRefund(String code, MultipartFile multipartFile) {
        Orders orders = ORDER_REPO.findByCodeOrder(code).orElseThrow();
        ClouUse imagesUse =  cloudinarySer.uploadFilePay(multipartFile);
        if(orders.getKeyImageRefundPaymentOrder() != null) {
            List<String> list = new ArrayList<>();
            list.add(orders.getKeyImagePaymentOrder());
            cloudinarySer.delete(list);
        }
        orders.setImgRefundPaymentOrder(imagesUse.getUrl());
        orders.setKeyImageRefundPaymentOrder(imagesUse.getKey());
        ORDER_REPO.save(orders);
    }

    @Override
    public void updateEmail(String code, String email) {
        Orders orders = ORDER_REPO.findByCodeOrder(code).orElseThrow();
        orders.getInfoOrder().setEmailNotifyOrder(email);
        ORDER_REPO.save(orders);
    }



    @Override
    public Boolean getSituation(String code) {
        Orders orders = ORDER_REPO.findByCodeOrder(code).orElseThrow();

        Set<OrderDetail> orderDetails = orders.getOrderDetails();
        AtomicInteger sum = new AtomicInteger();
        AtomicInteger sumDefect = new AtomicInteger();

        orderDetails.forEach((e) -> {
            if(e.getState() != null) {
                sumDefect.set((int)
                        (sumDefect.get() + e.getOptionSize().getOption().getProduct().getPriceProduct() * e.getQuantityDefect()));
            }
            sum.set(sum.get() + e.getPrice());

        });

        int SumResult = sum.get() - sumDefect.get();

        if (SumResult == 0) {
            return  false;
        }else  {
            return  true;
        }
    }

    @Override
    public OrderRes updateStateDefect(String code, String key, List<OderDefectReq> oderDefectReqs) {
        Orders orders = ORDER_REPO.findByCodeOrder(code).orElseThrow();
        orders.setStateOrder(key);
        Set<OrderDetail> orderDetails = new HashSet<>();
        orders.getOrderDetails().forEach((e) -> {

            List<OderDefectReq> oderDefectReqs1 = new ArrayList<>(checkIncludeDefect(e, oderDefectReqs));

            if(oderDefectReqs1.isEmpty()) {
                e.setState("ĐỦ");
            }else  {
                e.setState("LỖI");
                e.setQuantityDefect(oderDefectReqs1.get(0).getQuantity());
                e.setNote(oderDefectReqs1.get(0).getDescribe());
            }
            orderDetails.add(e);
        });
        if(orders.getInfoOrder().getEmailNotifyOrder() != null && !orders.getInfoOrder().getEmailNotifyOrder().isEmpty()) {
            String emailNotify = orders.getInfoOrder().getEmailNotifyOrder();
            emailSer.SendEmailNotifyOrder(emailNotify, "Your order has a problem.we will call you soon to solve your order", "problem");
        }
        orders.setOrderDetails(orderDetails);
        ORDER_REPO.save(orders);
        return getByCodeF(code);
    }

    private List<OderDefectReq> checkIncludeDefect(OrderDetail orderDetail , List<OderDefectReq> oderDefectReqs) {

        Predicate<OderDefectReq> streamsPredicate = item ->
                Objects.equals(item.getIdSize() , orderDetail.getOptionSize().getSize().getIdSize()) &&
                        Objects.equals(item.getIdOption(), orderDetail.getOptionSize().getOption().getId());

        return oderDefectReqs.stream()
                .filter(streamsPredicate)
                .toList();
    }

    @Override
    public OrderRes getByCodeF(String code) {
        OrderRes orderRes = new OrderRes();
        Orders orders = ORDER_REPO.findByCodeOrder(code).orElseThrow();
        orderRes.setCode(orders.getCodeOrder());
        orderRes.setState(orders.getStateOrder());
        orderRes.setImgRefund(orders.getImgRefundPaymentOrder());
        orderRes.setImgPay(orders.getImgPaymentOrder());
        orderRes.setCountState(getStep(orders.getStateOrder()));
        orderRes.setTypePay(orders.getInfoOrder().getTypePaymentOrder());
        orderRes.setEmailNotify(orders.getInfoOrder().getEmailNotifyOrder());
        return orderRes;
    }

    private int getPriceSumVoucher(String code) {

        Orders orders = ORDER_REPO.findByCodeOrder(code).orElseThrow();
        AtomicInteger sumVoucher = new AtomicInteger();
        orders.getVouchers().forEach((e) -> {

            if(e.getPriceConditionVoucher() < orders.getSumOrder()) {
                if (e.getTypeVoucher().equals("PERCENT")) {
                    int price = (int) (orders.getSumOrder() * (e.getPercentDiscountVoucher() / 100));
                    sumVoucher.set(sumVoucher.get() + price);
                } else {
                    int price = (int) e.getPriceDiscountVoucher();
                    sumVoucher.set(sumVoucher.get() + price);
                }
            }
        });
        return  sumVoucher.get();
    }

    private int getPriceDefect(String code) {
        Orders orders = ORDER_REPO.findByCodeOrder(code).orElseThrow();
        AtomicInteger priceDefect = new AtomicInteger();

        orders.getOrderDetails().forEach(orderDetail -> {

            int quantityDefect = orderDetail.getQuantityDefect();
            if(quantityDefect > 0) {
                int data = (int) ((orderDetail.getOptionSize().getOption().getProduct().getPriceProduct() + orderDetail.getOptionSize().getOption().getPrice()) * quantityDefect);
                priceDefect.set( priceDefect.get() + data);
            }
        });
        return priceDefect.get();
    }



    @Override
    public DataSumBillRes getSumByCode(String code) {

        DataSumBillRes dataSumBillRes = new DataSumBillRes();

        Orders orders = ORDER_REPO.findByCodeOrder(code).orElseThrow();
        int feeShip = getPriceShipByCode(orders.getCodeOrder());
        int totalProduct = getTotalByCode(code);

        int feeVoucher = getPriceSumVoucher(code);
        int total = orders.getSumOrder();

        int priceDefect = getPriceDefect(code);
        total = total - priceDefect;
        totalProduct = totalProduct - priceDefect;

        dataSumBillRes.setFeeTotal(total);
        dataSumBillRes.setFeeShip(feeShip);
        dataSumBillRes.setFeeProducts(totalProduct);
        dataSumBillRes.setFeeVoucher(feeVoucher);
        return  dataSumBillRes;
    }

    @Override
    public List<OrderRes> getAll() {
        List<OrderRes> orderRes = new ArrayList<>();
        ORDER_REPO.findAll().forEach((e) -> {
            orderRes.add(convertToOtherData.getOrderRes(e));
        });
        sort(orderRes);
        return orderRes;
    }


    public void sort(List<OrderRes> orderRes) {
        Comparator<OrderRes> comparator = new Comparator<OrderRes>() {
            @Override
            public int compare(OrderRes o1, OrderRes o2) {
                return Long.compare(o2.getDate(), o1.getDate());
            }
        };
        orderRes.sort(comparator);
    };

    @Override
    public List<OderUse> getDetailOrder(String code) {
        List<OderUse> oderUses = new ArrayList<>();
        Orders orders = ORDER_REPO.findByCodeOrder(code).orElseThrow();
        orders.getOrderDetails().forEach((e) -> {
            OderUse oderUse = convertToOtherData.getOrderUse(e);
            OptionUse optionUse =  optionSer.getDetailById(e.getOptionSize().getOption().getId());
            oderUse.setOptionUse(optionUse);

            if(e.getQuantityDefect() > 0) {
                int data = (int) (e.getOptionSize().getOption().getProduct().getPriceProduct() + e.getOptionSize().getOption().getPrice());
                int priceAfterDefect = e.getQuantityDefect() *  data;
                oderUse.setPriceAfterDefect(priceAfterDefect);
            }

            oderUses.add(oderUse);
        });
        return oderUses;
    }

    @Override
    public List<VoucherUse> getVouchers(String code) {
        List<VoucherUse> voucherUses = new ArrayList<>();
        Orders orders = ORDER_REPO.findByCodeOrder(code).orElseThrow();

        AtomicInteger sum = new AtomicInteger();
        orders.getOrderDetails().forEach((e) -> {
            sum.set(sum.get() + e.getPrice());
        });

        orders.getVouchers().forEach((e) -> {
            int priceDisCount  = 0;
            VoucherUse voucherUse = new VoucherUse();
            voucherUse.setName(e.getDescribeVoucher());
            voucherUse.setType(e.getTypeVoucher());
            if(e.getTypeVoucher().equals("PERCENT")) {
                priceDisCount = (int) (sum.get() * (e.getPercentDiscountVoucher()) / 100);
                voucherUse.setPrice(priceDisCount);
                voucherUse.setPercent(e.getPercentDiscountVoucher());
                sum.set(sum.get() - priceDisCount);
            } else  {
                priceDisCount = (int) e.getPriceDiscountVoucher();
                voucherUse.setPrice(e.getPriceDiscountVoucher());
                voucherUse.setPrice(priceDisCount);
                sum.set(sum.get() - priceDisCount);
            }
            voucherUses.add(voucherUse);
        });
        return voucherUses;
    }

    @Override
    public List<OrderRes> getOrderByCode(String state) {
        String code = state.replaceAll("-", " ").trim();
        Predicate<OrderRes> streamsPredicate = null;
        if(code.equals("ALL")) {
            return getAll();
        }else if(code.equals("HANDLING")) {
            streamsPredicate = item ->
                    !Objects.equals(item.getState(), "TỪ CHỐI")
                            &&  !Objects.equals(item.getState(), "THÀNH CÔNG")
                            && !Objects.equals(item.getState(), "THẤT BẠI")
                            && !Objects.equals(item.getState(), "ĐƠN HÀNG GẶP LỖI");
        } else  {
            streamsPredicate = item -> Objects.equals(item.getState(), code);
        }
        return getAll().stream()
                .filter(streamsPredicate)
                .toList();
    }


    @Override
    public List<OrderRes> filterByKey(List<OrderRes> orderRes, String key) {
        Predicate<OrderRes> streamsPredicate = item ->  item.getCode().toUpperCase().contains(key.toUpperCase());
        return orderRes.stream()
                .filter(streamsPredicate)
                .toList();
    }

    @Override
    public List<OrderRes> filterByTypePay(List<OrderRes> orderRes, String type) {
        orderRes.forEach((e) -> {
            System.out.println(type);
        });
        Predicate<OrderRes> streamsPredicate = item ->  item.getTypePay().toUpperCase().equals(type.toUpperCase());
        return orderRes.stream()
                .filter(streamsPredicate)
                .toList();
    }



    private Predicate<Orders> filterEstimate(String type) {
        Calendar calendar = Calendar.getInstance();
        Predicate<Orders> predicate = null;
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

    public EstimateUse estimateUseProfit(String type) {
        List<Orders> orders = ORDER_REPO.findAll();
        Predicate<Orders> ordersPredicate = orders1 -> Objects.equals(orders1.getStateOrder(), "THÀNH CÔNG");
        List<Orders> orders1 = orders.stream().filter(ordersPredicate).toList();
        EstimateUse estimateUse = new EstimateUse();

        List<Orders> orderLastWeek = orders1.stream().filter(filterEstimate("LAST_"+ type)).toList();
        List<Orders> orderThisWeek = orders1.stream().filter(filterEstimate("THIS_" + type)).toList();



        AtomicInteger profitLast = new AtomicInteger();
        AtomicInteger profitRecent = new AtomicInteger();
        orderLastWeek.forEach((e) -> {
            profitLast.set(profitLast.get() + e.getSumOrder());
        });
        orderThisWeek.forEach((e) -> {
            profitRecent.set(profitRecent.get() + e.getSumOrder());
        });


        estimateUse = getDataCompareEstimate(profitRecent.get(), profitLast.get());

        return  estimateUse;
    }

    public EstimateUse estimateOption(String type) {
        List<Orders> orders = ORDER_REPO.findAll();
        EstimateUse estimateUse = new EstimateUse();

        List<Orders> orderLastWeek = orders.stream().filter(filterEstimate("LAST_"+ type)).toList();
        List<Orders> orderThisWeek = orders.stream().filter(filterEstimate("THIS_" + type)).toList();


        AtomicInteger quantityLast = new AtomicInteger();
        AtomicInteger quantityRecent = new AtomicInteger();
        orderLastWeek.forEach((e) -> {
            e.getOrderDetails().forEach(orderDetail ->  {
                quantityLast.set(quantityLast.get() + orderDetail.getQuantity());
            });
        });
        orderThisWeek.forEach((e) -> {
            e.getOrderDetails().forEach(orderDetail ->  {
                quantityRecent.set(quantityRecent.get() + orderDetail.getQuantity());
            });
        });

        estimateUse = getDataCompareEstimate(quantityRecent.get(), quantityLast.get());
        return  estimateUse;
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

    private Predicate<Orders> ordersPredicateYear(int years) {
        Predicate<Orders> predicate = null;
        predicate  = item -> GetYear(item.getCreatedDate().getTime()) == years;
        return  predicate;
    }

    private Predicate<Orders> ordersPredicateMonthly(int month) {
        Predicate<Orders> predicate = null;
        predicate  = item -> GetMonth(item.getCreatedDate().getTime()) == month;
        return  predicate;
    }

    public List<EstimateMonthlyUse> estimateMonthlyProfit(int year) {
        int key = 60000000;
        List<EstimateMonthlyUse> estimateMonthlyUses = new LinkedList<>();
        List<Orders> list = ORDER_REPO.findAll();
        List<Orders> orders = list.stream().filter(ordersPredicateYear(year)).toList();
        for (int i = 1; i <= 12; i ++ ) {
            List<Orders> orders1 = orders.stream().filter(ordersPredicateMonthly(i)).toList();
            if(orders.isEmpty()) {
                EstimateMonthlyUse use = new EstimateMonthlyUse();
                use.setMonth(i);
                use.setPercent(0);
                use.setValue(0);
                estimateMonthlyUses.add(use);
            }else  {

                AtomicInteger profit = new AtomicInteger();
                orders1.forEach(orders2 ->  {
                    profit.set( profit.get() + orders2.getSumOrder());
                });

                EstimateMonthlyUse use = new EstimateMonthlyUse();
                double percent =( (double)profit.get() / (double)  key) * 100;
                use.setPercent(Math.round(percent));
                use.setValue(profit.get());
                use.setMonth(i);
                estimateMonthlyUses.add(use);
            }
        }
        return  estimateMonthlyUses;
    }

    public List<EstimateMonthlyUse> estimateMonthlyProduct(int year) {
        int key = 300;
        List<EstimateMonthlyUse> estimateMonthlyUses = new LinkedList<>();
        List<Orders> list = ORDER_REPO.findAll();
        List<Orders> orders = list.stream().filter(ordersPredicateYear(year)).toList();
        for (int i = 1; i <= 12; i ++ ) {
            List<Orders> orders1 = orders.stream().filter(ordersPredicateMonthly(i)).toList();
            if(orders.isEmpty()) {
                EstimateMonthlyUse use = new EstimateMonthlyUse();
                use.setMonth(i);
                use.setPercent(0);
                use.setValue(0);
                estimateMonthlyUses.add(use);
            }else  {

                AtomicInteger quantity = new AtomicInteger();
                orders1.forEach(orders2 ->  {
                    orders2.getOrderDetails().forEach(orderDetail -> {
                        quantity.set(quantity.get() + orderDetail.getQuantity());
                    });
                });

                EstimateMonthlyUse use = new EstimateMonthlyUse();
                double percent =( (double)quantity.get() / (double)  key) * 100;
                use.setPercent(Math.round(percent));
                use.setValue(quantity.get());
                use.setMonth(i);
                estimateMonthlyUses.add(use);
            }
        }
        return  estimateMonthlyUses;
    }
    public List<EstimateMonthlyUse> estimateMonthlyOrder(int year) {
        int key = 150;
        // Chuẩn là 300 đơn

        List<EstimateMonthlyUse> estimateMonthlyUses = new LinkedList<>();
        List<Orders> list = ORDER_REPO.findAll();
        List<Orders> orders = list.stream().filter(ordersPredicateYear(year)).toList();

        for (int i = 1; i <= 12; i ++ ) {
            List<Orders> orders1 = orders.stream().filter(ordersPredicateMonthly(i)).toList();
            if(orders.isEmpty()) {
                EstimateMonthlyUse use = new EstimateMonthlyUse();
                use.setMonth(i);
                use.setPercent(0);
                use.setValue(0);
                estimateMonthlyUses.add(use);
            }else  {
                EstimateMonthlyUse use = new EstimateMonthlyUse();

                double percent =( (double) orders1.size() / (double)  key) * 100;

                use.setPercent(Math.round(percent));
                use.setValue(orders1.size());
                use.setMonth(i);
                estimateMonthlyUses.add(use);
            }
        }
        return  estimateMonthlyUses;
    }
    public EstimateUse estimateOrder(String type) {

        List<Orders> orders = ORDER_REPO.findAll();
        EstimateUse estimateUse = new EstimateUse();

        List<Orders> orderLastWeek = orders.stream().filter(filterEstimate("LAST_"+ type)).toList();
        List<Orders> orderThisWeek = orders.stream().filter(filterEstimate("THIS_" + type)).toList();

        estimateUse = getDataCompareEstimate(orderThisWeek.size(), orderLastWeek.size());
        return  estimateUse;
    }

    @Override
    public List<OrderRes> filterByDate(List<OrderRes> orderRes, String date) {
        Calendar calendar = Calendar.getInstance();
        Predicate<OrderRes> streamsPredicate = null;
        switch (date) {
            case "TODAY" -> {
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                streamsPredicate = item -> GetDate(item.getDate()) == day
                        && GetMonth(item.getDate()) == month
                        && GetYear(item.getDate()) == year;
                return orderRes.stream()
                        .filter(streamsPredicate)
                        .toList();
            }
            case "YESTERDAY" -> {
                calendar.add(Calendar.DATE, -1);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                streamsPredicate = item -> GetDate(item.getDate()) == day
                        && GetMonth(item.getDate()) == month
                        && GetYear(item.getDate()) == year;
                return orderRes.stream()
                        .filter(streamsPredicate)
                        .toList();
            }
            case "THIS_WEEK" -> {
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                streamsPredicate = item -> GetWeek(item.getDate()) == dayOfWeek;
                return orderRes.stream()
                        .filter(streamsPredicate)
                        .toList();
            }
            case "LAST_WEEK" -> {
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
                int year = calendar.get(Calendar.YEAR);
                streamsPredicate = item -> GetWeekOfYear(item.getDate()) == weekOfYear
                && GetYear(item.getDate()) == year;
                return orderRes.stream()
                        .filter(streamsPredicate)
                        .toList();
            }
            case "THIS_MONTH" -> {
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                streamsPredicate = item -> GetYear(item.getDate()) == year && GetMonth(item.getDate()) == month;
                return orderRes.stream()
                        .filter(streamsPredicate)
                        .toList();
            }
            case "LAST_MONTH" -> {
                calendar.add(Calendar.MONTH, -1);
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                streamsPredicate = item -> GetMonth(item.getDate())  ==  month && GetYear(item.getDate()) == year;
                return orderRes.stream()
                        .filter(streamsPredicate)
                        .toList();
            }
            default -> {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date dateGet = sdf.parse(date);
                    calendar.setTimeInMillis(dateGet.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                streamsPredicate = item -> GetDate(item.getDate()) == day
                        && GetMonth(item.getDate()) == month
                        && GetYear(item.getDate()) == year;
                return orderRes.stream()
                        .filter(streamsPredicate)
                        .toList();
            }
        }
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

    @Override
    public List<CustomerRes> getInfoCustomer() {
        List<CustomerRes> dataResult = new ArrayList<>();
        ORDER_REPO.findAll().forEach((e) -> {
            CustomerRes customerRes = convertToOtherData.getCustomerUse(e.getInfoOrder());
            dataResult.add(customerRes);
        });
        return dataResult;
    }

    @Override
    public List<CustomerRes> searchInfoByCode(String code, String state) {
        Predicate<CustomerRes> streamsPredicate = item -> item.getName().contains(code) || item.getCodeOrder().contains(code) || item.getPhone().contains(code);
        return getInfoByState(state).stream()
                .filter(streamsPredicate)
                .toList();
    }

    @Override
    public List<CustomerRes> getInfoByState(String state) {
        String code = state.replaceAll("-", " ").trim();
        if(code.equals("ALL")) {
            return getInfoCustomer();
        }
        if(code.equals("ĐANG CHUẨN BỊ")) {
            Predicate<CustomerRes> streamsPredicate = item -> !item.getState().equalsIgnoreCase("THÀNH CÔNG") && !item.getState().equalsIgnoreCase("TỪ CHỐI") && !item.getState().equalsIgnoreCase("THẤT BẠI");
            return getInfoCustomer().stream()
                    .filter(streamsPredicate)
                    .toList();
        }else if(code.equals("TỪ CHỐI")) {
            Predicate<CustomerRes> streamsPredicate = item -> item.getState().equalsIgnoreCase("TỪ CHỐI") || item.getState().equalsIgnoreCase("THẤT BẠI");
            return getInfoCustomer().stream()
                    .filter(streamsPredicate)
                    .toList();
        } else  {
            Predicate<CustomerRes> streamsPredicate = item -> item.getState().equalsIgnoreCase(code);
            return getInfoCustomer().stream()
                    .filter(streamsPredicate)
                    .toList();
        }
    }





    // add new order
    @Override
    public ResultDto save(InfoOrderDto infoOrderDto, User user, Cookie cookie) {

        List<CartUse> cartUses = cartSer.getAllDataCart(cookie.getValue());
        List<VoucherUse> voucherUses = cartSer.getVoucherUseCart(cookie.getValue());

        ResultDto resultDto = new ResultDto();
        resultDto.setTypeTransport(infoOrderDto.getTypeTransport());

        AtomicReference<Integer> sumPrice = new AtomicReference<>((Integer) 0);
        Orders orders = new Orders();

        // handle calculative sum price
        cartUses.forEach((e) -> {
            sumPrice.set((int) (sumPrice.get() + (e.getSumPrice())));
        });

        // handle voucher for order
        List<Voucher> vouchers = new ArrayList<>();
        voucherUses.forEach((e) -> {
            vouchers.add(voucherSer.getByIdR(e.getId()));
            if(user != null) {
                userVoucherSer.updateAfterUseByUserAndVoucher(user, voucherSer.getByIdR(e.getId()));
            }
            int sumGet = sumPrice.get();
            if(Objects.equals(e.getType(), "PERCENT")) {
                sumGet = (int) (sumGet - ( sumGet * e.getPercent()));
            }else  {
                sumGet = (int) (sumGet - e.getPrice());
            }
            sumPrice.set(sumGet);
        });
        orders.setVouchers(vouchers);


        // handle  price shift
        sumPrice.set((int) (sumPrice.get() + getPriceTransport(infoOrderDto.getTypeTransport())));



        String codeResult = generateRandomCode();
        resultDto.setCode(codeResult);

        // handle discount member
        if(user != null) {
            Double data = (double) ( (double) user.getMember().getPercentDiscountMember() /  (double)  100);
            Integer dataPrice = (int) (sumPrice.get() * data);
            sumPrice.set(sumPrice.get() - dataPrice);
            orders.setDiscountMember(dataPrice);
        }


        orders.setSumOrder(sumPrice.get());
        orders.setCodeOrder(codeResult);
        orders.setStateOrder("CHỜ");

        // handle save order first time
        Orders orders1 = ORDER_REPO.save(orders);


        // handle infoOder and detail order
        Set<OrderDetail> orderDetails = getDetailOrder(cartUses,orders1);
        InfoOrder infoOrder =  infoOrder(infoOrderDto);
        infoOrder.setOrders(orders1);
        if(orders.getUser() != null) {
            infoOrder.setEmailNotifyOrder(orders.getUser().getEmailUser());
        }
        orders1.setInfoOrder(infoOrder);
        orders1.setOrderDetails(orderDetails);
        if(user != null) {
            orders1.setUser(user);
        }

        // handle save order second time
        ORDER_REPO.save(orders1);
        socketSer.notifyNewOrder();

        cartSer.clean(cookie.getValue());
        cartSer.cleanVoucher(cookie.getValue());
        return resultDto;
    }













    @Override
    public boolean checkCode(String code) {
        if(ORDER_REPO.findByCodeOrder(code).isPresent()) {
            return  true;
        }else {
            return false;
        }
    }

    @Override
    public Orders getByCode(String code) {
        return ORDER_REPO.findByCodeOrder(code).orElseThrow();
    }

    @Override
    public DetailOrderUse getDetailByCode(String code) {
        Orders orders = ORDER_REPO.findByCodeOrder(code).orElseThrow();
        InfoOrder infoOrder = orders.getInfoOrder();
        DetailOrderUse detailOrderUse = new DetailOrderUse();
        detailOrderUse.setPhone(infoOrder.getPhoneOrder());
        detailOrderUse.setFirstName(infoOrder.getFirstName());
        detailOrderUse.setLastName(infoOrder.getLastNameOrder());
        detailOrderUse.setAddress(infoOrder.getAddressOrder());
        detailOrderUse.setTypeTrans(infoOrder.getTypeTransportOrder());
        detailOrderUse.setTypePay(infoOrder.getTypePaymentOrder());
        detailOrderUse.setSumPay(orders.getSumOrder());
        return detailOrderUse;
    }

    @Override
    public List<CartUse> getInfoByCode(String code) {
        List<CartUse> list = new ArrayList<>();
        Orders orders = ORDER_REPO.findByCodeOrder(code).orElseThrow();
        orders.getOrderDetails().forEach((e) -> {
            CartUse cartUse = new CartUse();
            cartUse.setQuantity(e.getQuantity());
            cartUse.setOptionUse(optionSer.getDetailById(e.getOptionSize().getOption().getId()));
            cartUse.setSumPrice(e.getPrice());
            cartUse.setOptionSizeUSe(convertToOtherData.getOptionSizeUse(e.getOptionSize()));
            list.add(cartUse);
        });
        return list;
    }

    @Override
    public List<VoucherUse> getVoucherUse(String code) {
        List<VoucherUse> voucherUses = new ArrayList<>();

        Orders orders = ORDER_REPO.findByCodeOrder(code).orElseThrow();


        Predicate<OrderDetail> streamsPredicate = item -> item.getOptionSize() != null ;
        List<OrderDetail> orderDetails = orders.getOrderDetails().stream()
                .filter(streamsPredicate)
                .toList();


        AtomicInteger price = new AtomicInteger();
        AtomicInteger priceDefect = new AtomicInteger();

        orderDetails.forEach((e) -> {
            if (e.getState()!= null) {
                int priceGet = (int) (productSer.getByIdEt(e.getOptionSize().getOption().getProduct().getIdProduct()).getPriceProduct() * e.getQuantityDefect());
                priceDefect.set(priceDefect.get() + priceGet);
            }
            if(e.getOptionSize() != null) {
                int priceGet = (int) (productSer.getByIdEt(e.getOptionSize().getOption().getProduct().getIdProduct()).getPriceProduct() * e.getQuantity());
                price.set(price.get() + priceGet);
            }
        });

        int SumTotal = price.get() - priceDefect.get();

        orders.getVouchers().forEach((e) -> {

            VoucherUse voucherUse = convertToOtherData.getVoucherUse(e);

            if(e.getPriceConditionVoucher() < SumTotal) {
                voucherUse.setState("Sử dụng");

                if(voucherUse.getType().equals("PRICE")) {
                    voucherUse.setPriceSubtract(voucherUse.getPrice());
                } else  {
                    int priceGet = (int) (SumTotal * ((e.getPercentDiscountVoucher())/100));
                    voucherUse.setPriceSubtract(priceGet);
                }
            } else  {
                voucherUse.setState("Không sử dụng");
                voucherUse.setPriceSubtract(0);
            }
            voucherUses.add(voucherUse);
        });


        return voucherUses;
    }
}
