package com.leventsclone.leventsclone.data.use;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuggestUse {
   private List<OptionUse> optionUses = new ArrayList<>();
   private List<String> ListTextSuggest = new ArrayList<>();
   private List<TypeProductUse> typeProductUses = new ArrayList<>();
   private int count;
}
