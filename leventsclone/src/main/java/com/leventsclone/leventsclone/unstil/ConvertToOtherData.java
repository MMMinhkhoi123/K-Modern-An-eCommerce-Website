package com.leventsclone.leventsclone.unstil;

import com.leventsclone.leventsclone.data.dto.UserDto;
import com.leventsclone.leventsclone.data.response.CustomerRes;
import com.leventsclone.leventsclone.data.response.DirectoryRes;
import com.leventsclone.leventsclone.data.response.OrderRes;
import com.leventsclone.leventsclone.data.use.*;
import com.leventsclone.leventsclone.entity.*;
import com.leventsclone.leventsclone.entity.Collection;

import java.text.SimpleDateFormat;
import java.util.*;

public class ConvertToOtherData {


    public  OptionSizeUSe getOptionSizeUse(OptionSize optionSize)  {
        OptionSizeUSe optionSizeUSe = new OptionSizeUSe();
        optionSizeUSe.setId(optionSize.getId());
        optionSizeUSe.setQuantity(optionSize.getQuantity());
        optionSizeUSe.setSizeUse(getSizeUSe(optionSize.getSize()));
        return  optionSizeUSe;
    }


    public  OptionImageUse getOptionImageUse(OptionImage optionImage) {
     OptionImageUse imageUse = new OptionImageUse();
     imageUse.setId(optionImage.getId());
     imageUse.setIsActive(optionImage.getIsActive());
     imageUse.setImageUse(getImageUse(optionImage.getImage()));
     return  imageUse;
    }


    public ImageUse getImageUse(Image image) {
        ImageUse imageUse = new ImageUse();
        if(image == null) {
            return null;
        }
        imageUse.setCodeImage(image.getCode());
        imageUse.setLinkImage(image.getLink());
        imageUse.setIdImage(image.getId());
        return  imageUse;
    }



    public EventUse getEventUse(Event event) {
        EventUse eventUse = new EventUse();
        eventUse.setId(event.getIdEvent());
        eventUse.setName(event.getNameEvent());
        eventUse.setDateEnd(event.getDateStartEndEvent().getTime());
        eventUse.setDateStart(event.getDateStartEvent().getTime());
        eventUse.setPath(event.getPathEvent());
        eventUse.setState(event.getStateEvent());
        return  eventUse;
    }

    public Event getEvent(EventUse eventUse) {
        Event event = new Event();
        if(eventUse.getId() != null) {
            event.setIdEvent(eventUse.getId());
        }
        event.setNameEvent(eventUse.getName());
        event.setPathEvent(eventUse.getPath());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(eventUse.getDateStart());
        event.setDateStartEvent(calendar.getTime());

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTimeInMillis(eventUse.getDateEnd());
        event.setDateStartEndEvent(calendarEnd.getTime());
        event.setStateEvent(false);
        return  event;
    }


    public RatingUse getRatingUse(Rating rating) {
        RatingUse ratingUse = new RatingUse();
        ratingUse.setId(rating.getId());
        ratingUse.setTitle(rating.getTitle());
        ratingUse.setContent(rating.getComment());
        ratingUse.setCount(rating.getCount());
        ratingUse.setStateAssess(rating.getPermit());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(rating.getDateAssess().getTime());
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String date = dayOfMonth + "/"+ month + "/" + year;
        ratingUse.setDate(date);
        ratingUse.setImageUse(getImageUse(rating.getImage()));
        ratingUse.setNameAuth(rating.getUser().getNameUser());
        return ratingUse;
    }

    public Address getAddress(AddressUse addressUse) {
        Address address = new Address();
        if(addressUse.getId() != null) {
            address.setIdAddress(addressUse.getId());
        }
        address.setCityAddress(addressUse.getCity());
        address.setCommuneAddress(addressUse.getCommune());
        address.setDistrictAddress(addressUse.getDistrict());
        address.setPhoneAddress(addressUse.getPhone());
        address.setUsingAddress(addressUse.isUsingAddress());
        address.setDetailOverAddress(addressUse.getDetail());
        address.setLastNameAddress(addressUse.getLastName());
        address.setFirstNameAddress(addressUse.getFirstName());
        return  address;
    }

