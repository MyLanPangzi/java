package com.hiscat.spring.annotation.config.imp;

import com.hiscat.spring.annotation.entity.MyColor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author hiscat
 */
@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
@Import({MyColor.class, MyImportSelector.class, MyImportBeanDefinitionRegistrar.class})
public class ImportConfig {
}
