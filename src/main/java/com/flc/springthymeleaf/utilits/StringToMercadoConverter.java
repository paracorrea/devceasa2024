package com.flc.springthymeleaf.utilits;



import com.flc.springthymeleaf.domain.Mercado;
import com.flc.springthymeleaf.service.MercadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToMercadoConverter implements Converter<String, Mercado> {

    @Autowired
    private MercadoService mercadoService;

    @Override
    public Mercado convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        Integer id = Integer.valueOf(source);
        return mercadoService.findById(id);
    }
}