    public AddressUse getAddressUse(Address address) {
        AddressUse addressUse = new AddressUse();
        addressUse.setId(address.getIdAddress());
        addressUse.setCity(address.getCityAddress());
        addressUse.setLastName(address.getLastNameAddress());
        addressUse.setFirstName(address.getFirstNameAddress());
        addressUse.setPhone(address.getPhoneAddress());
        addressUse.setDistrict(address.getDistrictAddress());
        addressUse.setCommune(address.getCommuneAddress());
        addressUse.setUsingAddress(address.isUsingAddress());
        addressUse.setDetail(address.getDetailOverAddress());
        return  addressUse;
    }
    public OderUse getOrderUse(OrderDetail orderDetail) {
        OderUse oderUse = new OderUse();
        oderUse.setQuality(orderDetail.getQuantity());
        oderUse.setSizeUse( getSizeUSe(orderDetail.getOptionSize().getSize()));

//        if(orderDetail.getProductSizeColor() != null) {
//            oderUse.setPackaging(false);
//            oderUse.setSizeUse(getSizeUSe(orderDetail.getProductSizeColor().getSize()));
//            oderUse.setColor(getColorUse(orderDetail.getProductSizeColor().getColor()));
//            Product product = orderDetail.getProductSizeColor().getProduct();
////            oderUse.setNameProduct(product.getNameProduct());
////            oderUse.setIdProduct(product.getIdProduct());
//        }else  {
//            oderUse.setPackaging(true);
////            oderUse.setNameProduct("Dịch vụ gói quà");
//            oderUse.setNameImg("/image/packaging/Image_1d23fba1-9664-4c39-b93b-84e127b82583.webp");
//        }
        oderUse.setQuantityDefect(orderDetail.getQuantityDefect());
        oderUse.setDescribeDefect(orderDetail.getNote());
        oderUse.setPrice(orderDetail.getPrice());
        return  oderUse;
    }
    public OrderRes getOrderRes(Orders orders) {
        OrderRes orderRes = new OrderRes();
        orderRes.setCode(orders.getCodeOrder());
        orderRes.setState(orders.getStateOrder());
        orderRes.setTypePay(orders.getInfoOrder().getTypePaymentOrder());
        String name = orders.getInfoOrder().getFirstName() + " " + orders.getInfoOrder().getLastNameOrder();
        orderRes.setName(name);
        orderRes.setDate(orders.getCreatedDate().getTime());
        orderRes.setSumPrice(orders.getSumOrder());
        return  orderRes;
    }

    public CustomerRes getCustomerUse(InfoOrder infoOrder) {
        CustomerRes customerRes = new CustomerRes();
        customerRes.setName(infoOrder.getLastNameOrder() + " " + infoOrder.getFirstName());
        customerRes.setPhone(infoOrder.getPhoneOrder());
        customerRes.setPhoneExtra(infoOrder.getPhoneExtraOrder());
        customerRes.setAddress(infoOrder.getAddressOrder());
        customerRes.setCodeOrder(infoOrder.getOrders().getCodeOrder());
        customerRes.setPrice(infoOrder.getOrders().getSumOrder());
        customerRes.setState(infoOrder.getOrders().getStateOrder());
        return  customerRes;
    }

    public MemberUse getMemberUse(Member member) {
        MemberUse memberUse = new MemberUse();
        memberUse.setId(member.getIdMember());
        memberUse.setName(member.getNameMember());
        memberUse.setGrade(member.getGradeMember());
        memberUse.setPrice((int) member.getPriceAchieveMember());
        memberUse.setPercent(member.getPercentDiscountMember());
        Boolean check = !member.getUsers().isEmpty();
        memberUse.setUsing(check);
        return memberUse;
    }

    public VoucherUse getVoucherUse(Voucher voucher) {
    VoucherUse  voucherUse = new VoucherUse();
    voucherUse.setId(voucher.getIdVoucher());
    voucherUse.setName(voucher.getDescribeVoucher());
    voucherUse.setType(voucher.getTypeVoucher());
    voucherUse.setCode(voucher.getCodeVoucher());
    voucherUse.setPercent(voucher.getPercentDiscountVoucher());
    voucherUse.setPrice(voucher.getPriceDiscountVoucher());
    voucherUse.setPriceCondition(voucher.getPriceConditionVoucher());
    return  voucherUse;
    }

