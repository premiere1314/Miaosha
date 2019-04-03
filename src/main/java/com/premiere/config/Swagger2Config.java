package com.premiere.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@EnableSwagger2
@Configuration
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        //header中增加 token_id参数，没有可以去掉
        ParameterBuilder token = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        token.name("token_id").description("user token")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build();
        pars.add(token.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 指定controller存放的目录路径
                .apis(RequestHandlerSelectors.basePackage("cn.org.bw.springbootjpademo.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 文档标题
                .title("JPA学习")
                // 文档描述
                .description("1608V正在学习JPA")
                .termsOfServiceUrl("www.baidu.com")
                .version("v1")//定义版本号
                .build();
    }
}
