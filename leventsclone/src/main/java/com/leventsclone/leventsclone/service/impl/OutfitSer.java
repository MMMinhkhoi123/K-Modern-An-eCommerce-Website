package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.custome.UploadProperties;
import com.leventsclone.leventsclone.data.response.OutfitRes;
import com.leventsclone.leventsclone.data.use.*;
import com.leventsclone.leventsclone.entity.*;
import com.leventsclone.leventsclone.repository.IOutfitRepo;
import com.leventsclone.leventsclone.service.inter.IOutfit;
import com.leventsclone.leventsclone.unstil.ConvertToOtherData;
import com.leventsclone.leventsclone.unstil.Extra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

@Service
public class OutfitSer implements IOutfit {
    private final IOutfitRepo OUTFIT_REPO;
    private final UploadProperties uploadProperties;
    private final  ImageSer imageSer;
    private final  CloudinarySer cloudinarySer;
    private  final ConvertToOtherData convertToOtherData = new ConvertToOtherData();
    private final OptionSer optionSer;

    private final OptionSizeSer optionSizeSer;
    @Autowired
    public OutfitSer(
            OptionSer optionSer,
            IOutfitRepo iOutfitRepo,
                     CloudinarySer cloudinarySer,
                     UploadProperties uploadProperties,
                     ImageSer imageSer, OptionSizeSer optionSizeSer) {
        OUTFIT_REPO = iOutfitRepo;
        this.optionSizeSer = optionSizeSer;
        this.optionSer = optionSer;
        this.imageSer = imageSer;
        this.uploadProperties = uploadProperties;
        this.cloudinarySer = cloudinarySer;
    }


    private OutfitUse getDataOrigin(Outfit outfit) {
        OutfitUse outfitUse = new OutfitUse();
        outfitUse.setId(outfit.getId());
        outfitUse.setWeight(outfit.getWeight());
        outfitUse.setHeight(outfit.getHeight());
        outfitUse.setOptionSizeUSe(convertToOtherData.getOptionSizeUse(outfit.getOptionSize()));
        outfitUse.setOptionUse(optionSer.getDetailById(outfit.getOptionSize().getOption().getId()));
        List<OptionUse> optionUses = new ArrayList<>();
        outfit.getOptions().forEach(option -> {
            optionUses.add(optionSer.getDetailById(option.getId()));
        });
        outfitUse.setOptionUses(optionUses);
        List<ImageUse> imagesUses = new LinkedList<>();
        outfit.getImages().forEach(image -> {
            imagesUses.add(convertToOtherData.getImageUse(image));
        });
        outfitUse.setImagesUses(imagesUses);
        outfitUse.setImagesUses(imagesUses);
        return  outfitUse;
    }


    private void sort(List<OutfitUse> OutfitUse) {
        Comparator<OutfitUse> resComparator = new Comparator<OutfitUse>() {
            @Override
            public int compare(OutfitUse o1, OutfitUse o2) {
                return Double.compare(o2.getId() , o1.getId());
            }
        };
        OutfitUse.sort(resComparator);
    }
    @Override
    public List<OutfitUse> getAllR() {
        List<OutfitUse> outfitUses = new ArrayList<>();
        OUTFIT_REPO.findAll().forEach(outfit -> {
            OutfitUse outfitUse = getDataOrigin(outfit);
            outfitUses.add(outfitUse);
        });
        sort(outfitUses);
        return outfitUses;
    }

    @Override
    public List<OutfitUse> getAllSwiper() {
        int exceed = 6;
        List<OutfitUse> outfitUses = new ArrayList<>();
        List<Outfit> list = OUTFIT_REPO.findAll();
        for (int i = 0 ; i < list.size() ; i ++ ) {
            Outfit outfit = list.get(i);
            if(i <= exceed) {
                OutfitUse outfitUse = getDataOrigin(outfit);
                outfitUses.add(outfitUse);
            }
        }
        return outfitUses;
    }

