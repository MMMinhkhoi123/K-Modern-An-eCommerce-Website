package com.leventsclone.leventsclone.controller.admin.productSale;

import com.leventsclone.leventsclone.controller.common.AuthenticatedData;
import com.leventsclone.leventsclone.custome.UploadProperties;
import com.leventsclone.leventsclone.data.use.*;
import com.leventsclone.leventsclone.service.impl.CollectionSer;
import com.leventsclone.leventsclone.service.impl.ProductSer;
import com.leventsclone.leventsclone.service.impl.UploadSer;
import com.leventsclone.leventsclone.service.impl.UserSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/collections")
public class CollectionController {


    private final UploadProperties uploadProperties;
    private final CollectionSer collectionSer;

    private  final ProductSer productSer;

    private final UploadSer uploadSer;

    private final AuthenticatedData auth;


    @Autowired
    public CollectionController(UploadProperties uploadProperties,CollectionSer collectionSer,UploadSer uploadSer,ProductSer productSer, AuthenticatedData auth) {
        this.collectionSer = collectionSer;
        this.productSer = productSer;
        this.uploadSer = uploadSer;
        this.uploadProperties = uploadProperties;
        this.auth = auth;
    }



    private void handleAttribute(Model model,
                                 List<ProductUse> productUses,
                                 CollectionUse collectionUse,
                                 String type,
                                 String action,
                                 List<CollectionUse> collectionUses) {
        model.addAttribute("type", type);
        model.addAttribute("action", action);
        model.addAttribute("collection", collectionUse);
        model.addAttribute("collections", collectionUses);
        model.addAttribute("products", productUses);
    }




    @GetMapping(value = "")
    public String getPageHome(Model model,
                              @RequestParam("update") Optional<String> updatePr,
                              @RequestParam("action")Optional<String> actionPr) {
        auth.authentication(model);
        List<CollectionUse> collectionUses = collectionSer.getAll();
        CollectionUse collectionUse = new CollectionUse();
        List<ProductUse> productUses = new ArrayList<>();
        String action = "screen";
        String type = null;

        if(actionPr.isPresent()) {
            type = "add";
            action = "edit";
            productUses = productSer.getAll();
            model.addAttribute("imagesUpload", uploadSer.getAll());
        }
        if(updatePr.isPresent()) {
            type = "update";
            action = "edit";
            productUses = productSer.getAll();
            collectionUse = collectionSer.getById(Long.parseLong(updatePr.orElseThrow()));
            model.addAttribute("imagesUpload", uploadSer.getAll());
        }

        handleAttribute(model,
                 productUses,
                 collectionUse,
                 type,
                 action,
                collectionUses);
        return  "admin/PageAdmin/productSale/collections";
    }


    @PutMapping("/update")
    public String update(
            Model model,
            @RequestParam("id") Optional<String> id,
            @RequestParam("file") Optional<MultipartFile> file,
            @RequestParam("name") Optional<String> name,
            @RequestParam("product") Optional<List<String>> products,
            @RequestParam("dark") Optional<Boolean> dark,
            @RequestParam("describe") Optional<String> describe
    ) throws IOException {
        auth.authentication(model);
        String errorProduct = null;
        CollectionUse collectionUseOld = collectionSer.getById(Long.valueOf(id.orElseThrow()));
        CollectionUse collectionUse = new CollectionUse();
        collectionUse.setId(Long.parseLong(id.orElseThrow()));
        collectionUse.setDark(dark.orElseThrow());
        collectionUse.setName(name.orElseThrow());
        collectionUse.setDescribe(describe.orElseThrow());
        if(products.isEmpty()) {
            errorProduct = "Bạn chưa chọn sản phẩm";
        }else {
            List<ProductUse> productUses = new ArrayList<>();
            products.orElseThrow().forEach((e) -> {
                ProductUse productUse = productSer.getById(Long.parseLong(e));
                productUses.add(productUse);
            });
            collectionUse.setProducts(productUses);
        }
        if(errorProduct == null) {
                collectionSer.update(id.orElseThrow()
                        ,name.orElseThrow(),file,
                        dark.orElseThrow()
                        ,products.orElseThrow(),describe.orElseThrow());

            model.addAttribute("success", "Cập nhật bộ siêu tập thành công");
            collectionUse = collectionSer.getById(Long.valueOf(id.orElseThrow()));
        } else  {
            if(file.isPresent()) {
                File file1 = new File("");
                String path = file1.getAbsolutePath() + uploadProperties.getCommon() + file.orElseThrow().getOriginalFilename();
                Path paths  = Paths.get(path);
                collectionUse.setImgAdd(file.orElseThrow().getOriginalFilename());
                Files.copy(file.orElseThrow().getInputStream(),paths, StandardCopyOption.REPLACE_EXISTING);
                collectionUse.setImgAdd(file.orElseThrow().getOriginalFilename());
            } else  {
                collectionUse.setImg(collectionUseOld.getImg());
            }
        }
        List<CollectionUse> collectionUses = collectionSer.getAll();
        handleAttribute(model,
                productSer.getAll(),
                collectionUse,
                "update",
                "edit",
                collectionUses
        );
        model.addAttribute("errorProduct",errorProduct);
        model.addAttribute("imagesUpload", uploadSer.getAll());
        return  "admin/PageAdmin/productSale/collections :: #admin__screen";
    }

