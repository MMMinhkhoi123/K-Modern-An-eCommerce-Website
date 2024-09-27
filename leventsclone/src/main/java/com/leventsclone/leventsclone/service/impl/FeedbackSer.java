package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.data.use.*;
import com.leventsclone.leventsclone.entity.*;
import com.leventsclone.leventsclone.repository.IFeedbackRepo;
import com.leventsclone.leventsclone.service.inter.IFeedback;
import com.leventsclone.leventsclone.unstil.ConvertToOtherData;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class FeedbackSer implements IFeedback {

    private final IFeedbackRepo FEEDBACK__REPO;
    private final ConvertToOtherData convertToOtherData = new ConvertToOtherData();
    private final ImageSer imageSer;

    private final RatingSer userAssessSer;

    private OptionSer optionSer;
    private final ProductSer  productSer;
    private final ColorSer colorSer;
    private final CloudinarySer cloudinarySer;


    public FeedbackSer(OptionSer optionSer,IFeedbackRepo FEEDBACK__REPO,ImageSer imageSer, CloudinarySer cloudinarySer, ProductSer productSer, ColorSer colorSer, RatingSer userAssessSer) {
        this.optionSer = optionSer;
        this.FEEDBACK__REPO = FEEDBACK__REPO;
        this.imageSer = imageSer;
        this.cloudinarySer = cloudinarySer;
        this.productSer = productSer;
        this.colorSer = colorSer;
        this.userAssessSer = userAssessSer;
    }

    @Override
    public List<FeedbackUse> getAll() {
        List<FeedbackUse> feedbackUses = new ArrayList<>();

        FEEDBACK__REPO.findAll().forEach((item) -> {
            FeedbackUse feedbackUse = new FeedbackUse();
            feedbackUse.setId(item.getIdFeedback());
            ImageUse imageUse = imageSer.getById(item.getImage().getId());
            feedbackUse.setImageUse(imageUse);
            OptionUse optionUse = optionSer.getDetailById(item.getOption().getId());
            feedbackUse.setOptionUse(optionUse);
            feedbackUses.add(feedbackUse);
        });
        return feedbackUses;
    }

    private boolean checkColor(List<ColorUse> data, ColorUse item) {
        AtomicBoolean status  = new AtomicBoolean(true);
        data.forEach((itemGet) -> {
            if(Objects.equals(itemGet.getName(), item.getName())) {
                status.set(false);
            }
        });
        return status.get();
    }

    @Override
    public List<FeedbackUse> getSwiper() {
        List<FeedbackUse> feedbackUses = new ArrayList<>();
        List<Feedback> feedbacks =  FEEDBACK__REPO.findAll();


        int exceed = 4;
        for (int i = 0; i < feedbacks.size() ; i ++) {
            if(i <= exceed) {
                FeedbackUse feedbackUse = new FeedbackUse();
                feedbackUse.setId(feedbacks.get(i).getIdFeedback());
                ImageUse imageUse = imageSer.getById(feedbacks.get(i).getImage().getId());
                feedbackUse.setImageUse(imageUse);
                OptionUse optionUse = optionSer.getDetailById(feedbacks.get(i).getOption().getId());
                feedbackUse.setOptionUse(optionUse);

//                ImagesUse imagesUse = imageSer.getByFeedback(feedbacks.get(i));
//                feedbackUse.setImg(imagesUse.getName());
//                feedbackUse.setSumCountAssess(userAssessSer.getSumStarByIdProductAndIdColor(feedbacks.get(i).getImage().getProduct().getIdProduct(), feedbacks.get(i).getImage().getColor().getIdColor()));
//                Set<ImagesUse> images = imageSer.getAllByIdProductAndIdColor(imagesUse.getProductUse().getId(), imagesUse.getColorUse().getId());
//                feedbackUse.setImgProduct(images.stream().findFirst().orElseThrow().getName());
//                feedbackUse.setColorUse(imagesUse.getColorUse());
//                feedbackUse.setProductUse(imagesUse.getProductUse());
//                List<ImagesUse> imagesUses = new ArrayList<>(imageSer.getAllByIdProductAndIdColor(imagesUse.getProductUse().getId(), imagesUse.getColorUse().getId()));
//                List<ColorUse> list = new ArrayList<>();
//                imagesUses.forEach((e) -> {
//                    if(checkColor(list, e.getColorUse())) {
//                        list.add(e.getColorUse());
//                    }
//                });
//                feedbackUse.setColorUses(list);
//                feedbackUse.setImagesUses(imagesUses);
                feedbackUses.add(feedbackUse);
            }
        }
        return feedbackUses;
    }

    @Override
    public FeedbackUse getById(Long id) {
//        Feedback feedback = FEEDBACK__REPO.findById(id).orElseThrow();
//        FeedbackUse feedbackUse = new FeedbackUse();
//        feedbackUse.setId(feedback.getIdFeedback());
//        ImagesUse imagesUse = imageSer.getByFeedback(feedback);
//        feedbackUse.setColorUse(imagesUse.getColorUse());
//        feedbackUse.setProductUse(imagesUse.getProductUse());
//        feedbackUse.setImg(imagesUse.getName());
//        feedbackUse.setSumCountAssess(userAssessSer.getSumStarByIdProductAndIdColor(feedback.getImage().getProduct().getIdProduct(),feedback.getImage().getColor().getIdColor()));
//        Set<ImagesUse> list = imageSer.getAllByIdProductAndIdColor(imagesUse.getProductUse().getId(), imagesUse.getColorUse().getId());
//        feedbackUse.setImgProduct(list.stream().findFirst().orElseThrow().getName());

//        return feedbackUse;
        return  null;
    }

    @Override
    public void save(MultipartFile multipartFile, Long idOption) {
        Feedback feedback = new Feedback();
        feedback.setShowFeedback(false);
        ImagesUse imagesUse = cloudinarySer.uploadFileFeedback(multipartFile);
        Image image = imageSer.SaveTypeFeedBack(imagesUse);
        Option option = optionSer.getEnById(idOption);
        feedback.setImage(image);
        feedback.setOption(option);
        FEEDBACK__REPO.save(feedback);
    }

    @Override
    public void delete(List<Long> delete) {
        delete.forEach(FEEDBACK__REPO::deleteById);
    }


}
