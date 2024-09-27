package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.data.response.InventoryRes;
import com.leventsclone.leventsclone.data.use.*;
import com.leventsclone.leventsclone.entity.Option;
import com.leventsclone.leventsclone.entity.Size;
import com.leventsclone.leventsclone.service.inter.ICart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

@Service
public class CartSer implements ICart {

    private final  OptionSer optionSer;
    private final SizeSer sizeSer;
    private  final  OptionSizeSer optionSizeSer;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 8;

    private Map<String, List<CartUse>> listCart = new HashMap<>();

    private Map<String, List<VoucherUse>> vouchers = new HashMap<>();

    @Autowired
    public CartSer(OptionSizeSer optionSizeSer,OptionSer optionSer, SizeSer sizeSer) {
        this.optionSer = optionSer;;
        this.sizeSer = sizeSer;
        this.optionSizeSer = optionSizeSer;
    }

    @Override
    public String getCodeCart() {
        String  code = generateRandomCode();
        while (listCart.containsKey(code)) {
            code = generateRandomCode();
        }
        return  code;
    }

    @Override
    public void deleteProductToCart(String codeCart,Long idOptionSize) {
        List<CartUse> list = new LinkedList<>(listCart.get(codeCart));
        list.removeIf((e) -> e.getOptionSizeUSe().getId().equals(idOptionSize));
        if(list.isEmpty()) {
            listCart.replace(codeCart, new ArrayList<>());
        } else  {
            listCart.replace(codeCart, list);
        }

    }

    @Override
    public void addProduct(String code, CartUse cartUse) {
        List<CartUse> cartUses = new ArrayList<>();
        cartUses.add(cartUse);
        if(listCart.get(code) != null) {
            cartUses.addAll(listCart.get(code));
        }
        listCart.put(code, cartUses);
    }

    @Override
    public void updateQuantity(String code, CartUse cartUse) {
        List<CartUse> dataOld = listCart.get(code);
        for (int i = 0; i<  dataOld.size(); i++) {
                if(Objects.equals(dataOld.get(i).getOptionSizeUSe().getId(), cartUse.getOptionSizeUSe().getId())) {
                    dataOld.get(i).setQuantity(cartUse.getQuantity());
                }

        }
        listCart.replace(code, dataOld);
    }

    @Override
    public boolean CheckContainsProduct(String code, CartUse cartUse) {
        AtomicBoolean status = new AtomicBoolean(false);
        if(listCart.get(code) == null) {
            return  false;
        } else  {
            List<CartUse> dataOld = listCart.get(code);
            dataOld.forEach(e -> {
                if(Objects.equals(e.getOptionSizeUSe().getId() , cartUse.getOptionSizeUSe().getId())){
                    status.set(true);
                }
            });
        }
        return status.get();
    }



    @Override
    public List<CartUse> getAllDataCart(String code) {
        if(listCart.get(code) == null ){
            return  new ArrayList<>();
        }
        return listCart.get(code);
    }

    @Override
    public List<VoucherUse> getVoucherUseCart(String code) {
        if(this.vouchers.get(code) == null) {
            return  new ArrayList<>();
        } else  {
            return this.vouchers.get(code);
        }
    }

    public CartUse setDataCart(Long idSize, Long idOption, int quantity) {

        Option option = optionSer.getEnById(idOption);
        Size size = sizeSer.getByIdET(idSize);
        OptionUse optionUse = optionSer.getDetailById(idOption);
        OptionSizeUSe optionSizeUSe = optionSizeSer.getByOptionAndSize(option, size);

        CartUse cartUse = new CartUse();
        cartUse.setQuantity(quantity);
        cartUse.setOptionUse(optionUse);
        cartUse.setOptionSizeUSe(optionSizeUSe);
        cartUse.setSumPrice(optionUse.getPriceCorrect() * quantity);

        return cartUse;
    }


    @Override
    public void addProductToCart(String codeCart, Long idOption, Long idSize, int quantity) {

        CartUse cartUse = setDataCart(idSize, idOption, quantity);

        if(CheckContainsProduct(codeCart, cartUse)) {
            System.out.println("update success");
            // update quantity
            updateQuantity(codeCart, cartUse);
        } else  {
            System.out.println("Add success");
            addProduct(codeCart, cartUse);
            // add cart
        }
    }

    @Override
    public void clean(String code) {
        listCart.replace(code, new ArrayList<>());
    }

    @Override
    public void cleanVoucher(String code) {
        vouchers.replace(code, new ArrayList<>());
    }

