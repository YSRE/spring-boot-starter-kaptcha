package com.shy.kaptcha.controller;

import com.google.code.kaptcha.Producer;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * @author shy
 * @date 2023/3/16
 */
@ConditionalOnProperty(name = {"kaptcha.enable","kaptcha.web.enable"},havingValue = "true")
@Controller
@RequestMapping("/kaptcha")
public class DefaultKaptchaController {

    @Autowired
    private Producer producer;

    @GetMapping("/img")
    public void getCode(HttpServletResponse response){
        String text = renderImage(response);
        handleKaptchaText(text);
    }

    protected String renderImage(HttpServletResponse response){
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate");
        response.addHeader(HttpHeaders.CACHE_CONTROL, "post-check=0, pre-check=0");
        response.setHeader(HttpHeaders.PRAGMA, "no-cache");
        response.setContentType("image/jpeg");
        String text = producer.createText();
        try (ServletOutputStream out = response.getOutputStream()) {
            ImageIO.write(producer.createImage(text), "jpg", out);
        } catch (IOException e) {
            throw new RuntimeException("获取验证码失败");
        }
        return text;
    }

    protected void handleKaptchaText(String text){
        // 业务里可以将验证码的存储到redis里,
    }
}
