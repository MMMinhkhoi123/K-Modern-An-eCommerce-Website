package com.leventsclone.leventsclone.controller.admin.common;

import com.leventsclone.leventsclone.service.impl.UploadSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/upload")
public class uploadController {
    private final UploadSer uploadSer;

    @Autowired
    public uploadController(UploadSer uploadSer) {
        this.uploadSer = uploadSer;
    }


    @PostMapping(value = "/save")
    public String upload(@RequestBody Optional<List<String>> data,Model model) {
        uploadSer.save(data.orElseThrow());
        model.addAttribute("imagesUpload", uploadSer.getAll());
        model.addAttribute("success", "Tải hình ảnh thành công");
        return  "admin/fragmentsAdmin/common/listUpload :: #upload__collection--content";
    }

    @DeleteMapping(value = "/delete")
    public String delete(@RequestBody Optional<List<String>> data,Model model) {
        uploadSer.delete(data.orElseThrow());
        model.addAttribute("imagesUpload", uploadSer.getAll());
        model.addAttribute("success", "Xóa hình ảnh thành công");
        return  "admin/fragmentsAdmin/common/listUpload :: #upload__collection--content";
    }
}
