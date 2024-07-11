package com.flc.springthymeleaf.converter;

import com.flc.springthymeleaf.domain.Permissionario;
import com.flc.springthymeleaf.service.PermissionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToPermissionarioConverter implements Converter<String, Permissionario> {

    @Autowired
    private PermissionarioService permissionarioService;

    @Override
    public Permissionario convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        return permissionarioService.findByCnpj(source);
    }
}