package com.leventsclone.leventsclone.controller.user.Views;

import com.leventsclone.leventsclone.controller.user.fragment.CollectionsFragmentController;
import com.leventsclone.leventsclone.data.use.*;
import com.leventsclone.leventsclone.entity.Event;
import com.leventsclone.leventsclone.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/collections")
public class ProductsController {
    private final ColorSer colorSer;
    private final SizeSer sizeSer;
    private final TypeProductSer typeProductSer;
    private List<ProductSizeColorUSe> data ;
    private List<OptionUse> optionUses ;
    private final CollectionSer collectionSer;

    private final EventSer eventSer;

    private int  thisPage = 1;

    private  final  UserSer userSer;
    private  final MemberSer memberSer;

    private final OptionSer optionSer;

    @Autowired
    public ProductsController(
            OptionSer optionSer, ColorSer colorSer, SizeSer sizeSer,
                              TypeProductSer typeProductSer, CollectionSer collectionSer,
                              EventSer eventSer,
                              UserSer userSer,
                              MemberSer memberSer
    ) {
        this.optionSer = optionSer;
        this.colorSer = colorSer;
        this.sizeSer = sizeSer;
        this.typeProductSer = typeProductSer;
        this.collectionSer = collectionSer;
        this.eventSer = eventSer;
        this.userSer = userSer;
        this.memberSer = memberSer;
    }

