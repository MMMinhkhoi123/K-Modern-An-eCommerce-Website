package com.leventsclone.leventsclone.controller.user.fragment;

import com.leventsclone.leventsclone.data.use.CityUse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/fragments-pages")
public class PagesFragmentController {

    @PostMapping(value = "/city")
    public  String  getListCiTy(@RequestBody List<CityUse> city, Model model){
        model.addAttribute("array_city", city);
        return "user/fragments/shipping/City :: city";
    }


    @PostMapping(value = "/city-address-add")
    public  String  getListCiTyAddressAdd(@RequestBody List<CityUse> city, Model model){
        model.addAttribute("array_city", city);
        return "user/fragments/common/Manage :: #address__city";
    }

    @PostMapping(value = "/district")
    public  String  getListDistrict(@RequestBody List<CityUse> district, Model model){
        model.addAttribute("array_district", district);
        return "user/fragments/shipping/District :: district";
    }

    @PostMapping(value = "/district-address-add")
    public  String  getListDistrictAddress(@RequestBody List<CityUse> district, Model model){
        model.addAttribute("array_district", district);
        return "user/fragments/common/Manage :: #address__district";
    }



    @PostMapping(value = "/commune")
    public  String  getListCommune(@RequestBody List<CityUse> commune, Model model){
        model.addAttribute("array_commune", commune);
        return "user/fragments/shipping/Commune :: commune";
    }


    @PostMapping(value = "/commune-address-add")
    public  String  getListCommuneAddressAdd(@RequestBody List<CityUse> commune, Model model){
        model.addAttribute("array_commune", commune);
        return "user/fragments/common/Manage :: #address__commune";
    }



    @GetMapping(value = "/reset-district")
    public  String  resetDistrict( Model model){
        model.addAttribute("array_district",  new ArrayList<>());
        return "user/fragments/common/Manage :: #address__district";
    }

    @GetMapping(value = "/reset-commune")
    public  String  resetCommune( Model model){
        model.addAttribute("array_commune", new ArrayList<>());
        return "user/fragments/common/Manage :: #address__commune";
    }



}