    @PostMapping("/save")
    public String save(Model model,
                       @RequestParam("file") Optional<MultipartFile> file,
                       @RequestParam("name") Optional<String> name,
                       @RequestParam("product") Optional<List<String>> products,
                       @RequestParam("dark") Optional<Boolean> dark,
                       @RequestParam("describe") Optional<String> describe) throws IOException {
        auth.authentication(model);
        String errorProduct = null;
        String errorImg = null;
        CollectionUse collectionUse = new CollectionUse();
        collectionUse.setDark(dark.orElseThrow());
        collectionUse.setName(name.orElseThrow());
        collectionUse.setDescribe(describe.orElseThrow());
        if(products.isEmpty()) {
            errorProduct = "Bạn chưa chọn sản phẩm";
        }else {
            List<ProductUse> productUses = new ArrayList<>();
            products.orElseThrow().forEach((e) -> {
                ProductUse productUse = productSer.getById(Long.parseLong(e));
                productUses.add(productUse);
            });
            collectionUse.setProducts(productUses);
        }
        if(file.isEmpty()) {
            errorImg = "Bạn chưa chọn ảnh chủ đề";
        } else  {
            File file1 = new File("");
            String path = file1.getAbsolutePath() + uploadProperties.getCommon() + file.orElseThrow().getOriginalFilename();
            Path paths  = Paths.get(path);
            collectionUse.setImgAdd(file.orElseThrow().getOriginalFilename());
            Files.copy(file.orElseThrow().getInputStream(),paths, StandardCopyOption.REPLACE_EXISTING);
        }
        if(errorImg == null && errorProduct == null) {
            collectionSer.save(name.orElseThrow(), file.orElseThrow(), dark.orElseThrow(), products.orElseThrow(),describe.orElseThrow());
            model.addAttribute("success", "Tạo bộ siêu tập thành công");
        }
        model.addAttribute("errorProduct",errorProduct);
        model.addAttribute("errorImg",errorImg);
        model.addAttribute("imagesUpload", uploadSer.getAll());
        handleAttribute(model,
                productSer.getAll(),
                collectionUse,
                "add",
                "edit",
                collectionSer.getAll()
        );
        return  "admin/PageAdmin/productSale/collections :: #admin__screen";
    }


    @GetMapping(value = "/open-from-create")
    public String openFromCreate(Model model) {
        auth.authentication(model);
        model.addAttribute("imagesUpload", uploadSer.getAll());
        handleAttribute(model,
                productSer.getAll(),
                new CollectionUse(),
                "add",
                "edit",
                collectionSer.getAll()
                );
        return  "admin/PageAdmin/productSale/collections :: #admin__screen";
    }

    @GetMapping(value = "/open-form-update/{id}")
    public String openFromUpdate(Model model, @PathVariable(value = "id") Optional<Long> id) {
        auth.authentication(model);
        model.addAttribute("imagesUpload", uploadSer.getAll());
        CollectionUse collectionUse = collectionSer.getById(id.orElseThrow());
        handleAttribute(model,
                productSer.getAll(),
                collectionUse,
                "update",
                "edit",
                collectionSer.getAll()
        );
        return  "admin/PageAdmin/productSale/collections :: #admin__screen";
    }

    @DeleteMapping("/delete")
    public String delete(Model model, @RequestBody Optional<List<String>> list) {
        auth.authentication(model);
        collectionSer.delete(list.orElseThrow());
        handleAttribute(model,
                null,
                new CollectionUse(),
                "delete",
                "screen",
                collectionSer.getAll()
        );
        return  "admin/fragmentsAdmin/productsSale/collections/collection :: collection";
    }

}
