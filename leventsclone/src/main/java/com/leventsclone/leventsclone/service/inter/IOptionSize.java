package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.use.OptionSizeUSe;
import com.leventsclone.leventsclone.entity.Option;
import com.leventsclone.leventsclone.entity.OptionSize;
import com.leventsclone.leventsclone.entity.Size;

import java.util.List;

public interface IOptionSize {
    List<OptionSizeUSe> getAllByOption(Option option);

    OptionSize getEnByOptionAndSize(Option option, Size size);

    OptionSizeUSe getByOptionAndSize(Option option, Size size);

    OptionSize getEnById(Long idOptionSize);

    void  save(Option option, Long idSize, Integer quantity);

    void  delete(Long id);

    List<OptionSize> getAllEnByOption(Option option);

    void updateQuantity(OptionSize optionSize, Integer quantity);



}
