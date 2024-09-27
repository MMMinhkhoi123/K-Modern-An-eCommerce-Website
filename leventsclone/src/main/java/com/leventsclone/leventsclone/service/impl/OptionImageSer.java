package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.data.use.ImageUse;
import com.leventsclone.leventsclone.data.use.ImagesUse;
import com.leventsclone.leventsclone.data.use.OptionImageUse;
import com.leventsclone.leventsclone.entity.Image;
import com.leventsclone.leventsclone.entity.Option;
import com.leventsclone.leventsclone.entity.OptionImage;
import com.leventsclone.leventsclone.repository.IOptionImageRepo;
import com.leventsclone.leventsclone.service.inter.IOptionImage;
import com.leventsclone.leventsclone.unstil.ConvertToOtherData;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Service
public class OptionImageSer implements IOptionImage {
    private final IOptionImageRepo OPTION_IMAGE_REPO;
    private final ImageSer imageSer;

    private final CloudinarySer cloudinarySer;

    private final ConvertToOtherData convert = new ConvertToOtherData();

    public  OptionImageSer(IOptionImageRepo iOptionImageRepo, ImageSer imageSer, CloudinarySer cloudinarySer) {
        OPTION_IMAGE_REPO = iOptionImageRepo;
        this.imageSer = imageSer;
        this.cloudinarySer = cloudinarySer;
    }


    @Override
    public List<OptionImageUse> getAllByOption(Option option) {

        List<OptionImageUse> optionImageUses = new LinkedList<>();

        OPTION_IMAGE_REPO.findAllByOption(option).orElseThrow().forEach(optionImage ->
                {
                    OptionImageUse imageUse = convert.getOptionImageUse(optionImage);
                    optionImageUses.add(imageUse);
                }
                );
        return optionImageUses;
    }

    @Override
    public void save(List<String> imgs, Option option) {
        imgs.forEach(item -> {
            ImageUse imagesUse = imageSer.moveFile(item);
            Image image = new Image();
            image.setCode(imagesUse.getCodeImage());
            image.setLink(imagesUse.getLinkImage());
            Image imageAfterSave = imageSer.save(image);
            OptionImage image1 = new OptionImage();
            image1.setImage(imageAfterSave);
            image1.setIsActive(false);
            image1.setOption(option);
            OPTION_IMAGE_REPO.save(image1);
        });
    }

    @Override
    public void update(List<String> imageNew, Option option) {

        List<OptionImage> optionImageUses = OPTION_IMAGE_REPO.findAllByOption(option).orElseThrow();

        // get list Loss in database
        Predicate<OptionImage> optionImagePredicate = optionImage -> {
            Predicate<String> iPredicate = s -> Objects.equals(s, optionImage.getImage().getLink());
            List<String> images = imageNew.stream().filter(iPredicate).toList();
            return images.isEmpty();
        };
        List<OptionImage> optionImageLosses = optionImageUses.stream().filter(optionImagePredicate).toList();

        // get list new in supplied images
        Predicate<String> stringPredicate = s -> {
            Predicate<OptionImage> iPredicate = optionImage -> Objects.equals(s, optionImage.getImage().getLink());
            List<OptionImage> images = optionImageUses.stream().filter(iPredicate).toList();
            return images.isEmpty();
        };
        List<String> ImageNews = imageNew.stream().filter(stringPredicate).toList();

        // delete loss in cloudy and database
        //# delete in database
        List<String> listDelete = new LinkedList<>();
        optionImageLosses.forEach(optionImage -> {
            System.out.println(optionImage.getImage().getCode());
            listDelete.add(optionImage.getImage().getCode());
            OPTION_IMAGE_REPO.deleteById(optionImage.getId());
            imageSer.deleteById(optionImage.getImage().getId());
        });
        //# delete in cloudy
        if(!listDelete.isEmpty()) {

            cloudinarySer.deleteInProducts(listDelete);
        }

        //# move list new onto database and cloudy
        save(ImageNews, option);
    }

    @Override
    public void delete(Long id) {
        OPTION_IMAGE_REPO.deleteById(id);
    }
}
