package com.leventsclone.leventsclone.api.admin;

import com.leventsclone.leventsclone.custome.UploadProperties;
import com.leventsclone.leventsclone.data.use.ClouUse;
import com.leventsclone.leventsclone.service.impl.CloudinarySer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin/products-api")
public class ProductsApi {
    private final UploadProperties uploadProperties;
    private final CloudinarySer cloudinarySer;

    public ProductsApi(UploadProperties uploadProperties, CloudinarySer cloudinaryService, CloudinarySer cloudinarySer) {
        this.uploadProperties = uploadProperties;
        this.cloudinarySer = cloudinarySer;
    }

    @DeleteMapping("/delete/{name}")
    public Boolean deleteImage(@PathVariable(value = "name")Optional<String> name) {
        File x = new File("");
        String path = x.getAbsolutePath() +  uploadProperties.getDirectory() + name.orElseThrow();
        File file = new File(path);
        System.out.println(path);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Tệp đã được xóa thành công.");
                return  true;
            } else {
                System.out.println("Không thể xóa tệp.");
                return  false;
            }
        } else {
            System.out.println("Tệp không tồn tại.");
            return  false;
        }
    }

    @DeleteMapping("/delete-outfit/{name}")
    public Boolean deleteImageOutFit(@PathVariable(value = "name")Optional<String> name) {
        File x = new File("");
        String path = x.getAbsolutePath() +  uploadProperties.getOutfit() + name.orElseThrow();
        File file = new File(path);
        System.out.println(path);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Tệp đã được xóa thành công.");
                return  true;
            } else {
                System.out.println("Không thể xóa tệp.");
                return  false;
            }
        } else {
            System.out.println("Tệp không tồn tại.");
            return  false;
        }
    }



    @DeleteMapping("/delete-common/{name}")
    public Boolean deleteImageCommon(@PathVariable(value = "name")Optional<String> name) {
        File x = new File("");
        String path = x.getAbsolutePath() +  uploadProperties.getCommon() + name.orElseThrow();
        File file = new File(path);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Tệp đã được xóa thành công.");
                return  true;
            } else {
                System.out.println("Không thể xóa tệp.");
                return  false;
            }
        } else {
            System.out.println("Tệp không tồn tại.");
            return  false;
        }
    }


    @PostMapping("/upload-common")
    public Boolean uploadImageCommon(@RequestParam("file") MultipartFile files, HttpServletRequest httpServletRequest) throws IOException {
        boolean data =  true;
        File file = new File("");
        System.out.println("Handing common" + files.getOriginalFilename());
        String UrlFile = file.getAbsolutePath() +  uploadProperties.getCommon();
        Path paths  = Paths.get(UrlFile + files.getOriginalFilename());
        Files.copy(files.getInputStream(),paths, StandardCopyOption.REPLACE_EXISTING);
        return  data;
    }


    @PostMapping("/upload-outfit")
    public Boolean uploadImageOufit(@RequestParam("file") MultipartFile files, HttpServletRequest httpServletRequest) throws IOException {
        boolean data =  true;
        File file = new File("");
        System.out.println("Handing img outfit : " + files.getOriginalFilename());
        String UrlFile = file.getAbsolutePath() +  uploadProperties.getOutfit();
        Path paths  = Paths.get(UrlFile + files.getOriginalFilename());
        Files.copy(files.getInputStream(),paths, StandardCopyOption.REPLACE_EXISTING);
        return  data;
    }


    @PostMapping("/upload")
    public Boolean uploadImage(@RequestParam("file") MultipartFile files, HttpServletRequest httpServletRequest) throws IOException {
        boolean data =  true;
        File file = new File("");
        System.out.println("Handing" + files.getOriginalFilename());
        String UrlFile = file.getAbsolutePath() +  uploadProperties.getDirectory();
        Path paths  = Paths.get(UrlFile + files.getOriginalFilename());
        Files.copy(files.getInputStream(),paths, StandardCopyOption.REPLACE_EXISTING);
        return  data;
    }



    @GetMapping(value = "/img-upload/{imgname}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public  @ResponseBody byte[] DownLoadVideo(@PathVariable("imgname") Optional<String> name) throws IOException {
        File file = new File("");
        String url = file.getAbsolutePath() + uploadProperties.getDirectory() + name.orElseThrow();
        return Files.readAllBytes(Paths.get(url));
    }

    @GetMapping(value = "/img-upload-outfit/{imgname}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public  @ResponseBody byte[] getImgUploadOutfit(@PathVariable("imgname") Optional<String> name) throws IOException {
        File file = new File("");
        String url = file.getAbsolutePath() + uploadProperties.getOutfit() + name.orElseThrow();
        return Files.readAllBytes(Paths.get(url));
    }

    @GetMapping(value = "/img-upload-common/{imgname}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public  @ResponseBody byte[] getImgUpload(@PathVariable("imgname") Optional<String> name) throws IOException {
        File file = new File("");
        String url = file.getAbsolutePath() + uploadProperties.getCommon() + name.orElseThrow();
        return Files.readAllBytes(Paths.get(url));
    }
}
