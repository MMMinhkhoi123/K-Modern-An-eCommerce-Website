package com.leventsclone.leventsclone.data.request;

import lombok.Data;

@Data
public class OderDefectReq {
    private Long idOption;
    private Long idSize;
    private int quantity;
    private String describe;
}
