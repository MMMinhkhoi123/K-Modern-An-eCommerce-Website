package com.leventsclone.leventsclone.controller.user.fragment;

import com.leventsclone.leventsclone.data.use.OptionUse;
import com.leventsclone.leventsclone.service.impl.EventSer;
import com.leventsclone.leventsclone.service.impl.OptionSer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/fragments")
public class CollectionsFragmentController {
    private List<OptionUse> optionUses;
    private OptionSer optionSer;
    private int  thisPage = 1;

    public CollectionsFragmentController( OptionSer optionSer) {
        this.optionSer = optionSer;
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
        optionUses = optionSer.getProductSuccessByText(text.orElseThrow(), false).getOptionUses();
        thisPage = optionSer.handleFilterStatus(color,size, price,page,sort, optionUses);
        optionUses = optionSer.dataArray;
        model.addAttribute("options", optionSer.handlePagination(optionUses,thisPage));
        if(sort.isPresent()) {
            model.addAttribute("sortSet", sort.orElseThrow());
        }
        return "user/fragments/collections/View-Products :: list";
    }

    @GetMapping(value = "/{name}")
    public String getDataByType(
            @PathVariable(value = "name") Optional<String> name,
            @RequestParam(value = "color") Optional<String> color,
            @RequestParam(value = "size") Optional<String> size,
            @RequestParam(value = "price") Optional <String> price,
            @RequestParam(value = "page") Optional <String> page,
            @RequestParam(value = "sort") Optional <String> sort,
            Model model) {
//        if(eventSer.getByPath(name.orElseThrow()).isPresent()) {
//            Event event = eventSer.getByPath(name.orElseThrow()).orElseThrow();
//            data = productSizeColorSer.getAllByEvent(event.getIdEvent());
//        } else  {
//
            optionUses = optionSer.getAllByTypeProduct(name.orElseThrow());
//        }
        thisPage = optionSer.handleFilterStatus(color,size, price,page,sort,optionUses);
        optionUses = optionSer.dataArray;
        model.addAttribute("options", optionSer.handlePagination(optionUses,thisPage));
        if(sort.isPresent()) {
            model.addAttribute("sortSet", sort.orElseThrow());
        }
        return "user/fragments/collections/View-Products :: list";
    }

    @GetMapping(value = "/RATE")
    public String getDataRate(
            @RequestParam(value = "color") Optional<String> color,
            @RequestParam(value = "size") Optional<String> size,
            @RequestParam(value = "price") Optional <String> price,
            @RequestParam(value = "page") Optional <String> page,
            @RequestParam(value = "sort") Optional <String> sort,
            Model model) {
        if(price.isPresent()) {
            List<String> data = List.of(price.orElseThrow().split(","));
            model.addAttribute("filterPrice", data);
        }

        optionUses = optionSer.getBestSeller();
        thisPage = optionSer.handleFilterStatus(color,size, price,page,sort,optionUses);
        optionUses = optionSer.dataArray;
        model.addAttribute("options", optionSer.handlePagination(optionUses,thisPage));
        if(sort.isPresent()) {
            model.addAttribute("sortSet", sort.orElseThrow());
        }
        return "user/fragments/collections/View-Products :: list";
    }




    @GetMapping(value = "/all-products")
    public String getData(
            @RequestParam(value = "color") Optional<String> color,
            @RequestParam(value = "size") Optional<String> size,
            @RequestParam(value = "price") Optional <String> price,
            @RequestParam(value = "page") Optional <String> page,
            @RequestParam(value = "sort") Optional <String> sort,
            Model model) {
        if(price.isPresent()) {
            List<String> data = List.of(price.orElseThrow().split(","));
            model.addAttribute("filterPrice", data);
        }
        optionUses = optionSer.getBestSeller();
        thisPage = optionSer.handleFilterStatus(color,size, price,page,sort,optionUses);
        optionUses = optionSer.dataArray;
        model.addAttribute("options", optionSer.handlePagination(optionUses,thisPage));
        if(sort.isPresent()) {
            model.addAttribute("sortSet", sort.orElseThrow());
        }
        return "user/fragments/collections/View-Products :: list";
    }


    @GetMapping(value = "/new-arrival")
    public String getDataNewArrival(
            @RequestParam(value = "color") Optional<String> color,
            @RequestParam(value = "size") Optional<String> size,
            @RequestParam(value = "price") Optional <String> price,
            @RequestParam(value = "page") Optional <String> page,
            @RequestParam(value = "sort") Optional <String> sort,
            Model model) {

        optionUses = optionSer.getBestSeller();
        thisPage = optionSer.handleFilterStatus(color,size, price,page,sort,optionUses);
        optionUses = optionSer.dataArray;
        model.addAttribute("options", optionSer.handlePagination(optionUses,thisPage));
        if(sort.isPresent()) {
            model.addAttribute("sortSet", sort.orElseThrow());
        }
        return "user/fragments/collections/View-Products :: list";
    }

    @GetMapping(value = "/best-seller")
    public String getDataBestSeller(
            @RequestParam(value = "color") Optional<String> color,
            @RequestParam(value = "size") Optional<String> size,
            @RequestParam(value = "price") Optional <String> price,
            @RequestParam(value = "page") Optional <String> page,
            @RequestParam(value = "sort") Optional <String> sort,
            Model model) {

        optionUses = optionSer.getBestSeller();
        thisPage = optionSer.handleFilterStatus(color,size, price,page,sort,optionUses);

        model.addAttribute("options", optionSer.handlePagination(optionUses,thisPage));
        if(sort.isPresent()) {
            model.addAttribute("sortSet", sort.orElseThrow());
        }
        return "user/fragments/collections/View-Products :: list";
    }

    @GetMapping(value = "/filter-list")
    public  String  getListFilter(
            @RequestParam(value = "q") Optional<String> text,
            @RequestParam(value = "color") Optional<String> color,
            @RequestParam(value = "size") Optional<String> size,
            @RequestParam(value = "price") Optional <String> price,
            Model model
    ) {

        handleFilter(color, price,size, model);
        return "user/fragments/collections/Filter-Products :: filter-list";
    }

    public static void handleFilter(
            @RequestParam("color") Optional<String> color,
            @RequestParam("price") Optional<String> price,
            @RequestParam("color") Optional<String> size,
            Model model) {
        if(color.isPresent()) {
            List<String> data = new ArrayList<>();
            if(!color.get().isEmpty()) {
                data = List.of(color.orElseThrow().split(","));
            }
            model.addAttribute("filterColor", data);
        } else  {
            model.addAttribute("filterColor", new ArrayList<>());
        }
        if(price.isPresent()) {
            List<String> data = new ArrayList<>();
            if(!price.get().isEmpty()) {
                data = List.of(price.orElseThrow().split(","));
            }
            model.addAttribute("filterPrice", data);
        } else  {
            model.addAttribute("filterPrice", new ArrayList<>());
        }

        if(size.isPresent()) {
            List<String> data = new ArrayList<>();
            if(!size.get().isEmpty()) {
                data =List.of(size.orElseThrow().split(","));
            }
            model.addAttribute("filterSize", data);
        } else  {
            model.addAttribute("filterSize", new ArrayList<>());
        }
    }

}
