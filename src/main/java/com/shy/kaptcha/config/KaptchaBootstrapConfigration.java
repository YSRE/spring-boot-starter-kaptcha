package com.shy.kaptcha.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.shy.kaptcha.config.properties.KaptchaProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import java.util.Properties;

/**
 * @author shy
 * @date 2023/3/15
 */
@AutoConfiguration
@EnableConfigurationProperties(KaptchaProperties.class)
public class KaptchaBootstrapConfigration {

    /**
     * 默认的producer bean.使用者也可以替换
     * @param kp
     * @return
     */
    @Bean
    @DependsOn("kaptchaProperties")
    @ConditionalOnMissingBean(Producer.class)
    public Producer defaultProducer(@Qualifier("kaptchaProperties") KaptchaProperties kp){
        Properties properties = kp.toProperties();
        DefaultKaptcha dk = new DefaultKaptcha();
        dk.setConfig(new Config(properties));
        return dk;
    }
}
