# 项目说明

### 概况
spring-boot-starter-kaptcha使用springboot3.0.4+jdk17+kaptcha2.3.3
提供yml形式的配置.

### 使用
1.项目引用
```xml
<dependency>
    <groupId>com.shy</groupId>
    <artifactId>spring-boot-starter-kaptcha</artifactId>
    <version>1.0.0</version>
</dependency>
```
2.配置开关
以下配置用来控制是否开启验证码功能,以及是否暴露获取验证码的http接口
接口地址为 /kaptcha/img
```yaml
kaptcha:
  enable: true
  web:
    enable: true
```
其他备选项,可以参考KaptchaProperties的metadata
