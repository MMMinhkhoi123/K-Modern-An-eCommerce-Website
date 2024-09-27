package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.response.PriceFilterRes;
import com.leventsclone.leventsclone.data.response.ProductRes;
import com.leventsclone.leventsclone.data.use.EstimateUse;
import com.leventsclone.leventsclone.data.use.OptionUse;
import com.leventsclone.leventsclone.data.use.ProductSizeColorUSe;
import com.leventsclone.leventsclone.data.use.SuggestUse;
import com.leventsclone.leventsclone.entity.Color;
import com.leventsclone.leventsclone.entity.Event;
import com.leventsclone.leventsclone.entity.Option;
import com.leventsclone.leventsclone.entity.Product;

import java.util.List;

public interface IOption {
    List<OptionUse> getBestSeller();
    List<OptionUse> getNewArrive();

    OptionUse getByProductAndColor(Long idProduct, Long idColor);
    List<OptionUse> getAll();

    OptionUse getDetailById(Long idOption);

    ProductRes handlePagination(List<OptionUse> optionUses, int page);

    PriceFilterRes getPriceFilter(List<OptionUse> data);

    Option getEnById(Long idOption);

    List<OptionUse> getAllByProduct(Product product
    );

    List<OptionUse> getAllByEvent(Event event
    );

    SuggestUse getProductSuccessByText(String text, boolean limit);

    List<OptionUse> getAllByTypeProduct(String type);

    void save(Long idProduct, Long idColors, List<String> listImages, Integer price);
    void update(Long idProduct, Long idColors, List<String> listImages, Integer price);

    void  delete(Long id);

    EstimateUse estimateProduct(Integer type);
}
