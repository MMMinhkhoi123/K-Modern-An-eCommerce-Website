package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.custome.UploadProperties;
import com.leventsclone.leventsclone.data.use.ImageUse;
import com.leventsclone.leventsclone.data.use.ImagesUse;
import com.leventsclone.leventsclone.entity.Image;
import com.leventsclone.leventsclone.entity.Upload;
import com.leventsclone.leventsclone.repository.IUploadRepo;
import com.leventsclone.leventsclone.service.inter.IUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UploadSer implements IUpload {

    private final IUploadRepo UPLOAD__REPO;

    private  final UploadProperties uploadProperties;

    private final CloudinarySer cloudinarySer;

    @Autowired
    public UploadSer(IUploadRepo iUploadRepo, CloudinarySer cloudinarySer, UploadProperties uploadProperties) {
        this.UPLOAD__REPO = iUploadRepo;
        this.cloudinarySer = cloudinarySer;
        this.uploadProperties = uploadProperties;
    }

    @Override
    public void save(List<String> list) {
        File file = new File("");
        list.forEach((e) -> {
            Upload upload = new Upload();

            String pathFull = file.getAbsolutePath() + uploadProperties.getCommon() + e;
            File file1 = new File(pathFull);

            ImagesUse imagesUse = cloudinarySer.uploadCommon(file1);

            upload.setKeyUpload(imagesUse.getKey());
            upload.setDateUpload(new Date());
            upload.setLinkUpload(imagesUse.getName());
            Upload upload1 = UPLOAD__REPO.save(upload);
            System.out.println(upload1.getLinkUpload());

            Path nguonPath = Paths.get(pathFull);
            try {
                Files.deleteIfExists(nguonPath);
            } catch (IOException ex) {
                System.out.println("Xóa thất bại");
            }
        });
    }

    @Override
    public void delete(List<String> list) {
        List<String> listKey = new ArrayList<>();
        list.forEach((e) -> {
            Upload upload = UPLOAD__REPO.findByLinkUpload(e).orElseThrow();
            listKey.add(upload.getKeyUpload());
            UPLOAD__REPO.delete(upload);
        });
        cloudinarySer.delete(listKey);
    }

    @Override
    public List<ImagesUse> getAll() {
        List<ImagesUse> list = new ArrayList<>();
        UPLOAD__REPO.findAll().forEach((e) -> {
            ImagesUse imagesUse = new ImagesUse();
            imagesUse.setKey(e.getKeyUpload());
            imagesUse.setName(e.getLinkUpload());
            list.add(imagesUse);
        });
        return list;
    }

    @Override
    public Image getImageByLink(String link) {
        Upload upload = UPLOAD__REPO.findByLinkUpload(link).orElseThrow();
        Image image = new Image();
        image.setLink(upload.getLinkUpload());
        image.setCode(upload.getKeyUpload());
        UPLOAD__REPO.deleteById(upload.getIdUpload());
        return image;
    }


}
