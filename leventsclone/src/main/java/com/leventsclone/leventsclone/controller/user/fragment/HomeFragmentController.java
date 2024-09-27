package com.leventsclone.leventsclone.controller.user.fragment;

import com.leventsclone.leventsclone.data.use.SuggestUse;
import com.leventsclone.leventsclone.service.impl.DirectorySer;
import com.leventsclone.leventsclone.service.impl.OptionSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/init")
public class HomeFragmentController {
    private final DirectorySer DIRECTORY_SER;
    private final OptionSer optionSer;

    @Autowired
    public HomeFragmentController(DirectorySer directorySer, OptionSer optionSer) {
        this.DIRECTORY_SER = directorySer;
        this.optionSer = optionSer;
    }

    @GetMapping(value = "/directory")
    public  String  getDataSideBar(Model model) {
        model.addAttribute("DataDirectory",DIRECTORY_SER.getAll());
        return "user/fragments/common/sidebar :: sidebar";
    }

    @GetMapping(value = "/search/{text}")
    public  String  getDataSideBar(Model model, @PathVariable("text") String text) {
        SuggestUse suggestUse = optionSer.getProductSuccessByText(text, true);
        if(suggestUse.getCount() == 0) {
            model.addAttribute("noFund", text);
        }else  {
            model.addAttribute("dataResult",suggestUse);
        }
        return "user/fragments/common/Option-header/Header_search :: #content--search";
    }

}
