package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.response.InventoryRes;
import com.leventsclone.leventsclone.data.use.CartUse;
import com.leventsclone.leventsclone.data.use.OderUse;
import com.leventsclone.leventsclone.data.use.VoucherUse;

import java.util.List;

public interface ICart {
    String generateRandomCode();
    String getCodeCart();

    void  deleteProductToCart(String codeCart, Long idOptionSize);

    void  addProduct(String code, CartUse cartUse);

    void  updateQuantity(String code, CartUse cartUse);

    boolean CheckContainsProduct(String code, CartUse cartUse);

    List<CartUse> getAllDataCart(String code);
    List<VoucherUse> getVoucherUseCart(String code);

    void  addProductToCart(String codeCart, Long idOption, Long idSize, int quantity);

    void clean(String code);

    void cleanVoucher(String code);

    boolean checkUseVoucher(VoucherUse voucherUse, String key);

    void addVoucher(VoucherUse voucherUse, String code);

    void deleteVoucher(String code, String keyCard);
    void  createOder(String valueCookie);

    void updateQuantityProduct(Long idOptionSize, String code, int quantity);

    List<InventoryRes> checkInventory(String code);

}
