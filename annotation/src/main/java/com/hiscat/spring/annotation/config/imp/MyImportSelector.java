package com.hiscat.spring.annotation.config.imp;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        return new String[]{"com.hiscat.spring.annotation.entity.Blue", "com.hiscat.spring.annotation.entity.Yellow"};
    }
}
