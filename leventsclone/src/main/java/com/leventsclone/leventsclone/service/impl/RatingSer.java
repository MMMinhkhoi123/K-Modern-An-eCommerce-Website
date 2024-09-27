package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.data.response.RatingRes;
import com.leventsclone.leventsclone.data.use.ClouUse;
import com.leventsclone.leventsclone.data.use.OptionUse;
import com.leventsclone.leventsclone.data.use.RatingUse;
import com.leventsclone.leventsclone.entity.*;
import com.leventsclone.leventsclone.repository.IRatingRepo;
import com.leventsclone.leventsclone.service.inter.IRating;
import com.leventsclone.leventsclone.unstil.ConvertToOtherData;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

@Service
public class RatingSer implements IRating {

    private final IRatingRepo RATING_REPO;

    private final CloudinarySer cloudinarySer;


    private final ImageSer imageSer;

    private final ConvertToOtherData convertToOtherData = new ConvertToOtherData();

    private final OptionSer optionSer;

    private final OrderSer orderSer;

    public RatingSer(ImageSer imageSer,OrderSer orderSer,OptionSer optionSer,IRatingRepo ASSESS_REPO, CloudinarySer cloudinarySer) {
        this.RATING_REPO = ASSESS_REPO;
        this.cloudinarySer = cloudinarySer;
        this.optionSer = optionSer;
        this.orderSer = orderSer;
        this.imageSer = imageSer;
    }



    @Override
    public void save(String tile, User user, String content, Optional<MultipartFile> multipartFile, Long idOption, String code, Integer count) {

        Option option = optionSer.getEnById(idOption);
        Orders orders = orderSer.getByCode(code);

        Rating userAssess = new Rating();

        Optional<Rating> userRating = RATING_REPO.findByOrdersAndOptionAndUser(orders, option, user);

            if(multipartFile.isPresent()) {

//                if(userRating.orElseThrow().getImage() != null) {
//                    //delete image on cloudy
//                    List<String> imgDelete = new ArrayList<>();
//                    imgDelete.add(userRating.orElseThrow().getImage().getCode());
//                    cloudinarySer.delete(imgDelete);
//                    // update on database
//                }
                ClouUse clouUse = cloudinarySer.uploadFileAssess(multipartFile.orElseThrow());
                Image image = new Image();
                image.setCode(clouUse.getKey());
                image.setLink(clouUse.getUrl());
                userAssess.setImage(imageSer.save(image));
            }

        userAssess.setDateAssess(new Date());
        userAssess.setUser(user);
        userAssess.setPermit(false);
        userAssess.setCount(count);
        userAssess.setComment(content);
        userAssess.setTitle(tile);
        userAssess.setOption(option);
        userAssess.setOrders(orders);

        if(multipartFile.isPresent()) {
           ClouUse clouUse =  cloudinarySer.uploadFileAssess(multipartFile.orElseThrow());
        }
        RATING_REPO.save(userAssess);
    }


    @Override
    public RatingRes getAllByPage(int page, Long idOption) {
        Option option = optionSer.getEnById(idOption);


        RatingRes rating = new RatingRes();
        List<RatingUse> ratingUses = new ArrayList<>();
        List<Rating> list = new ArrayList<>();
        int countLimit = 3;
        int countPage = 0;
        int resultSumStar = 0;
        int countUserAssess = 0;


         list = RATING_REPO.findAllByOption(option);

        int sumStar = 0;
        int countOne = 0;
        int countTwo = 0;
        int countTrue = 0;
        int countFour = 0;
        int countFive = 0;


        for (int x = 0; x < list.size() ; x ++) {
            switch (list.get(x).getCount()) {
                case 1 -> {
                    countOne ++;
                }
                case 2 -> {
                    countTwo ++;
                }
                case 3 -> {
                    countTrue ++;
                }
                case 4 -> {
                    countFour ++;
                }
                case 5 -> {
                    countFive ++;
                }
            }
            countUserAssess += 1;
            sumStar += list.get(x).getCount();
        }

        // set sum star
        resultSumStar = (int) Math.round((double) sumStar / list.size());


        // set count page
        countPage = (int) Math.ceil((double) list.size() / (countLimit));

        // set list
        if(!list.isEmpty()) {
            ratingUses = new ArrayList<>(getDataAfterPag(page, list));
        }

        rating.setSumCountStar(resultSumStar);
        rating.setThisPage(page);
        rating.setCountPag(countPage);

        rating.setCountAll(list.size());
        rating.setCountOne(countOne);
        rating.setCountTwo(countTwo);
        rating.setCountFour(countFour);
        rating.setCountFive(countFive);
        rating.setCountTrue(countTrue);
        rating.setList(ratingUses);
        rating.setNumberAssess(countUserAssess);
        return rating;
    }

