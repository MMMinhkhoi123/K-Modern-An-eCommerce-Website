package com.leventsclone.leventsclone.controller.admin.productSale;

import com.leventsclone.leventsclone.controller.common.AuthenticatedData;
import com.leventsclone.leventsclone.data.use.*;
import com.leventsclone.leventsclone.entity.Option;
import com.leventsclone.leventsclone.entity.Product;
import com.leventsclone.leventsclone.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

@Controller
@RequestMapping("/admin/products")
public class ProductsAssembleController {
    private final OptionSer optionSer;
    private final ColorSer colorSer;
    private final SizeSer sizeSer;
    private final ProductSer productSer;

    private final CloudinarySer cloudinarySer;

    private final ImageSer imageSer;

    private final OptionImageSer optionImageSer;

    private final OptionSizeSer optionSizeSer;

    private final AuthenticatedData auth;

    @Autowired
    public ProductsAssembleController(
            OptionSizeSer optionSizeSer,
            ColorSer colorSer,
            SizeSer sizeSer,
            ProductSer productSer,
            AuthenticatedData auth,
            CloudinarySer cloudinarySer,
            ImageSer imageSer,
            OptionImageSer optionImageSer,
            OptionSer optionSer) {
        this.optionSer = optionSer;
        this.productSer = productSer;
        this.colorSer = colorSer;
        this.sizeSer = sizeSer;
        this.auth = auth;
        this.optionSizeSer = optionSizeSer;
        this.cloudinarySer = cloudinarySer;
        this.imageSer = imageSer;
        this.optionImageSer = optionImageSer;
    }


    private void handleAttribute(Model model,
                                 List<SizeUse> sizeUses,
                                 List<ColorUse> colorUses,
                                 List<ProductUse> productUses,
                                 OptionUse optionUse,
                                 String type,
                                 String action,
                                 List<OptionUse> optionUses) {
        model.addAttribute("type",type);
        model.addAttribute("action",action);
        model.addAttribute("option", optionUse);
        model.addAttribute("options", optionUses);
        model.addAttribute("products", productUses);
        model.addAttribute("colors", colorUses);
        model.addAttribute("sizes", sizeUses);
    }


    @GetMapping("")
    public String getPageProducts(Model model,
                                  @RequestParam("action") Optional<String> actionPr
            ,@RequestParam("update") Optional<Long> update
            ,@RequestParam("detail") Optional<Long> detail,
                                  @RequestParam("actionSize") Optional<Long> addSize) {
        auth.authentication(model);
        String action = "screen";
        String type = null;
        List<ColorUse> colorUses = new ArrayList<>();
        List<ProductUse> productUses = new ArrayList<>();
        List<SizeUse> sizeUses = new LinkedList<>();
        OptionUse optionUse = null;

        List<OptionUse> optionUses = optionSer.getAll();
       if(actionPr.isPresent()) {
           action = "edit";
           type = "add";
           productUses = productSer.getAll();
       }
       if(update.isPresent()) {
           action = "edit";
           type = "update";
           optionUse = optionSer.getDetailById(update.orElseThrow());
           productUses = productSer.getAll();
           colorUses = colorSer.getAll();
       }

       if(detail.isPresent() && addSize.isEmpty()) {
           action = "detail";
           type = "detail";
           optionUse = optionSer.getDetailById(detail.orElseThrow());
       }

       if(addSize.isPresent()) {
           action = "AddSize";
           type = "detail";
           optionUse = optionSer.getDetailById(addSize.orElseThrow());
           OptionUse optionUse1 = optionSer.getDetailById(addSize.orElseThrow());

           List<SizeUse> list = sizeSer.getByNameTypeSize(optionUse1.getProductUse().getTypeProductUse().getNameDirectory());
           Predicate<SizeUse> sizeUsePredicate = sizeUse -> {
               Predicate<OptionSizeUSe>optionUsePredicate = optionUse2 -> optionUse2.getSizeUse().getId() == sizeUse.getId();
               List<OptionSizeUSe> optionSizeUSes = optionUse1.getOptionSizeUSes().stream().filter(optionUsePredicate).toList();
               return optionSizeUSes.isEmpty();
           };
           sizeUses = list.stream().filter(sizeUsePredicate).toList();
       }


       handleAttribute(model,
               sizeUses,
               colorUses,
               productUses,
               optionUse,
               type,
                action,
               optionUses
        );
        return  "admin/PageAdmin/productSale/products";
    }


