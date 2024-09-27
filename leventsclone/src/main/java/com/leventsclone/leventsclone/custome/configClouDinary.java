package com.leventsclone.leventsclone.custome;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class configClouDinary {
    @Bean
    public Cloudinary getCloudinary(){
        Map config = new HashMap();
        config.put("cloud_name", "dbsr61csr");
        config.put("api_key", "157341899761611");
        config.put("api_secret", "Af2jbfXru0HEjx8OhFF2ASdHXmQ");
        config.put("secure", true);
        return new Cloudinary(config);
    }
}