    public User getUser(UserDto userDto) {
        User user = new User();
        Date date = new Date();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
             date = formatter.parse(userDto.getDate());
        } catch (Exception ex) {
            System.out.println("Error valid");
        }
        user.setBirthdayUser(date);
        user.setPasswordUser(userDto.getPassword());
        user.setEmailUser(userDto.getEmail());
        user.setGenderUser(userDto.getGender());
        user.setPhoneUser(userDto.getPhone());
        user.setNameUser(userDto.getFullName());
        user.setGradeChangeRewardUser(0);
        user.setGradeSetMemberUser(0);
        return  user;
    }
    public OutfitUse getOutfit(Outfit outfit) {
        OutfitUse outfitUse = new OutfitUse();
        outfitUse.setHeight(outfit.getHeight());
        outfitUse.setWeight(outfit.getWeight());
        outfitUse.setId(outfit.getId());
        outfitUse.setOptionSizeUSe(getOptionSizeUse(outfit.getOptionSize()));
        List<ImageUse> imagesUses = new LinkedList<>();
        outfit.getImages().forEach(image -> {
            imagesUses.add(getImageUse(image));
        });
        outfitUse.setImagesUses(imagesUses);
        return  outfitUse;
    }
    public TypeProductUse getTypeProductUse(TypeProduct typeProduct) {
        TypeProductUse typeProductUse = new TypeProductUse();
        typeProductUse.setId(typeProduct.getIdTypeProduct());
        typeProductUse.setName(typeProduct.getNameTypeProduct());
        typeProductUse.setNameDirectory(typeProduct.getDirectory().getNameDirectory());
        typeProductUse.setKey(typeProduct.getKeyTypeProduct());
        return  typeProductUse;
    }

    public CollectionUse getCollectionUse(Collection collection) {
        CollectionUse dataResult = new CollectionUse();
        dataResult.setId(collection.getIdCollection());
        dataResult.setName(collection.getNameCollection());
        dataResult.setImg(collection.getImageCollection());
        dataResult.setDescribe(collection.getDescribeCollection());
        dataResult.setKeyImg(collection.getKeyCollection());
        dataResult.setDark(collection.isDarkColorCollection());
        List<ProductUse> productUses = new ArrayList<>();
        collection.getProducts().forEach((e) -> {
            productUses.add(getProductUse(e));
        });
        dataResult.setProducts(productUses);
        return  dataResult;
    }


    public SizeUse getSizeUSe(Size size) {
        SizeUse sizeUse = new SizeUse();
        sizeUse.setId(size.getIdSize());
        sizeUse.setName(size.getNameSize());
        sizeUse.setIdType(size.getTypeSize().getIdTypeSize());
        return  sizeUse;
    }
    public ColorUse getColorUse(Color color) {
        ColorUse colorUse = new ColorUse();
        colorUse.setId(color.getIdColor());
        colorUse.setName(color.getNameColor());
        colorUse.setCode(color.getCodeColor());
        return  colorUse;
    }

    public ProductUse getProductUse(Product product) {
        ProductUse productUse = new ProductUse();
        productUse.setDescribe(product.getDescribeProduct());
        productUse.setId(product.getIdProduct());

        List<ColorUse> colorUses = new LinkedList<>();
        product.getOptions().forEach(option -> {
            ColorUse colorUse = getColorUse(option.getColor());
            colorUse.setIdOpt(option.getId());
            colorUses.add(colorUse);
        });
        productUse.setColorUses(colorUses);

        if(product.getEvent() != null) {
            Long timeStart = product.getEvent().getEventEventProduct().getDateStartEvent().getTime();
            Long timeEnd = product.getEvent().getEventEventProduct().getDateStartEndEvent().getTime();
            Long timeToday = System.currentTimeMillis();
            if(timeStart < timeToday && timeEnd > timeToday) {
                double price = product.getPriceProduct() -  (product.getPriceProduct() * ((double)product.getEvent().getPercentDiscount() / (double)100));
                productUse.setPriceDiscount(price);
                productUse.setPercent(product.getEvent().getPercentDiscount());
                productUse.setIdEvent(product.getEvent().getEventEventProduct().getIdEvent());
            }else  {
                productUse.setPriceDiscount(0);
                productUse.setPercent(0);
            }
        }else  {
            productUse.setPriceDiscount(0);
            productUse.setPercent(0);
        }
        productUse.setName(product.getNameProduct());
        productUse.setPrice((int) product.getPriceProduct());
        productUse.setClassic(product.isClassicProduct());
        productUse.setTypeProductUse(getTypeProductUse(product.getTypeProduct()));
        return productUse;
    }


   public  DirectoryRes getDirectory(Directory directory) {
        DirectoryRes directoryRes = new DirectoryRes();
        directoryRes.setId(directory.getIdDirectory());
        directoryRes.setName(directory.getNameDirectory());
        directoryRes.setKey(directory.getKeyDirectory());
        Set<TypeProductUse> typeProductUse = new HashSet<>();
        directory.getTypeProducts().forEach((typeProduct) -> {
            typeProductUse.add(getTypeProductUse(typeProduct));
        });
        directoryRes.setTypeProductUses(typeProductUse);
        return directoryRes;
    }
}
