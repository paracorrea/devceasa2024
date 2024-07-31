package com.flc.springthymeleaf.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import com.flc.springthymeleaf.converter.StringToMercadoConverter;
import com.flc.springthymeleaf.converter.StringToPermissionarioConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private StringToMercadoConverter stringToMercadoConverter;

    @Autowired
    private StringToPermissionarioConverter stringToPermissionarioConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter((Converter<?, ?>) stringToMercadoConverter);
        registry.addConverter((Converter<?, ?>) stringToPermissionarioConverter);
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/script/**")
                .addResourceLocations("classpath:/static/script/")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}
