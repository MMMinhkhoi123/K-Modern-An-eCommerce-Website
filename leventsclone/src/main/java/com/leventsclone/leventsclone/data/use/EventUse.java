package com.leventsclone.leventsclone.data.use;

import lombok.Data;

@Data
public class EventUse {
    private String name;
    private Long id;
    private Long dateStart;
    private Long dateEnd;
    private Boolean state;
    private String path;
}
