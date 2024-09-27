package com.leventsclone.leventsclone.data.use;
import com.leventsclone.leventsclone.data.response.OrderRes;
import lombok.Data;

@Data
public class RatingUse {
    private Long id;
    private String title;
    private String content;
    private String Date;
    private int count;
    private String nameAuth;
    private ImageUse imageUse;
    private boolean stateAssess;
    private OrderRes orderRes;
    private OptionUse optionUse;
}
