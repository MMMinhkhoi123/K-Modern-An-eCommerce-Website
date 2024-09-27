package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.custome.UploadProperties;
import com.leventsclone.leventsclone.data.use.ImageUse;
import com.leventsclone.leventsclone.data.use.ImagesUse;
import com.leventsclone.leventsclone.entity.*;
import com.leventsclone.leventsclone.repository.IImageRepo;
import com.leventsclone.leventsclone.repository.IOptionImageRepo;
import com.leventsclone.leventsclone.service.inter.IImage;
import com.leventsclone.leventsclone.unstil.ConvertToOtherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

@Service
public class ImageSer implements IImage {
    private final IImageRepo IMAGE_REPO;
    private  final ConvertToOtherData convertToOtherData = new ConvertToOtherData();
    private  final UploadProperties uploadProperties;

    private final  CloudinarySer cloudinarySer;


    @Autowired
    public ImageSer(
            IImageRepo IMAGE_REPO,
            IOptionImageRepo IIMAGE_REPO, UploadProperties uploadProperties, CloudinarySer cloudinarySer) {
        this.IMAGE_REPO = IMAGE_REPO;
        this.uploadProperties = uploadProperties;
        this.cloudinarySer = cloudinarySer;
    }





    private void sortData(List<ImagesUse> imagesUses) {
        Comparator<ImagesUse> comparator = new Comparator<ImagesUse>() {
            @Override
            public int compare(ImagesUse o1, ImagesUse o2) {
                return Integer.compare((int) o2.getDateCreate().getTime(), (int) o1.getDateCreate().getTime());
            }
        };
        imagesUses.sort(comparator);
    }






    @Override
    public Image save(Image image) {
        return  IMAGE_REPO.save(image);
    }


    @Override
    public void deleteById(Long idImage) {
        IMAGE_REPO.deleteById(idImage);
    }

    @Override
    public Image SaveTypeFeedBack( ImagesUse imagesUse) {
        Image image = new Image();
        image.setLink(imagesUse.getName());
        image.setCode(imagesUse.getKey());
        return IMAGE_REPO.save(image);
    }



    @Override
    public ImageUse getById(Long id) {
        return convertToOtherData.getImageUse(IMAGE_REPO.findById(id).orElseThrow());
    }


    public List<String>  getListImgNew(List<String> imagesUses, List<String> images) {
        List<String> imagesUses1 = new ArrayList<>();
        Predicate<String> streamsPredicate = item -> !images.contains(item.trim());
        imagesUses1 = imagesUses.stream()
                .filter(streamsPredicate)
                .toList();
        return  imagesUses1;
    }

    public List<String>  getListImgDeleted(List<String> imagesUses, List<String> images, List<String> imgNew) {
        List<String> imagesUses1 = new ArrayList<>();
        Predicate<String> streamsPredicate = item -> !imagesUses.contains(item.trim()) && !imgNew.contains(item.trim());
        imagesUses1 = images.stream()
                .filter(streamsPredicate)
                .toList();
        return  imagesUses1;
    }




    private String getNewName(String name) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // Các ký tự có thể có trong chuỗi
        Random random = new Random();
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString() + name;
    }
    public   ImageUse  moveFile(String name) {
        ImageUse url = new ImageUse();
        File file = new File("");
        String source = file.getAbsolutePath() + uploadProperties.getDirectory() + name;
        try {
            File file1 = new File(source);
            Path nguonPath = Paths.get(source);
            url = cloudinarySer.upload(file1);
            Files.deleteIfExists(nguonPath);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  url;
    }

    public   ImageUse  moveFileOutfit(String name) {
        ImageUse url = new ImageUse();
        File file = new File("");
        String source = file.getAbsolutePath() + uploadProperties.getOutfit() + name;
        try {
            File file1 = new File(source);
            Path nguonPath = Paths.get(source);
            url = cloudinarySer.upload(file1);
            Files.deleteIfExists(nguonPath);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  url;
    }

}
