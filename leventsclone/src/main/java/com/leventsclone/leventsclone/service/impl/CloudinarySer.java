package com.leventsclone.leventsclone.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.leventsclone.leventsclone.data.use.ClouUse;
import com.leventsclone.leventsclone.data.use.ImageUse;
import com.leventsclone.leventsclone.data.use.ImagesUse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinarySer {

    private final Cloudinary cloudinary;

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public ClouUse uploadFile(MultipartFile gif) {
        try {
            File uploadedFile = convertMultiPartToFile(gif);

            Map params = ObjectUtils.asMap(
                    "folder", "collections",
                    "resource_type", "image");
            Map uploadResult = cloudinary.uploader().upload(uploadedFile, params);
            boolean isDeleted = uploadedFile.delete();
            ClouUse clouUse = new ClouUse();
            clouUse.setUrl((String) uploadResult.get("secure_url"));
            clouUse.setKey((String) uploadResult.get("public_id"));
            if (isDeleted){
                System.out.println("File successfully deleted");
            }else
                System.out.println("File doesn't exist");
            return  clouUse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ClouUse uploadFileAssess(MultipartFile gif) {
        try {
            File uploadedFile = convertMultiPartToFile(gif);

            Map params = ObjectUtils.asMap(
                    "folder", "assess",
                    "resource_type", "image");
            Map uploadResult = cloudinary.uploader().upload(uploadedFile, params);
            boolean isDeleted = uploadedFile.delete();
            ClouUse clouUse = new ClouUse();
            clouUse.setUrl((String) uploadResult.get("secure_url"));
            clouUse.setKey((String) uploadResult.get("public_id"));
            if (isDeleted){
                System.out.println("File successfully deleted");
            }else
                System.out.println("File doesn't exist");
            return  clouUse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ClouUse uploadFilePay(MultipartFile gif) {
        try {
            File uploadedFile = convertMultiPartToFile(gif);

            Map params = ObjectUtils.asMap(
                    "folder", "uploadPay",
                    "resource_type", "image");
            Map uploadResult = cloudinary.uploader().upload(uploadedFile, params);
            boolean isDeleted = uploadedFile.delete();
            ClouUse clouUse = new ClouUse();
            clouUse.setUrl((String) uploadResult.get("secure_url"));
            clouUse.setKey((String) uploadResult.get("public_id"));
            if (isDeleted){
                System.out.println("File successfully deleted");
            }else
                System.out.println("File doesn't exist");
            return  clouUse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ImagesUse uploadFileFeedback(MultipartFile gif) {
        try {
            File uploadedFile = convertMultiPartToFile(gif);

            Map params = ObjectUtils.asMap(
                    "folder", "feedback",
                    "resource_type", "image");
            Map uploadResult = cloudinary.uploader().upload(uploadedFile, params);
            boolean isDeleted = uploadedFile.delete();
            ImagesUse imagesUse = new ImagesUse();
            imagesUse.setName((String) uploadResult.get("secure_url"));
            imagesUse.setKey((String) uploadResult.get("public_id"));
            if (isDeleted){
                System.out.println("File successfully deleted");
            }else
                System.out.println("File doesn't exist");
            return  imagesUse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public ImageUse upload(File file)  {
        ImageUse imageUse = new ImageUse();
                Map params = ObjectUtils.asMap(
                "folder", "products",
                "resource_type", "image");
        try{
            Map data = this.cloudinary.uploader().upload(file, params);
            imageUse.setLinkImage((String) data.get("secure_url"));
            imageUse.setCodeImage((String) data.get("public_id"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  imageUse;
    }

    public ImagesUse uploadOutfit(File file)  {
        ImagesUse ImagesUse = new ImagesUse();
        Map params = ObjectUtils.asMap(
                "folder", "outfit",
                "resource_type", "image");
        try{
            Map data = this.cloudinary.uploader().upload(file, params);
            ImagesUse.setName((String) data.get("secure_url"));
            ImagesUse.setKey((String) data.get("public_id"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  ImagesUse;
    }
    public ImagesUse uploadCommon(File file)  {
        ImagesUse ImagesUse = new ImagesUse();
        Map params = ObjectUtils.asMap(
                "folder", "common",
                "resource_type", "image");
        try{
            Map data = this.cloudinary.uploader().upload(file, params);
            ImagesUse.setName((String) data.get("secure_url"));
            ImagesUse.setKey((String) data.get("public_id"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  ImagesUse;
    }

    public void delete(List<String> list)  {
        Map params = ObjectUtils.asMap(
                "type", "upload",
                "resource_type", "image");
        try{
            Map data = this.cloudinary.api().deleteResources(list, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteInProducts(List<String> list)  {
        Map params = ObjectUtils.asMap(
                "type", "upload",
                "resource_type", "image");
        try{
            Map data = this.cloudinary.api().deleteResources(list, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