    public void  authentication(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            AuthenticatedUse user = userSer.getByUserName(authentication.getName());
            model.addAttribute("authenticated", user );
            model.addAttribute("members", memberSer.getAll());
        }
    }


    @GetMapping(value = "/search")
    public String getDataSearch(
            @RequestParam(value = "q")Optional<String> text,
            @PathVariable(value = "name") Optional<String> name,
            @RequestParam(value = "color")Optional<String> color,
            @RequestParam(value = "size") Optional<String> size,
            @RequestParam(value = "price") Optional <String> price,
            @RequestParam(value = "page") Optional <String> page,
            @RequestParam(value = "sort") Optional <String> sort,
            Model model) {

        authentication(model);
        optionUses = optionSer.getProductSuccessByText(text.orElseThrow(), false).getOptionUses();

        if (!data.isEmpty()) {
            model.addAttribute("colorFilter", colorSer.getColorFilter(optionUses));
            model.addAttribute("sizeFilter", sizeSer.getSizeFilter(optionUses));
            model.addAttribute("priceFilter", optionSer.getPriceFilter(optionUses));
        }else  {
            model.addAttribute("colorFilter", null);
            model.addAttribute("sizeFilter", null);
            model.addAttribute("priceFilter",null);
        }
        if(sort.isPresent()) {
            model.addAttribute("sortSet", sort.orElseThrow());
        }
        thisPage = optionSer.handleFilterStatus(color,size, price,page,sort, optionUses);
        optionUses = optionSer.dataArray;
        model.addAttribute("url","search?q="+text.orElseThrow());
        model.addAttribute("namePage"," Tìm kiếm: đã tìm thấy 0 kết quả cho \" " + text.orElseThrow() +"\"");
        model.addAttribute("options", optionSer.handlePagination(optionUses, thisPage));
        model.addAttribute("dataCart", new ArrayList<>());

        CollectionsFragmentController.handleFilter(color, price, size, model);
        return "user/Page/Products";
    }



    @GetMapping(value = "/{name}")
    public String getDataByType(
            @PathVariable(value = "name") Optional<String> name,
            @RequestParam(value = "color")Optional<String> color,
            @RequestParam(value = "size") Optional<String> size,
            @RequestParam(value = "price") Optional <String> price,
            @RequestParam(value = "page") Optional <String> page,
            @RequestParam(value = "sort") Optional <String> sort,
            Model model
    ) {
        authentication(model);
        if (collectionSer.checkContainCollection(name.orElseThrow())) {
            model.addAttribute("products", collectionSer.getAdequate(name.orElseThrow()));
            CollectionUse collectionUse =  collectionSer.getByKey(name.orElseThrow());
            model.addAttribute("active", collectionUse.getName());
            Set<OderUse> oderUses = new HashSet<>();
            model.addAttribute("dataCart", oderUses);
           return  "user/Page/DetailCollection";
        }


        if(sort.isPresent()) {
            model.addAttribute("sortSet", sort.orElseThrow());
        }
        optionUses = optionSer.getBestSeller();

        if(eventSer.getByPath(name.orElseThrow()).isPresent()) {
            Event event = eventSer.getByPath(name.orElseThrow()).orElseThrow();
            optionUses = optionSer.getAllByEvent(event);
        }else  {
            optionUses = optionSer.getAllByTypeProduct(name.orElseThrow());
        }

        if (!optionUses.isEmpty()) {
            model.addAttribute("colorFilter", colorSer.getColorFilter(optionUses));
            model.addAttribute("sizeFilter", sizeSer.getSizeFilter(optionUses));
            model.addAttribute("priceFilter", optionSer.getPriceFilter(optionUses));
        }else  {
            model.addAttribute("colorFilter", null);
            model.addAttribute("sizeFilter", null);
            model.addAttribute("priceFilter", null);
        }
        thisPage = optionSer.handleFilterStatus(color,size, price,page,sort, optionUses);
        optionUses = optionSer.dataArray;

        if(eventSer.getByPath(name.orElseThrow()).isPresent()) {
            Event event = eventSer.getByPath(name.orElseThrow()).orElseThrow();
            model.addAttribute("url", event.getPathEvent());
            model.addAttribute("namePage",event.getNameEvent());
        }else  {
            model.addAttribute("url", name.orElseThrow());
            model.addAttribute("namePage",typeProductSer.getByName(name.orElseThrow()).getNameTypeProduct());
        }
        model.addAttribute("options", optionSer.handlePagination(optionUses, thisPage));
        model.addAttribute("dataCart", new ArrayList<>());

        CollectionsFragmentController.handleFilter(color, price, size, model);
        return "user/Page/Products";
    }




    @GetMapping(value = "/best-seller")
    public String getDataBestSeller(
            @RequestParam(value = "color")Optional<String> color,
            @RequestParam(value = "size") Optional<String> size,
            @RequestParam(value = "price") Optional <String> price,
            @RequestParam(value = "page") Optional <String> page,
            @RequestParam(value = "sort") Optional <String> sort,
            Model model) {
        authentication(model);
        optionUses = optionSer.getBestSeller();
        model.addAttribute("colorFilter", colorSer.getColorFilter(optionUses));
        model.addAttribute("sizeFilter", sizeSer.getSizeFilter(optionUses));
        model.addAttribute("priceFilter", optionSer.getPriceFilter(optionUses));
        thisPage = optionSer.handleFilterStatus(color,size, price,page,sort, optionUses);
        optionUses = optionSer.dataArray;
        model.addAttribute("url","best-seller");
        model.addAttribute("namePage","Best Seller");
        model.addAttribute("options", optionSer.handlePagination(optionUses, thisPage));
        model.addAttribute("dataCart", new ArrayList<>());

        if(sort.isPresent()) {
            model.addAttribute("sortSet", sort.orElseThrow());
        }
        CollectionsFragmentController.handleFilter(color, price, size, model);
        return "user/Page/Products";
    }


    @GetMapping(value = "/new-arrival")
    public  String getDataNewArrive(
            @RequestParam(value = "color")Optional<String> color,
            @RequestParam(value = "size") Optional<String> size,
            @RequestParam(value = "price") Optional <String> price,
            @RequestParam(value = "page") Optional <String> page,
            @RequestParam(value = "sort") Optional <String> sort,
            Model model) {
        authentication(model);
        optionUses = optionSer.getBestSeller();

        model.addAttribute("colorFilter", colorSer.getColorFilter(optionUses));
        model.addAttribute("sizeFilter", sizeSer.getSizeFilter(optionUses));
        model.addAttribute("priceFilter",optionSer.getPriceFilter(optionUses));
        thisPage = optionSer.handleFilterStatus(color,size, price,page,sort, optionUses);
        optionUses = optionSer.dataArray;
        model.addAttribute("url","new-arrival");
        model.addAttribute("namePage","New Arrival");
        model.addAttribute("options", optionSer.handlePagination(optionUses, thisPage));
        model.addAttribute("dataCart", new ArrayList<>());

        if(sort.isPresent()) {
            model.addAttribute("sortSet", sort.orElseThrow());
        }
        CollectionsFragmentController.handleFilter(color, price, size, model);
        return "user/Page/Products";
    }



    @GetMapping(value = "/all-products")
    public  String getData(
            @RequestParam(value = "color")Optional<String> color,
            @RequestParam(value = "size") Optional<String> size,
            @RequestParam(value = "price") Optional <String> price,
            @RequestParam(value = "page") Optional <String> page,
            @RequestParam(value = "sort") Optional <String> sort,
            Model model) {
        authentication(model);

        optionUses = optionSer.getBestSeller();

        model.addAttribute("colorFilter", colorSer.getColorFilter(optionUses));
        model.addAttribute("sizeFilter", sizeSer.getSizeFilter(optionUses));
        model.addAttribute("priceFilter", optionSer.getPriceFilter(optionUses));


        thisPage = optionSer.handleFilterStatus(color,size, price,page,sort, optionUses);
        optionUses = optionSer.dataArray;


        model.addAttribute("url","all-products");
        model.addAttribute("namePage","All Products");

        model.addAttribute("options", optionSer.handlePagination(optionUses, thisPage));

        model.addAttribute("dataCart", new ArrayList<>());

        if(sort.isPresent()) {
            model.addAttribute("sortSet", sort.orElseThrow());
        }
        if(size.isPresent()) {
            List<String> data = List.of(size.orElseThrow().split(","));
            model.addAttribute("filterSize", data);
        } else  {
            model.addAttribute("filterSize", new ArrayList<>());
        }
        CollectionsFragmentController.handleFilter(color, price, size, model);
        return "user/Page/Products";
    }






    @GetMapping(value = "/RATE")
    public  String getDataRate(
            @RequestParam(value = "color")Optional<String> color,
            @RequestParam(value = "size") Optional<String> size,
            @RequestParam(value = "price") Optional <String> price,
            @RequestParam(value = "page") Optional <String> page,
            @RequestParam(value = "sort") Optional <String> sort,
            Model model) {
        authentication(model);
        optionUses = optionSer.getBestSeller();
        model.addAttribute("colorFilter", colorSer.getColorFilter(optionUses));
        model.addAttribute("sizeFilter", sizeSer.getSizeFilter(optionUses));
        model.addAttribute("priceFilter", optionSer.getPriceFilter(optionUses));
        thisPage = optionSer.handleFilterStatus(color,size, price,page,sort,optionUses);
        optionUses = optionSer.dataArray;
        model.addAttribute("url","RATE");
        model.addAttribute("namePage","SPECIAL PRICES");
        model.addAttribute("options", optionSer.handlePagination(optionUses, thisPage));
        model.addAttribute("dataCart", new ArrayList<>());

        if(sort.isPresent()) {
            model.addAttribute("sortSet", sort.orElseThrow());
        }
        if(size.isPresent()) {
            List<String> data = List.of(size.orElseThrow().split(","));
            model.addAttribute("filterSize", data);
        } else  {
            model.addAttribute("filterSize", new ArrayList<>());
        }
        CollectionsFragmentController.handleFilter(color, price, size, model);
        return "user/Page/Products";
    }

}
