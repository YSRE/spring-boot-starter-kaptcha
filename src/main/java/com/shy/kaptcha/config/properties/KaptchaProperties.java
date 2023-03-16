package com.shy.kaptcha.config.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Properties;

import static com.google.code.kaptcha.Constants.*;

/**
 * @link com.google.code.kaptcha.Constants}
 * 此处是按照实体类嵌套进行property结构映射的. 也可以参考kafka,全用@Value绑定propertyKey
 * @author shy
 * @date 2023/3/15
 */
@Data
@Component("kaptchaProperties")
@ConfigurationProperties(prefix = "kaptcha")
public class KaptchaProperties{
    private boolean enable = true;

    private Web web = new Web();

    private SessenConfig session = new SessenConfig();

    @NestedConfigurationProperty
    private Border border = new Border();
    @NestedConfigurationProperty
    private Noise noise = new Noise();

    /** 图片样式 **/
    @Value("${kaptcha.obscurificator.impl:com.google.code.kaptcha.impl.WaterRipple}")
    private String obscurificatorImpl;

    /** 图片实现类 **/
    @Value("${kaptcha.producer.impl:com.google.code.kaptcha.impl.DefaultKaptcha}")
    private String producerImpl;
    /** 文本实现类 **/
    @NestedConfigurationProperty
    private TextProducer textproducer = new TextProducer();
    /** 文字渲染器 **/
    @Value("${kaptcha.word.impl:com.google.code.kaptcha.text.impl.DefaultWordRenderer}")
    private String wordImpl;

    @NestedConfigurationProperty
    private Background background = new Background();

    @NestedConfigurationProperty
    private Image image = new Image();

    public Properties toProperties(){
        Properties p = new Properties();
        if(!this.enable) return p;
        renderSessionConfig(p);
        renderBorder(p);
        renderNoise(p);
        p.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL,this.obscurificatorImpl);
        p.setProperty(KAPTCHA_PRODUCER_IMPL,this.producerImpl);
        renderTextProducer(p);
        renderBackground(p);
        p.setProperty(KAPTCHA_WORDRENDERER_IMPL,wordImpl);
        renderImage(p);
        return p;
    }

    private void renderSessionConfig(Properties p){
        p.setProperty(KAPTCHA_SESSION_CONFIG_KEY,this.session.key);
        p.setProperty(KAPTCHA_SESSION_CONFIG_DATE,this.session.date);
    }

    private void renderBorder(Properties p){
        //google的原始解析逻辑:没配置就默认false;
        if(!this.border.enable) {
            p.setProperty(KAPTCHA_BORDER,"no");
            return;
        }
        p.setProperty(KAPTCHA_BORDER,"yes");
        p.setProperty(KAPTCHA_BORDER_COLOR, border.color);
        p.setProperty(KAPTCHA_BORDER_THICKNESS,border.thickness);
    }

    private void renderNoise(Properties p){
        p.setProperty(KAPTCHA_NOISE_COLOR, noise.color);
        p.setProperty(KAPTCHA_NOISE_IMPL, noise.impl);
    }

    private void renderTextProducer(Properties p){
        p.setProperty(KAPTCHA_TEXTPRODUCER_IMPL,this.textproducer.impl);
        p.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_STRING,this.textproducer.textChar.string);
        p.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH,this.textproducer.textChar.length);
        p.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE,this.textproducer.textChar.space);
        p.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES,this.textproducer.font.names);
        p.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR,this.textproducer.font.color);
        p.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE,this.textproducer.font.size);
    }

    private void renderBackground(Properties p){
        p.setProperty(KAPTCHA_BACKGROUND_IMPL,this.background.impl);
        p.setProperty(KAPTCHA_BACKGROUND_CLR_FROM,this.background.clear.from);
        p.setProperty(KAPTCHA_BACKGROUND_CLR_TO,this.background.clear.to);
    }

    private void renderImage(Properties p){
        p.setProperty(KAPTCHA_IMAGE_WIDTH,this.image.width);
        p.setProperty(KAPTCHA_IMAGE_HEIGHT,this.image.height);
    }

    @Data
    public static class Web{
        /** 是否启用web接口. 路径为 /kaptcha/img **/
        private boolean enable = true;
    }

    @Data
    public static class SessenConfig{
        private String key = KAPTCHA_SESSION_KEY;
        private String date = KAPTCHA_SESSION_DATE;
    }


    @Data
    public static class Border{
        /** 是否启用边框 true or false **/
        private boolean enable = true;
        /** 边框颜色 合法值:(r,g,b) 或颜色名:{@link Color}默认black **/
        private String color = "black";
        /** 边框宽度 **/
        private String thickness = "1";
    }

    @Data
    public static class Noise{
        /** 噪点颜色 **/
        private String color = "black";
        /** 噪点实现类.默认无噪点 **/
        private String impl = "com.google.code.kaptcha.impl.NoNoise";
    }

    @Data
    public static class TextProducer{
        /** 文字实现类 **/
        private String impl = "com.google.code.kaptcha.text.impl.DefaultTextCreator";
        private TextChar textChar = new TextChar();
        private TextFont font = new TextFont();


    }
    @Data
    public static class TextChar{
        /** 字符串,验证码里的字符从里面取 **/
        private String string = "abcde2345678gfynmnpwx";
        /** 验证码长度 **/
        private String length = "5";
        /** 字符间隔 **/
        private String space = "2";
    }

    @Data
    public static class TextFont{
        /**
         * 验证码文本字体样式 默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
         */
        private String names = "Arial,Courier";
        /** 字体颜色 **/
        private String color = "black";
        /** 字体大小 **/
        private String size = "40";
    }

    @Data
    public static class Background{
        /** 背景实现类 **/
        private String impl = "com.google.code.kaptcha.impl.DefaultBackground";
        /** 渐变色 **/
        private Clear clear = new Clear();


        @Getter
        @Setter
        public class Clear{
            private String from = "LIGHT_GRAY";
            private String to = "white";
        }
    }

    @Data
    public static class Image{
        /** 图片宽度 **/
        private String width = "200";
        /** 图片高度 **/
        private String height = "50";
    }
}
