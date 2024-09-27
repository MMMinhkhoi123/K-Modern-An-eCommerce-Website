package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.data.response.PriceFilterRes;
import com.leventsclone.leventsclone.data.response.ProductRes;
import com.leventsclone.leventsclone.data.use.EstimateUse;
import com.leventsclone.leventsclone.data.use.OptionUse;
import com.leventsclone.leventsclone.data.use.SuggestUse;
import com.leventsclone.leventsclone.data.use.TypeProductUse;
import com.leventsclone.leventsclone.entity.*;
import com.leventsclone.leventsclone.repository.IOptionRepo;
import com.leventsclone.leventsclone.service.inter.IOption;
import com.leventsclone.leventsclone.unstil.ConvertToOtherData;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class OptionSer implements IOption {
    private final IOptionRepo OPTION_REPO;
    private final OptionImageSer  optionImageSer;
    private final ProductSer productSer;
    private final ColorSer colorSer;
    private final OptionSizeSer optionSizeSer;
    private final  TypeProductSer typeProductSer;
    private final ConvertToOtherData convert = new ConvertToOtherData();


    public OptionSer(ProductSer productSer, ColorSer colorSer,TypeProductSer typeProductSer,IOptionRepo iProductOptionRepo, OptionImageSer optionImageSer, OptionSizeSer optionSizeSer) {
        this.OPTION_REPO = iProductOptionRepo;
        this.optionImageSer = optionImageSer;
        this.optionSizeSer = optionSizeSer;
        this.typeProductSer = typeProductSer;
        this.productSer = productSer;
        this.colorSer = colorSer;
    }

    @Override
    public List<OptionUse> getBestSeller() {
        List<Option> options = new LinkedList<>(OPTION_REPO.findAll());
        List<OptionUse> optionUses = new LinkedList<>();
        // handle seller


        List<EstimateUse> datax = new LinkedList<>();
        options.forEach(option -> {
            AtomicReference<Integer> x = new AtomicReference<>(0);
            List<OptionSize> data =  optionSizeSer.getAllEnByOption(option);
            data.forEach((e) -> {
                e.getOrderDetails().forEach(item -> {
                    if(Objects.equals(item.getOptionSize().getOption().getId(), option.getId())) {
                        x.set(x.get() + item.getQuantity());
                    }
                });
            });
            EstimateUse use = new EstimateUse();
            use.setValue(x.get());
            use.setKey(option.getId());
            datax.add(use);
        });


        Comparator<EstimateUse> x = new Comparator<EstimateUse>() {
            @Override
            public int compare(EstimateUse o1, EstimateUse o2) {
                return Integer.compare(o2.getValue(), o1.getValue());
            }
        };

        datax.sort(x);


        for (int y = 0 ; y < datax.size() ; y ++ ) {
            if(y <=5) {
                Option option = OPTION_REPO.findById(datax.get(y).getKey()).orElseThrow();
                option.getRatings().forEach(rating -> {
                    countRating += rating.getCount();
                });
                OptionUse optionUse = getOptionUSe(option);
                optionUse.setCountRating(  (int) Math.round((double) countRating / option.getRatings().size()));
                optionUses.add(optionUse);
                countRating = 0;
            }
        }


        return optionUses;
    }

    private  int countRating = 0;
    @Override
    public List<OptionUse> getNewArrive() {


        List<OptionUse> optionUses = new LinkedList<>();
        List<OptionUse> optionUses1 = new LinkedList<>();
        OPTION_REPO.findAll().forEach(item ->  {

            item.getRatings().forEach(rating -> {
                countRating += rating.getCount();
            });


              OptionUse optionUse = getOptionUSe(item);
              optionUse.setCountRating(  (int) Math.round((double) countRating / item.getRatings().size()));
            optionUses.add(optionUse);
            countRating = 0;
        });

        Comparator<OptionUse> comparator = new Comparator<OptionUse>() {
            @Override
            public int compare(OptionUse o1, OptionUse o2) {
                return Integer.compare((int) o2.getDate().getTime(), (int) o1.getDate().getTime());
            }
        };
        optionUses.sort(comparator);
        int count = 6;

        for (int i = 0 ; i < count ; i++) {
            optionUses1.add(optionUses.get(i));
        }
        return optionUses1;
    }

    @Override
    public OptionUse getByProductAndColor(Long idProduct, Long idColor) {
        Product product = productSer.getByIdEt(idProduct);
        Color color = colorSer.getColorByIdEt(idColor);
        Option option = OPTION_REPO.findByProductAndColor(product, color);
        return getOptionUSe(option);
    }

    @Override
    public List<OptionUse> getAll() {
        List<Option> options = new LinkedList<>(OPTION_REPO.findAll());
        List<OptionUse> optionUses = new LinkedList<>();
        // handle seller
        // test
        options.forEach(item ->  {

            item.getRatings().forEach(rating -> {
                countRating += rating.getCount();
            });
            OptionUse optionUse = getOptionUSe(item);
            optionUse.setCountRating(  (int) Math.round((double) countRating / item.getRatings().size()));
            optionUses.add(optionUse);
            countRating = 0;
        });

        return optionUses;
    }

    private OptionUse getOptionUSe(Option item) {
        OptionUse data = new OptionUse();
        data.setIdOpt(item.getId());
        int priceSum =  (item.getPrice());
        data.setPrice(priceSum);
        data.setDate(item.getDataCreate());
        data.setColorUse(convert.getColorUse(item.getColor()));
        data.setProductUse(convert.getProductUse(item.getProduct()));
        data.setOptionImageUses(new HashSet<>(optionImageSer.getAllByOption(item)));
        data.setOptionSizeUSes(new HashSet<>(optionSizeSer.getAllByOption(item)));
        data.setUsing(!item.getOptionImages().isEmpty()
                || !item.getRatings().isEmpty()
                || !item.getOptionSize().isEmpty()
                || !item.getOutfits().isEmpty()
                || !item.getFeedbacks().isEmpty());
        return  data;
    }

    @Override
    public OptionUse getDetailById(Long idOption) {
        Option option = OPTION_REPO.findById(idOption).orElseThrow();
        return getOptionUSe(option);
    }

    @Override
    public ProductRes handlePagination(List<OptionUse> optionUses, int page) {
        ProductRes productResUse = new ProductRes();
        List<OptionUse> productRes = new ArrayList<>();

        int limitItemPage = 5;
        int  countPageResult = (int) Math.ceil((double) optionUses.size() / (limitItemPage));
        int indexEnd = page * limitItemPage;
        int indexStart = indexEnd - limitItemPage;
        int countProduct = optionUses.size();

        for (int i = 0 ; i < optionUses.size(); i++) {
            if(i >= indexStart && i < indexEnd) {
                productRes.add(optionUses.get(i));
            }
        }

        productResUse.setOptionUses(optionUses);
        productResUse.setCountProduct(countProduct);
        productResUse.setCountPage(countPageResult);
        productResUse.setThisPage(page);
        return productResUse;
    }

    private int key = 0;
    public List<OptionUse>   dataArray;

    public int handleFilterStatus(
            Optional<String> color,
            Optional<String> size,
            Optional<String> price,
            Optional<String> page,
            Optional<String> sort,
            List<OptionUse> datas
    ) {
        dataArray = datas;
        key = 1;
        sort.ifPresent((item) -> {
            if(!item.isEmpty()) {
                key = 1;
                dataArray = new LinkedList<>(filterSort(item, dataArray));
            }
        });

        page.ifPresent((item) -> {
            if(!item.isEmpty()) {
                key =  Integer.parseInt(item);
            }
        });

        color.ifPresent((item) -> {
            if(!item.isEmpty()) {
                key = 1;
                dataArray =  filterForColor(item, dataArray);
            }
        });

        size.ifPresent((item) -> {
            if(!item.isEmpty()) {
                key = 1;
                dataArray =  filterForSize(item, dataArray);
            }
        });

        price.ifPresent((item) -> {
            if(!item.isEmpty()) {
                key = 1;
                dataArray =  filterForPrice(item, dataArray);
            }
        });
        return key;
    }



    public List<OptionUse> filterSort(String sort, List<OptionUse> data) {
        List<OptionUse> productSizeColorUSes = new LinkedList<>(data);
        Comparator<OptionUse> resComparator = null;
        switch (sort) {
            case "relevance" -> {
            }
            case "best-selling" -> {
//                Predicate<OptionUse> streamsPredicate = item -> item.getSold() > 5;
//                data = data.stream()
//                        .filter(streamsPredicate)
//                        .collect(Collectors.toList());
            }
            case "price-ascending" -> {
                resComparator = new Comparator<OptionUse>() {
                    @Override
                    public int compare(OptionUse o1, OptionUse o2) {
                        return Double.compare(o1.getPriceCorrect() , o2.getPriceCorrect());
                    }
                };
                productSizeColorUSes.sort(resComparator);
            }
            case  "price-descending" -> {
                resComparator = new Comparator<OptionUse>() {
                    @Override
                    public int compare(OptionUse o1, OptionUse o2) {
                        return Double.compare(o2.getPriceCorrect(), o1.getPriceCorrect());
                    }
                };
                productSizeColorUSes.sort(resComparator);
            }
        }
        return  productSizeColorUSes;
    }


    public List<OptionUse> filterForColor(String colors, List<OptionUse> optionUses) {
        List<String> dataColor = List.of(colors.split(","));

        Predicate<OptionUse> streamsPredicate = item -> dataColor.contains(item.getColorUse().getName());
        return optionUses.stream()
                .filter(streamsPredicate)
                .collect(Collectors.toList());
    }

    public List<OptionUse> filterForSize(String sizes, List<OptionUse> optionUses) {

        String replacedString = sizes.replaceAll("\\+", " ");
        List<String> colors = List.of(replacedString.split(","));

        List<OptionUse> optionUses1 = new ArrayList<>();


        optionUses.forEach(optionUse -> {

            colors.forEach(s -> {

                Predicate<OptionUse> streamsPredicate = item -> {

                    AtomicBoolean check = new AtomicBoolean(false);

                    optionUse.getOptionSizeUSes().forEach((e) -> {
                        if(e.getSizeUse().getName().equalsIgnoreCase(s)) {
                            check.set(true);
                        }
                    });

                    return check.get();
                };
                List<OptionUse> optionUses2 = optionUses.stream()
                        .filter(streamsPredicate)
                        .toList();
                if(!optionUses2.isEmpty()) {
                    optionUses1.add(optionUse);
                }
            });
        });
        return optionUses1;
    }

    public List<OptionUse> filterForPrice(String price, List<OptionUse> data) {
        List<String> priceList = List.of(price.split(",")); // => "// 1000A0000" , "1000A" ...
        Predicate<OptionUse> streamsPredicate = item -> checkPrice(priceList, item.getPriceCorrect());
        return data.stream()
                .filter(streamsPredicate)
                .collect(Collectors.toList());
    }

    public boolean checkPriceHandle(String priceStr, double price) {

        boolean statusPrice = true;
        try {
            if(priceStr.split("A").length > 1) {

                if(priceStr.split("A")[0].isEmpty()) {
                    double priceCompare = Double.parseDouble(priceStr.split("A")[1]);
                    if(price > priceCompare) {
                        statusPrice = false;
                    }
                } else  {
                    double minPrice = Double.parseDouble(priceStr.split("A")[0]);
                    double maxPrice = Double.parseDouble(priceStr.split("A")[1]);
                    if (price < minPrice || price > maxPrice) {
                        statusPrice = false;
                    }
                }
            } else  {
                double priceCompare = Double.parseDouble(priceStr.split("A")[0]);
                if (priceStr.lastIndexOf("A")  > 5) {
                    if(price <= priceCompare) {
                        statusPrice = false;
                    }
                }
            }
        } catch (Exception ex) {
            return false;
        }
//        System.out.println(statusPrice);
        return statusPrice;
    }

    public boolean checkPrice(List<String> listPrice, double price) {
        AtomicBoolean statusPrice = new AtomicBoolean(false);
        listPrice.forEach((e) -> {
            if(checkPriceHandle(e,price)) {
                statusPrice.set(true);
            }
        });
        return statusPrice.get();
    }

    @Override
    public PriceFilterRes getPriceFilter(List<OptionUse> data) {
        PriceFilterRes dataReturn = new PriceFilterRes();
        double max = 0;
        double min = 0;

        List<OptionUse> optionUses = new ArrayList<>(data);

        if(data.size() == 2) {
            if(data.get(0).getPriceCorrect()> data.get(1).getPriceCorrect()) {
                max =   data.get(0).getPriceCorrect();
                min =   data.get(1).getPriceCorrect();
            } else {
                min =   data.get(0).getPriceCorrect();
                max =   data.get(1).getPriceCorrect();
            }
        } else if(data.size() == 1)  {
            min =   0;
            max =  data.get(0).getPriceCorrect();
        } else  {
            sort(optionUses);
            min =   optionUses.get(data.size() -1).getPriceCorrect();
            max =   optionUses.get(0).getPriceCorrect();
        }

        dataReturn.setMinPrice(min);
        dataReturn.setMaxPrice(max);


        return dataReturn;
    }

    @Override
    public Option getEnById(Long idOption) {
        return OPTION_REPO.findById(idOption).orElseThrow();
    }

    @Override
    public List<OptionUse> getAllByProduct(Product product) {
        List<Option> options = OPTION_REPO.findAllByProduct(product);
        List<OptionUse> optionUses = new LinkedList<>();
        options.forEach(option -> {
            optionUses.add(getOptionUSe(option));
        });
        return optionUses;
    }

    @Override
    public List<OptionUse> getAllByEvent(Event event) {
        List<OptionUse> optionUses = new LinkedList<>();
        List<Option> options = OPTION_REPO.findAll();

        List<EventProduct> list = event.getEventProduct();

        List<Option> options0 = new LinkedList<>();
        list.forEach(eventProduct -> {

            Predicate<Option> optionPredicate = option -> Objects.equals(option.getProduct().getIdProduct(), eventProduct.getProduct().getIdProduct());
            List<Option> options1 = options.stream().filter(optionPredicate).toList();
            options0.addAll(options1);
        });

        options0.forEach(option ->  {
            optionUses.add(getOptionUSe(option));
        });
        return optionUses;
    }

    @Override
    public SuggestUse getProductSuccessByText(String text, boolean limit) {

        SuggestUse suggestUse = new SuggestUse();


        List<OptionUse> optionUses1 = new ArrayList<>();

        Predicate<OptionUse> streamsPredicate = item -> item.getProductUse().getName().toUpperCase().contains(text.toUpperCase());
        optionUses1 = getBestSeller().stream()
                .filter(streamsPredicate)
                .toList();


        int count = optionUses1.size();

        List<TypeProductUse> typeProductUses = new ArrayList<>();
        int limitGetItem = 6;
        if(!limit) {
            limitGetItem = optionUses1.size();
        }

        for(int i = 0 ; i < optionUses1.size() ; i ++) {
            if(i < limitGetItem) {
                if(checkNameCollection(typeProductUses, optionUses1.get(i).getProductUse().getTypeProductUse())) {
                    typeProductUses.add(optionUses1.get(i).getProductUse().getTypeProductUse());
                }
            }
        }
        suggestUse.setOptionUses(optionUses1);
        suggestUse.setCount(count);
        suggestUse.setTypeProductUses(typeProductUses);
        suggestUse.setListTextSuggest(new LinkedList<>());
        return suggestUse;
    }

    @Override
    public List<OptionUse> getAllByTypeProduct(String nameType) {
        List<OptionUse> listResult = new ArrayList<>();
        TypeProduct type = typeProductSer.getByName(nameType);
        List<TypeProductUse> typeProducts;

        if (type.getKeyTypeProduct().equalsIgnoreCase(type.getDirectory().getNameDirectory())) {
            typeProducts = typeProductSer.getALlByIdDirectory(type.getDirectory().getIdDirectory());
        } else {
            TypeProductUse data = convert.getTypeProductUse(type);
            typeProducts = new ArrayList<>();
            typeProducts.add(data);
        }
        Predicate<OptionUse> streamsPredicate = item -> checkTypeProduct(typeProducts, item.getProductUse().getTypeProductUse());
        listResult = getBestSeller().stream()
                .filter(streamsPredicate)
                .toList();
        return listResult;
    }




    @Override
    public void save(Long idProduct, Long idColors, List<String> listImages, Integer price) {
        Product product = productSer.getByIdEt(idProduct);
        Color color = colorSer.getColorByIdEt(idColors);
        Option option = new Option();
        option.setColor(color);
        option.setProduct(product);
        option.setPrice(price);
        Option optionAfterSave = OPTION_REPO.save(option);
        optionImageSer.save(listImages, optionAfterSave);
    }

    @Override
    public void update(Long idProduct, Long idColors, List<String> listImages, Integer price) {
        Product product = productSer.getByIdEt(idProduct);
        Color color = colorSer.getColorByIdEt(idColors);
        Option option = OPTION_REPO.findByProductAndColor(product, color);
        option.setPrice(price);
        Option optionAfterUpdate =  OPTION_REPO.save(option);
        optionImageSer.update(listImages, optionAfterUpdate);
    }

    @Override
    public void delete(Long id) {
        OPTION_REPO.deleteById(id);
    }

    @Override
    public EstimateUse estimateProduct(Integer type) {
        EstimateUse estimateUse = new EstimateUse();

        OPTION_REPO.findAll().forEach((e) -> {
            e.getOptionSize().forEach(optionSize -> {
                optionSize.getOrderDetails().forEach(orderDetail -> {
                    System.out.println(orderDetail.getOrder().getIdOrder());
                });
            });
        });


        return null;
    }


    private  boolean checkTypeProduct(List<TypeProductUse> typeProductUses , TypeProductUse data) {
        AtomicBoolean status = new AtomicBoolean(false);
        typeProductUses.forEach((e) -> {
            if (e.getId() == data.getId()) {
                status.set(true);
            }
        });
        return status.get();
    }

    private boolean checkNameCollection(List<TypeProductUse> typeProductUses, TypeProductUse typeProductUse) {
        List<TypeProductUse> typeProductUses1 = new ArrayList<>();
        Predicate<TypeProductUse> streamsPredicate = item -> Objects.equals(item.getKey(), typeProductUse.getKey());
        typeProductUses1 = typeProductUses.stream()
                .filter(streamsPredicate)
                .toList();
        if(!typeProductUses1.isEmpty()) {
            return  false;
        }else  {
            return  true;
        }
    }

    public void sort(List<OptionUse> data) {
        Comparator<OptionUse> resComparator = new Comparator<OptionUse>() {
            @Override
            public int compare(OptionUse o1, OptionUse o2) {
                return Double.compare(o2.getProductUse().getPrice(), o1.getProductUse().getPrice());
            }
        };
        if(!data.isEmpty()) {
            data.sort(resComparator);
        }
    }

}
