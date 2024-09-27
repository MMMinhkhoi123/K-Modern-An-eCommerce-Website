package com.leventsclone.leventsclone.custome;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "upload")
public class UploadProperties {
    private String directory;
    private String common;
    private String product;
    private String outfit;
}
