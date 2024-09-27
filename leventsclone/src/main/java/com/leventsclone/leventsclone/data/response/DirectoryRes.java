package com.leventsclone.leventsclone.data.response;

import com.leventsclone.leventsclone.data.use.TypeProductUse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DirectoryRes {
    private long id;
    private String name;
    private  String key;
    private boolean isUsing;
    private Set<TypeProductUse> typeProductUses;
}