    @Override
    public boolean checkUseVoucher(VoucherUse voucherUse, String key) {
        if(vouchers.get(key) == null) {
            return  false;
        }else  {
         List<VoucherUse> voucherUses =  vouchers.get(key);

            Predicate<VoucherUse> streamsPredicate = item -> Objects.equals(item.getCode(), voucherUse.getCode());
            List<VoucherUse> resultNew  = voucherUses.stream()
                    .filter(streamsPredicate)
                    .toList();
            if(resultNew.isEmpty()) {
                return  true;
            }else  {
                return  false;
            }
        }
    }

//    @Override
//    public void addPackaging(String code) {
//        List<OderUse> oderUseList = new ArrayList<>();
//        List<OderUse> productUses = listCart.get(code);
//
//        Predicate<OderUse> streamsPredicate = item -> !item.isPackaging();
//        oderUseList = productUses.stream()
//                .filter(streamsPredicate)
//                .toList();
//        listCart.replace(code, oderUseList);
//
//        OderUse oderUse = new OderUse();
//        oderUse.setPackaging(true);
//        oderUse.setPrice(190000);
//        oderUse.setQuality(1);
//        oderUse.setSizeUse(new SizeUse());
//        oderUse.setNameImg("/image/packaging/Image_1d23fba1-9664-4c39-b93b-84e127b82583.webp");
//        addProduct(code, oderUse);
//    }

    @Override
    public void addVoucher(VoucherUse voucherUse, String code) {
        if(this.vouchers.get(code) == null) {
            List<VoucherUse> voucherUses = new ArrayList<>();
            voucherUses.add(voucherUse);
            this.vouchers.put(code, voucherUses);
            System.out.println("new voucher key");
        } else  {
            List<VoucherUse> listOld = new ArrayList<>(this.vouchers.get(code));
            listOld.add(voucherUse);
            vouchers.put(code, listOld);
        }
    }


    @Override
    public void deleteVoucher(String code, String keyCard) {
        if(vouchers.get(keyCard) != null) {
            Predicate<VoucherUse> oderUsePredicate = (e) -> !Objects.equals(e.getCode(), code);
            List<VoucherUse> list = vouchers.get(keyCard).stream().filter(oderUsePredicate).toList();
            vouchers.replace(keyCard, list);
        }
    }


    @Override
    public void createOder(String valueCookie) {
        listCart.put(valueCookie, new ArrayList<>());
    }

    @Override
    public void updateQuantityProduct(Long idOptionSize, String code, int quantity) {
        List<CartUse> listReplace =  new ArrayList<>(listCart.get(code));
           for (int i = 0 ; i < listReplace.size() ; i++) {
                   if(Objects.equals(listReplace.get(i).getOptionSizeUSe().getId(), idOptionSize)) {
                       listReplace.get(i).setQuantity(quantity);
                   }

           }
       listCart.replace(code, listReplace);
    }

    @Override
    public List<InventoryRes> checkInventory(String code) {
        List<InventoryRes> inventoryRes = new ArrayList<>();
        List<CartUse> listReplace =  new ArrayList<>(listCart.get(code));

        listReplace.forEach((e) -> {
                Predicate<OptionSizeUSe> streamsPredicate = item -> item.getSizeUse().getId() == e.getOptionSizeUSe().getSizeUse().getId() ;
                OptionSizeUSe sizeUses = e.getOptionUse().getOptionSizeUSes().stream().filter(streamsPredicate).toList().get(0);
                if(sizeUses.getQuantity() < e.getQuantity()) {
                    inventoryRes.add(getInventory(e, sizeUses.getQuantity()));
                    updateQuantityProduct(e.getOptionSizeUSe().getId(), code, sizeUses.getQuantity());
                }
        });
        return inventoryRes;
    }


    private InventoryRes getInventory(CartUse cartUse, int quantityStock) {
        InventoryRes inventoryRes = new InventoryRes();
        inventoryRes.setOptionUse(cartUse.getOptionUse());
        inventoryRes.setImg(cartUse.getOptionUse().getOptionImageUses().stream().toList().get(0).getImageUse().getLinkImage());
        inventoryRes.setQuantityOld(cartUse.getQuantity());
        inventoryRes.setQuantity(quantityStock);
        return  inventoryRes;
    }

    @Override
    public String generateRandomCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        Random random = new Random();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }


    public int getTotalProducts(String code) {
        List<CartUse> listReplace =  new ArrayList<>(listCart.get(code));
        AtomicInteger data = new AtomicInteger();
        listReplace.forEach(cartUse -> {
            data.set(data.get() + cartUse.getSumPrice());
        });
        return  data.get();
    }

    public  int getTotalOld(String code, int price) {
        List<VoucherUse> voucherUses = getVoucherUseCart(code);
        AtomicInteger priceVoucherSubtract = new AtomicInteger();
        voucherUses.forEach((e) -> {
            if(Objects.equals(e.getType(), "PERCENT")) {
                int priceGet = (int) (getTotalProducts(code) * (e.getPercent() / 100));
                priceVoucherSubtract.set((int) (priceVoucherSubtract.get() + priceGet));
            }else  {
                priceVoucherSubtract.set((int) (priceVoucherSubtract.get() + e.getPrice()));
            }
        });
        return (getTotalProducts(code) + price) - priceVoucherSubtract.get();
    }
}