    private List<RatingUse> getDataAfterPag(int thisPage, List<Rating> Ass) {
        int countLimit = 3;
        int countPage = (int) Math.ceil((double) Ass.size() / (countLimit));
        int indexStartPage = thisPage * countLimit;
        if(indexStartPage == Ass.size()) {
            indexStartPage -=1;
        }
        int countSet = indexStartPage + 3;
        List<RatingUse> ratingUsesResult = new ArrayList<>();
        for (int x = indexStartPage; x < Ass.size() ; x ++) {
                if (x  <  countSet && Ass.get(x).getPermit()) {
                    ratingUsesResult.add(convertToOtherData.getRatingUse(Ass.get(x)));
                }
        }
        return ratingUsesResult;
    }

    @Override
    public RatingRes getAllByPageSet(int thisPage, int countStar, Long idOption) {
        List<Rating> ratingUsesGet = new ArrayList<>();
        RatingRes ratingRes = new RatingRes();
        int countLimit = 3;
        Option option = optionSer.getEnById(idOption);

        List<RatingUse> assessUses = new ArrayList<>();
        List<Rating> list0 = RATING_REPO.findAllByOption(option);

        Predicate<Rating> ratingPredicate = Rating::getPermit;
        List<Rating> list = list0.stream().filter(ratingPredicate).toList();
        Predicate<Rating> streamsPredicate = item -> item.getCount() == countStar;
        ratingUsesGet = list.stream()
                .filter(streamsPredicate)
                .toList();

        if(!ratingUsesGet.isEmpty()) {
            assessUses = new ArrayList<>(getDataAfterPag(thisPage, ratingUsesGet));
        }
        ratingRes.setList(assessUses);
        ratingRes.setThisPage(thisPage);
        int countPage = (int) Math.ceil((double) ratingUsesGet.size() / (countLimit));
        ratingRes.setCountPag(countPage);
        return ratingRes;
    }

    @Override
    public int getSumStarByIdProductAndIdColor(Long idProduct, Long idColor) {
//        Product product = productSer.getByIdEt(idProduct);
//        Color color = colorSer.getColorByIdEt(idColor);
//        AtomicInteger sumStar = new AtomicInteger();
//        List<Rating> list = ASSESS_REPO.findAllByColorAndProduct(color, product);
//        list.forEach((e) -> {
//            sumStar.addAndGet(e.getCount());
//        });
//        return (int) Math.round((double) sumStar.get() / list.size());
        return  0;
    }

    @Override
    public List<RatingUse> getRatingByUserAndOption(User user, Long idOption) {
        List<RatingUse> ratingUses = new LinkedList<>();
        List<Orders> listOrderSuccess = orderSer.getOrderSuccessByUserAndOption(user, idOption);

        Option option = optionSer.getEnById(idOption);

        listOrderSuccess.forEach(orders -> {
            RatingUse ratingUse = null;
            List<Rating> list0 = RATING_REPO.findAllByOrders(orders);

            Predicate<Rating> ratingPredicates = Rating::getPermit;
            List<Rating> list = list0.stream().filter(ratingPredicates).toList();

            if(checkContainInDetail(option, list)) {
                Predicate<Rating> ratingPredicate = item -> Objects.equals(item.getOption().getId(), option.getId()) && item.getPermit();
                List<Rating> rating = list.stream().filter(ratingPredicate).toList();
                ratingUse = convertToOtherData.getRatingUse(rating.get(0));
                ratingUse.setOrderRes(orderSer.getOrderRes(orders));
                ratingUse.setStateAssess(true);
                ratingUses.add(ratingUse);
            } else  {

                ratingUse = new RatingUse();
                ratingUse.setStateAssess(false);
                ratingUse.setOrderRes(orderSer.getOrderRes(orders));
                ratingUses.add(ratingUse);
            }

        });
        return  ratingUses;
    }

    @Override
    public List<RatingUse> getAll() {
        List<RatingUse> ratingUses = new LinkedList<>();
        RATING_REPO.findAll().forEach(rating -> {
            RatingUse ratingUse = convertToOtherData.getRatingUse(rating);
            OptionUse optionUse = optionSer.getDetailById(rating.getOption().getId());
            ratingUse.setOptionUse(optionUse);
            ratingUses.add(ratingUse);
        });
        return ratingUses;
    }

    @Override
    public void updateRating(Boolean state, Long idRating) {
        Rating rating = RATING_REPO.findById(idRating).orElseThrow();
        rating.setPermit(state);
        RATING_REPO.save(rating);
    }


    private boolean checkContainInDetail( Option option, List<Rating> listRating) {
        AtomicReference<Boolean> aBoolean = new AtomicReference<>(false);
        listRating.forEach(rating ->  {
            if(Objects.equals(rating.getOption().getId(), option.getId())) {
                aBoolean.set(true);
            }
        });
        return  aBoolean.get();
    }
}
