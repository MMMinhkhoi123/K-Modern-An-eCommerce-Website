package com.leventsclone.leventsclone.controller.admin.service;

import com.leventsclone.leventsclone.controller.common.AuthenticatedData;
import com.leventsclone.leventsclone.data.use.AuthenticatedUse;
import com.leventsclone.leventsclone.data.use.MemberUse;
import com.leventsclone.leventsclone.data.use.VoucherUse;
import com.leventsclone.leventsclone.service.impl.MemberSer;
import com.leventsclone.leventsclone.service.impl.UserSer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin/members")
public class MemberController {
    private final MemberSer memberSer;
    private final AuthenticatedData authentication;
    public MemberController(MemberSer memberSer, AuthenticatedData authentication) {
        this.memberSer = memberSer;
        this.authentication = authentication;
    }


    private void handleAttribute(Model model,
                                 List<MemberUse> memberUses,
                                 MemberUse memberUse,
                                 String type,
                                 String action) {
        model.addAttribute("type", type);
        model.addAttribute("action", action);
        model.addAttribute("member", memberUse);
        model.addAttribute("members", memberUses);
    }




    @GetMapping("")
    public String getHome(Model model,
                          @RequestParam("update") Optional<String> updatePr,
                          @RequestParam("action")Optional<String> actionPr) {
        authentication.authentication(model);
        List<MemberUse> memberUses = memberSer.getAll();
        MemberUse memberUse = new MemberUse();
        String type = null;
        String action = "screen";
        if(actionPr.isPresent()) {
            type = "add";
            action = "edit";
        }
        if(updatePr.isPresent()) {
            type = "update";
            action = "edit";
            memberUse  = memberSer.getById(Long.valueOf(updatePr.orElseThrow()));
        }
        handleAttribute(model, memberUses, memberUse, type, action);
        return "admin/PageAdmin/Service/Member";
    }

    @PostMapping("/save")
    public String saveMember(Model model, @RequestBody Optional<MemberUse> memberUse) {
        authentication.authentication(model);
        MemberUse memberUse1 = memberUse.orElseThrow();

        Map<String, String> listError = new HashMap<>();

        if(memberUse1.getPercent() < 0 || memberUse1.getPercent() > 100) {
            listError.put("percent","Tỷ lệ giảm nằm trong khoảng 0 - 100 %");
        }
        if(memberUse1.getPrice() < 0) {
            listError.put("price","Mức tiêu thụ lớn hơn hoặc bằng 0");
        } else  {
            if(memberSer.checkPrice((double) memberUse1.getPrice())) {
                listError.put("price","Mức tiêu thụ đã được sử dụng");
            }
        }
        if(listError.isEmpty()) {
            memberSer.save(memberUse1);
            memberUse1 = new MemberUse();
            model.addAttribute("success", "Tạo hạng thành viên thành công");
        }


        List<MemberUse> list = memberSer.getAll();
        model.addAttribute("errors", listError);
        handleAttribute( model, list, memberUse1, "add", "edit");
        return  "admin/PageAdmin/Service/Member :: #admin__screen";
    }


    @PutMapping("/update")
    public String updateMember(Model model, @RequestBody Optional<MemberUse> member) {
        authentication.authentication(model);
        MemberUse memberUse1 = member.orElseThrow();
        MemberUse memberUseOld = memberSer.getById(memberUse1.getId());

        Map<String, String> listError = new HashMap<>();
        if(memberUse1.getPercent() < 0 || memberUse1.getPercent() > 100) {
            listError.put("percent","Tỷ lệ giảm nằm trong khoảng 0 - 100 %");
        }
        if(memberUse1.getPrice() < 0) {
            listError.put("price","Mức tiêu thụ lớn hơn hoặc bằng 0");
        } else  {
            if(memberUseOld.getPrice() != memberUse1.getPrice()) {
                if (memberSer.checkPrice((double) memberUse1.getPrice())) {
                    listError.put("price", "Mức tiêu thụ đã được sử dụng");
                }
            }
        }
        if(listError.isEmpty()) {
            memberSer.update(memberUse1);
            memberUse1 = memberSer.getById(memberUse1.getId());
            model.addAttribute("success", "Cập nhật hạng thành viên thành công");
        }


        List<MemberUse> list = memberSer.getAll();
        model.addAttribute("errors", listError);
        handleAttribute( model, list, memberUse1, "update", "edit");
        return  "admin/PageAdmin/Service/Member :: #admin__screen";
    }

    @GetMapping(value = "/open-from-update/{id}")
    public String openFromCreate(Model model, @PathVariable("id") Optional<Long> id) {
        authentication.authentication(model);
        List<MemberUse> list = memberSer.getAll();
        handleAttribute( model, list,memberSer.getById(id.orElseThrow()), "update", "edit");
        return  "admin/PageAdmin/Service/Member :: #admin__screen";
    }

    @GetMapping(value = "/open-from-create")
    public String openFromCreate(Model model) {
        authentication.authentication(model);
        List<MemberUse> list = memberSer.getAll();
        handleAttribute( model, list,new MemberUse(), "add", "edit");
        return  "admin/PageAdmin/Service/Member :: #admin__screen";
    }


    @DeleteMapping("/delete")
    public String delete(Model model,@RequestBody Optional<List<Long>> id) {
        authentication.authentication(model);
        memberSer.delete(id.orElseThrow());
        List<MemberUse> list = memberSer.getAll();
        handleAttribute( model, list ,new MemberUse(), "screen", "screen");
        return "admin/fragmentsAdmin/service/member/member :: member";
    }


}