    @PostMapping("/save")
    public String addProducts(Model model,
                              @RequestParam("idProduct") Optional<Long> idProduct,
                              @RequestParam("price") Optional<Integer> price,
                              @RequestParam("idColor") Optional<Long> idColor,
                              @RequestParam("images") Optional<List<String>> image) {
        auth.authentication(model);
        String images = null;

        if(image.orElseThrow().isEmpty()) {
            images = "Sản phẩm chưa được thêm hình ảnh ";
        }
        if (images == null) {
            model.addAttribute("success", "Thêm sản phẩm bán thành công");
            optionSer.save(idProduct.orElseThrow(), idColor.orElseThrow(),image.orElseThrow(), price.orElseThrow());
        }
        model.addAttribute("errorImages", images);
        List<OptionUse> optionUses = optionSer.getAll();
//        sort(productSizeColorUSes);


        handleAttribute(model,
                null,
                colorSer.getAll(),
                productSer.getAll(),
                null,
                "add",
                "edit",
                optionUses
        );
        return  "admin/PageAdmin/productSale/products :: #screen__add";
    }

    @GetMapping("/detail/{data}")
    public String getDetail(@PathVariable(value = "data") Optional<Long> idOption, Model model) {
        auth.authentication(model);
        OptionUse optionUse = optionSer.getDetailById(idOption.orElseThrow());
        handleAttribute(model,
                null,
                null,
                null,
                optionUse,
                "detail",
                "detail",
                new LinkedList<>()
        );
        return  "admin/PageAdmin/productSale/products :: #admin__screen";
    }

    @PostMapping("/addSize-save")
    public  String addSizeProducts(Model model, @RequestParam("idOpt") Optional<Long> idOption,  @RequestParam("idSize") Optional<Long> idSize,@RequestParam("quantity")  Optional<Integer> quantity){
        auth.authentication(model);
        Option option = optionSer.getEnById(idOption.orElseThrow());
        optionSizeSer.save(option, idSize.orElseThrow(), quantity.orElseThrow());
        OptionUse optionUse = optionSer.getDetailById(idOption.orElseThrow());
        handleAttribute(model,
                null,
                null,
                null,
                optionUse,
                "detail",
                "detail",
                new LinkedList<>()
        );
        return  "admin/PageAdmin/productSale/products :: #admin__screen";
    }

    @PutMapping("/update")
    public String updateProducts(Model model,
                                 @RequestParam("idProduct") Optional<Long> idProduct,
                                 @RequestParam("price") Optional<Integer> price,
                                 @RequestParam("idColor") Optional<Long> idColor,
                                 @RequestParam("images") Optional<List<String>> image
    ) {
        auth.authentication(model);

        String images = null;

        if(image.orElseThrow().isEmpty()) {
            images = "Sản phẩm chưa được thêm hình ảnh ";
        }
        if (images == null) {
            model.addAttribute("success", "Cập nhật phẩm bán thành công");
            optionSer.update(idProduct.orElseThrow(), idColor.orElseThrow(), image.orElseThrow(),price.orElseThrow());
        }
        model.addAttribute("errorImages", images);

        OptionUse  optionUse = optionSer.getByProductAndColor(idProduct.orElseThrow(), idColor.orElseThrow());

        handleAttribute(model,
                null,
                colorSer.getAll(),
                productSer.getAll(),
                optionUse,
                "update",
                "edit",
                new LinkedList<>()
        );

        return  "admin/PageAdmin/productSale/products :: #screen__add";
    }



    @GetMapping("/color-by-product/{id}")
    public String getListSize(Model model, @PathVariable(value = "id") Optional<Long> idProduct) {
        auth.authentication(model);
         Product product =  productSer.getByIdEt(idProduct.orElseThrow());
         List<ColorUse> colors = colorSer.getAll();

        Predicate<ColorUse> colorUsePredicate = colorUse -> {
            Predicate<Option> optionUsePredicate = x -> Objects.equals(x.getColor().getIdColor(), colorUse.getId());

            List<Option> optionUses = product.getOptions().stream().filter(optionUsePredicate).toList();

            return optionUses.isEmpty();

        };
         List<ColorUse> optionUses1 = colors.stream().filter(colorUsePredicate).toList();

        handleAttribute(model,
                null,
                optionUses1,
                null,
                null,
                null,
                null,
                null
        );
        return  "admin/fragmentsAdmin/productsSale/products/addproducts :: #colorActive";
    }

