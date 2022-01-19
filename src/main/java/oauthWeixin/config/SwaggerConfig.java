package oauthWeixin.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Swagger 文档配置
 *
 * @author renq@trasen.cn
 * @date 2022年01月14日 17:23:47
 */
@Configuration
@EnableKnife4j
public class SwaggerConfig {

    @Resource
    private SwaggerProperties swaggerProperties;

    @Resource
    private OpenApiExtensionResolver openApiExtensionResolver;

    /**
     * 创建API
     */
    @PostConstruct
	@Bean
    public void createRestApi() {
		for (SwaggerProperties.Groups group : swaggerProperties.getGroups()) {
			String basePackage = group.getBasePackage();
			Docket docket = new Docket(DocumentationType.OAS_30)
					.enable(swaggerProperties.getEnabled())
					// 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
					.apiInfo(apiInfo())
					// 设置哪些接口暴露给Swagger展示
					.select()
					// 扫描所有有注解的api，用这种方式更灵活
					//.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
					// 扫描指定包中的swagger注解
					.apis(RequestHandlerSelectors.basePackage(basePackage))
					// 扫描所有 .apis(RequestHandlerSelectors.any())
					.paths(PathSelectors.any())
					.build()
					.groupName(group.getName())
                    .extensions(openApiExtensionResolver.buildExtensions(group.getName()));
					//前缀路径
					//.pathMapping(swaggerProperties.getPathMapping());
//			String beanName = StringUtils.substringAfterLast(basePackage, ".") + "Docket";
//			SpringUtils.registerBean(beanName, docket);
		}
    }

    /**
     * 添加摘要信息
     */
    private ApiInfo apiInfo() {
        // 用ApiInfoBuilder进行定制
        SwaggerProperties.Contact contact = swaggerProperties.getContact();
        return new ApiInfoBuilder()
                // 设置标题
                .title(swaggerProperties.getTitle())
                // 描述
                .description(swaggerProperties.getDescription())
                // 作者信息
                .contact(new Contact(contact.getName(), contact.getUrl(), contact.getEmail()))
                // 版本
                .version(swaggerProperties.getVersion())
                .build();
    }
}
