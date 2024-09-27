package com.leventsclone.leventsclone.data.use;

import lombok.*;

import java.security.Key;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TypeProductUse {
    private long id;
    private String name;
    private String key;
    private Long idDirectory;
    private String nameDirectory;
    private boolean isUsing;
}