    @GetMapping("/open-from-add")
    public String openFromAdd(Model model) {
        auth.authentication(model);
        List<OptionUse> productSizeColorUSes = optionSer.getAll();
        handleAttribute(model,
                null,
                new LinkedList<>(),
                productSer.getAll(),
                null,
                "add",
                "edit",
                productSizeColorUSes

        );
        return  "admin/PageAdmin/productSale/products :: #admin__screen";
    }

    @GetMapping("/open-from-add-size/{id}")
    public String openFromAddSize(Model model, @PathVariable(value = "id")Optional<Long> idOpt) {
        auth.authentication(model);
        OptionUse optionUse = optionSer.getDetailById(idOpt.orElseThrow());
        List<OptionUse> productSizeColorUSes = optionSer.getAll();

        List<SizeUse> list = sizeSer.getByNameTypeSize(optionUse.getProductUse().getTypeProductUse().getNameDirectory());
        Predicate<SizeUse> sizeUsePredicate = sizeUse -> {
            Predicate<OptionSizeUSe>optionUsePredicate = optionUse2 -> optionUse2.getSizeUse().getId() == sizeUse.getId();
            List<OptionSizeUSe> optionSizeUSes = optionUse.getOptionSizeUSes().stream().filter(optionUsePredicate).toList();
            return optionSizeUSes.isEmpty();
        };
        List<SizeUse> sizeUses = list.stream().filter(sizeUsePredicate).toList();

        handleAttribute(model,
                sizeUses,
                new LinkedList<>(),
                productSer.getAll(),
                optionUse,
                "detail",
                "AddSize",
                productSizeColorUSes
        );
        return  "admin/PageAdmin/productSale/products :: #admin__screen";
    }



    @GetMapping("/open-from-update/{id}")
    public String openFromAddUpdate(@PathVariable(value = "id") Optional<Long> idOption,Model model) {
        auth.authentication(model);

        OptionUse optionUse = optionSer.getDetailById(idOption.orElseThrow());

        handleAttribute(model,
                    null,
                    colorSer.getAll(),
                    productSer.getAll(),
                    optionUse,
                    "update",
                    "edit",
                    new LinkedList<>()
            );

        return  "admin/PageAdmin/productSale/products :: #admin__screen";
    }




    @DeleteMapping("/delete")
    public String Delete(@RequestBody Optional<List<Long>> list,
                         Model model) {

        auth.authentication(model);

        List<String> listImg = new ArrayList<>();


        list.orElseThrow().forEach((e) -> {

                Option option = optionSer.getEnById(e);
                optionImageSer.getAllByOption(option).forEach(optionImage -> {
                    listImg.add(optionImage.getImageUse().getCodeImage());
                    optionImageSer.delete(optionImage.getId());
                    imageSer.deleteById(optionImage.getImageUse().getIdImage());
                });
                optionSer.delete(option.getId());
        });

        if(!listImg.isEmpty()) {
            cloudinarySer.delete(listImg);
        }
        List<OptionUse> optionUses = optionSer.getAll();

        handleAttribute(model,
                null,
                null,
                null,
                null,
                "screen",
                "screen",
                optionUses
        );
        return  "admin/PageAdmin/productSale/products :: #admin__screen";
    }

    @DeleteMapping("/delete-option-size/{idOpt}")
    public String DeleteDetail(@RequestBody Optional<List<Long>> listIdOptionSize,@PathVariable("idOpt") Optional<Long> idOpt,
                         Model model) {

        auth.authentication(model);

        listIdOptionSize.orElseThrow().forEach(optionSizeSer::delete);
        OptionUse optionUse = optionSer.getDetailById(idOpt.orElseThrow());
        handleAttribute(model,
                null,
                null,
                null,
                optionUse,
              "detail",
                "detail",
                new LinkedList<>()
        );
        return  "admin/PageAdmin/productSale/products :: #admin__screen";
    }
}