    @Override
    public Outfit Save(String height, String weight , List<String> images, Long idOptionSize) {
        Outfit outfit = new Outfit();
        outfit.setWeight(weight);
        outfit.setHeight(height);
        OptionSize optionSize = optionSizeSer.getEnById(idOptionSize);
        outfit.setOptionSize(optionSize);
        List<Image> list = new LinkedList<>();
        images.forEach(s -> {
            ImageUse imageUse = imageSer.moveFileOutfit(s);
            Image image = new Image();
            image.setLink(imageUse.getLinkImage());
            image.setCode(imageUse.getCodeImage());
            list.add(imageSer.save(image));
        });
        outfit.setImages(list);
        return  OUTFIT_REPO.save(outfit);
    }

    @Override
    public Outfit update(String height, String weight, List<String> images, Long idOutfit) {

        Outfit outfit = OUTFIT_REPO.findById(idOutfit).orElseThrow();
        outfit.setWeight(weight);
        outfit.setHeight(height);

        //get list old
        Predicate<Image> imagePredicate0 = image -> {
            Predicate<String> stringPredicate = s -> Objects.equals(s, image.getLink());
            List<String> list = images.stream().filter(stringPredicate).toList();
            return !list.isEmpty();
        };
        List<Image> listOld = outfit.getImages().stream().filter(imagePredicate0).toList();

        //get list Loss
        Predicate<Image> imagePredicate = image -> {
            Predicate<String> stringPredicate = s -> Objects.equals(s, image.getLink());
            List<String> list = images.stream().filter(stringPredicate).toList();
            return list.isEmpty();
        };
        List<Image> listLoss = outfit.getImages().stream().filter(imagePredicate).toList();

        //get list New
        Predicate<String> predicate = s -> {
            Predicate<Image> imagePredicate1 = image -> Objects.equals(image.getLink(), s);

            List<Image> images1 = outfit.getImages().stream().filter(imagePredicate1).toList();
            return  images1.isEmpty();
        };
        List<String> listNew = images.stream().filter(predicate).toList();


        List<Image> imagesSetData = new LinkedList<>(listOld);
        //# handing move
        listNew.forEach(s -> {
            ImageUse imageUse = imageSer.moveFileOutfit(s);
            Image image = new Image();
            image.setCode(imageUse.getCodeImage());
            image.setLink(imageUse.getLinkImage());
            imagesSetData.add(imageSer.save(image));
        });


        // #delete list loss in database and cloudy
        List<String> listLossCloudy = new LinkedList<>();
        listLoss.forEach(image -> {
            imageSer.deleteById(image.getId());
            listLossCloudy.add(image.getCode());
        });
        if(!listLossCloudy.isEmpty()) {
            cloudinarySer.delete(listLossCloudy);
        }
        outfit.setImages(imagesSetData);
        OUTFIT_REPO.save(outfit);


        return  null;
    }

    @Override
    public void delete(List<Long> list) {
        list.forEach(OUTFIT_REPO::deleteById);
    }

    @Override
    public void deleteMix(List<Long> list, Long idOutfit) {
        Outfit outfit = OUTFIT_REPO.findById(idOutfit).orElseThrow();
        List<Option> options = outfit.getOptions();
        Predicate<Option> optionPredicate = option -> {
            Predicate<Long> predicate = aLong -> Objects.equals(aLong, option.getId());
            List<Long> list1 = list.stream().filter(predicate).toList();
            return list1.isEmpty();
        };
        List<Option> options1 = new LinkedList<>(options.stream().filter(optionPredicate).toList());
        outfit.setOptions(options1);
        OUTFIT_REPO.save(outfit);
    }

    @Override
    public OutfitUse getById(Long id) {
        Outfit outfit = OUTFIT_REPO.findById(id).orElseThrow();
        OutfitUse outfitUse = convertToOtherData.getOutfit(outfit);
        outfitUse.setOptionUse(optionSer.getDetailById(outfit.getOptionSize().getOption().getId()));
        List<OptionUse> optionUses = new ArrayList<>();
        outfit.getOptions().forEach(option -> {
            optionUses.add(optionSer.getDetailById(option.getId()));
        });
        outfitUse.setOptionUses(optionUses);
        return outfitUse;
    }

    @Override
    public Outfit getEnById(Long id) {
        return OUTFIT_REPO.findById(id).orElseThrow();
    }

