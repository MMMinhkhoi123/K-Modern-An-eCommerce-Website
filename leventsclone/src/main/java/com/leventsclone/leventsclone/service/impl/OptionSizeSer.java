package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.data.use.OptionSizeUSe;
import com.leventsclone.leventsclone.entity.Option;
import com.leventsclone.leventsclone.entity.OptionSize;
import com.leventsclone.leventsclone.entity.Size;
import com.leventsclone.leventsclone.repository.IOptionSizeRepo;
import com.leventsclone.leventsclone.service.inter.IOptionSize;
import com.leventsclone.leventsclone.unstil.ConvertToOtherData;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class OptionSizeSer implements IOptionSize {

    private final IOptionSizeRepo OPTION_SIZE_REPO;

    private final SizeSer sizeSer;
    private final ConvertToOtherData convert = new ConvertToOtherData();

    public OptionSizeSer(IOptionSizeRepo iOptionSizeRepo, SizeSer sizeSer) {
        OPTION_SIZE_REPO = iOptionSizeRepo;
        this.sizeSer = sizeSer;
    }
    @Override
    public List<OptionSizeUSe> getAllByOption(Option option) {
        List<OptionSizeUSe> optionSizeUSes = new LinkedList<>();
        OPTION_SIZE_REPO.findAllByOption(option).forEach(optionSize -> {
            OptionSizeUSe optionSizeUSe = convert.getOptionSizeUse(optionSize);
            optionSizeUSes.add(optionSizeUSe);
        });
        return optionSizeUSes;
    }

    @Override
    public OptionSize getEnByOptionAndSize(Option option, Size size) {
        return OPTION_SIZE_REPO.findByOptionAndSize(option, size);
    }

    @Override
    public OptionSizeUSe getByOptionAndSize(Option option, Size size) {
        return convert.getOptionSizeUse(getEnByOptionAndSize(option, size));
    }

    @Override
    public OptionSize getEnById(Long idOptionSize) {
        return OPTION_SIZE_REPO.findById(idOptionSize).orElseThrow();
    }

    @Override
    public void save(Option option, Long idSize, Integer quantity) {
        Size size = sizeSer.getByIdET(idSize);
        OptionSize optionSize = new OptionSize();
        optionSize.setSize(size);
        optionSize.setOption(option);
        optionSize.setQuantity(quantity);
        OPTION_SIZE_REPO.save(optionSize);
    }

    @Override
    public void delete(Long id) {
        OPTION_SIZE_REPO.deleteById(id);
    }

    @Override
    public List<OptionSize> getAllEnByOption(Option option) {
        return OPTION_SIZE_REPO.findAllByOption(option);
    }

    @Override
    public void updateQuantity(OptionSize optionSize, Integer quantity) {
        OptionSize option =  OPTION_SIZE_REPO.findById(optionSize.getId()).orElseThrow();
        option.setQuantity(quantity);
        OPTION_SIZE_REPO.save(option);
    }
}
