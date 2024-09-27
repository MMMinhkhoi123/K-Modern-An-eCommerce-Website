package com.leventsclone.leventsclone.data.use;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CollectionUse {
    private long id;
    private String name;
    private String img;
    private String keyImg;
    private boolean dark;
    private String describe;
    private List<ProductUse> products = new ArrayList<>();
    private String imgAdd;
}