    private void  deleteFile(String name) {
        File file = new File("");
        String url = file.getAbsolutePath()  + uploadProperties.getOutfit() + name;
        Path path  =Path.of(url);
        try {
            Files.delete(path);
        }catch (Exception ex) {
            System.out.println("File not exist");
        }
    }
    private File createFile(String name) {
        File file = new File("");
        String url = file.getAbsolutePath()  + uploadProperties.getOutfit() + name;
        return new File(url);
    }

    @Override
    public List<OutfitUse> getByType(String type) {
//        List<OutfitUse> dataOriginal = new ArrayList<>();
//        Predicate<OutfitUse> streamsPredicate = null;
//        if(type.equalsIgnoreCase("best-seller")) {
//            streamsPredicate = item ->
//            productSizeColorSer.getByIdProductAndIdColorAndIdSizeR(item.getProductUse().getId(), item.getColorUse().getId(), item.getSizeUse().getId()).getSold() > 5;
//        } else  {
//            streamsPredicate = item -> item.getProductUse().isClassic();
//        }
//        dataOriginal = getAllR().stream()
//                .filter(streamsPredicate)
//                .toList();
//        return dataOriginal;
        return  null;
    }

    @Override
    public OutfitRes getByNameAndColor(String data) {
        OutfitRes outfitRes = new OutfitRes();
//        AtomicReference<ProductSizeColorUSe> productSizeColorUSe = new AtomicReference<>(new ProductSizeColorUSe());
//        Set<ImagesUse> imagesUses = new HashSet<>();
//        List<ProductSizeColorUSe> productSizeColorUSes = new ArrayList<>();
//        OutfitUse outfitUse = new OutfitUse();
//
//        getAllR().forEach((e) -> {
//            String name = ex.getUrlVid(e.getProductUse().getName());
//            String color = ex.getUrlVid(e.getColorUse().getName());
//            String id = e.getId().toString();
//            String valid = name + "-" + color + "-" + id;
//            if(data.equalsIgnoreCase(valid)) {
//
//            }
//        });
//        getAll().forEach((e) -> {
//            String name = ex.getUrlVid(e.getProductUse().getName());
//            String color = ex.getUrlVid(e.getColorUse().getName());
//            String assembleValue = name + "-" +color;
//            if(assembleValue.equalsIgnoreCase(data)) {
//                productSizeColorUSe.set(e);
//                productSizeColorUSe.get().setImagesUses(imageSer.getAllByIdProductAndIdColor(e.getProductUse().getId(),e.getColorUse().getId()));
//            }
//        });
//
//        imagesUses = imageSer.getAllByIdProductAndIdColorMix
//                (productSizeColorUSe.get().getProductUse().getId(), productSizeColorUSe.get().getColorUse().getId());
//        Outfit outfit = OUTFIT_REPO.findByIdProductAndIdColor
//                (productSizeColorUSe.get().getProductUse().getId(), productSizeColorUSe.get().getColorUse().getId());
//        outfit.getProductSizeColors().forEach((e) -> {
//            productSizeColorUSes.add(productSizeColorSer.getDataByOriginal(e));
//        });
//        outfitUse = convertToOtherData.getOutfit(outfit);
//
//        outfitRes.setOutfitsUse(outfitUse);
//        outfitRes.setImagesUses(imagesUses);
//        outfitRes.setProductSizeColorUSe(productSizeColorUSe.get());
//        outfitRes.setProductSizeColorUSes(productSizeColorUSes);
        return outfitRes;
    }

    @Override
    public void addProductMix(Long idOutfit, List<Long> list) {
        Outfit outfit = OUTFIT_REPO.findById(idOutfit).orElseThrow();
        List<Option> options = outfit.getOptions();
        list.forEach(aLong ->  {
            Option option = optionSer.getEnById(aLong);
            options.add(option);
        });
        outfit.setOptions(options);
        OUTFIT_REPO.save(outfit);
    }

    @Override
    public Optional<Outfit> getByIdOfProDuctAndColorAndSize(Long idProduct, Long idColor, Long idSize) {
//        return OUTFIT_REPO.findByIdOfProductAndColorAndSize(idProduct,idColor,idSize);
        return  null;
    }


}
