package com.leventsclone.leventsclone.unstil;

public class Extra {
    public  String getUrlVid(String name) {
        String level1 = name.replaceAll(" ", "-");
        return level1.replaceAll("/" , "-");
    }
    
}
