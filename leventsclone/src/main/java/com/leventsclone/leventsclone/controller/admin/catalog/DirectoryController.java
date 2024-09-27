package com.leventsclone.leventsclone.controller.admin.catalog;


import com.leventsclone.leventsclone.controller.common.AuthenticatedData;
import com.leventsclone.leventsclone.data.response.DirectoryRes;
import com.leventsclone.leventsclone.data.use.AuthenticatedUse;
import com.leventsclone.leventsclone.service.impl.DirectorySer;
import com.leventsclone.leventsclone.service.impl.UserSer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping(value = "/admin/directory")
public class DirectoryController {
    private  final DirectorySer directorySer;

    private final AuthenticatedData auth;

    public DirectoryController(DirectorySer directorySer, AuthenticatedData authenticatedData) {
        this.directorySer = directorySer;
        this.auth = authenticatedData;
    }



    @GetMapping("")
    public String getPageDirectory(Model model,
                                   @RequestParam("update") Optional<String> updatePr,
                                   @RequestParam("action") Optional<String> actionPr) {
        auth.authentication(model);
        List<DirectoryRes> directoryRes = directorySer.getAll();
        String type = null;
        String action = "screen";
        DirectoryRes directoryRes1 = new DirectoryRes();
        if(actionPr.isPresent()) {
            type = "add";
            action= "edit";
        }
        if(updatePr.isPresent()) {
            type = "update";
            action= "edit";
            directoryRes1 = directorySer.getById(Long.valueOf(updatePr.orElseThrow()));
        }

        handleAttribute(model,
                directoryRes1,
                type,
                action,
                directoryRes
        );
        return "admin/PageAdmin/catalog/directory";
    }

    private void handleAttribute(Model model,
                                 DirectoryRes directoryResC,
                                 String type,
                                 String action,
                                 List<DirectoryRes> directoryRes) {
        model.addAttribute("type",type);
        model.addAttribute("directory", directoryResC);
        model.addAttribute("action",action);
        model.addAttribute("directories", directoryRes);
    }

    @PostMapping("/save")
    public String addDirectory(
            Model model, @RequestBody Optional<DirectoryRes> data) {
        auth.authentication(model);
        String errorCode = null;
        String state = null;
        DirectoryRes directoryRes = new DirectoryRes();

        if(directorySer.getByCode(data.orElseThrow().getKey()).isPresent()) {
            errorCode = "Đường dẫn đã được sữ dụng";
        } else  {
            state = "success";
            directorySer.save(data.orElseThrow());
        }
        directoryRes.setName(data.orElseThrow().getName());
        directoryRes.setKey(data.orElseThrow().getKey());
        model.addAttribute("state", state);
        model.addAttribute("errorCode", errorCode);
        handleAttribute(model,
                directoryRes,
                "add",
                "edit",
                directorySer.getAll()
        );
        return "admin/PageAdmin/catalog/directory :: #admin__screen";
    }

    @GetMapping("/open-from-update/{id}")
    public String getDataUpdate(@PathVariable(value = "id") Optional<Long> id, Model model) {
        auth.authentication(model);
        handleAttribute(model,
                directorySer.getById(id.orElseThrow()),
                "update",
                "edit",
                directorySer.getAll()
        );
        return "admin/PageAdmin/catalog/directory :: #admin__screen";
    }

    @GetMapping("/open-from-create")
    public String getFromCreate(Model model) {
        auth.authentication(model);
        handleAttribute(model,
                new DirectoryRes(),
                "add",
                "edit",
                directorySer.getAll()
        );
        return "admin/PageAdmin/catalog/directory :: #admin__screen";
    }



    @PutMapping(value = "/update")
    public String updateDirectory( Model model, @RequestBody Optional<DirectoryRes> data) {
        auth.authentication(model);
        String errorName = null;
        String state = null;

        if(data.isPresent()) {
            DirectoryRes directoryRes = data.orElseThrow();
            DirectoryRes directoryRes1 = directorySer.getById(directoryRes.getId());
            if(!directoryRes1.getName().equals(directoryRes.getName())) {
                directoryRes1.setName(directoryRes.getName());
            }
            if(!Objects.equals(directoryRes1.getKey(), directoryRes.getKey())) {
                if(directorySer.getByCode(directoryRes.getKey()).isPresent()) {
                    errorName = "Đường dẫn đã được sữ dụng";
                } else {
                    directoryRes1.setKey(directoryRes.getKey());
                }
            }
            if(errorName == null) {
                state = "success";
                directorySer.update(directoryRes1);
            }

        }
        model.addAttribute("state", state);
        model.addAttribute("errorCode", errorName);

        model.addAttribute("directory", data.orElseThrow());
        handleAttribute(model,
                data.orElseThrow(),
                "update",
                "edit",
                directorySer.getAll()
        );
        return "admin/PageAdmin/catalog/directory :: #admin__screen";
    }


    @DeleteMapping("/delete")
    public String delete(Model model, @RequestBody List<String> list) {
        auth.authentication(model);
        list.forEach((e) -> {
            directorySer.deleteById(Long.parseLong(e.trim()));
        });
        handleAttribute(model,
                null,
                null,
                "screen",
                directorySer.getAll()
        );
        return "admin/fragmentsAdmin/catalog/directory/directory :: directory";
    }

}
