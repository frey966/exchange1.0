package com.financia.gateway.swagger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import javax.annotation.Resource;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


    @Primary
    @Component
    @Slf4j
    public class SwaggerProvider implements SwaggerResourcesProvider {

        /**
         * swagger默认的url后缀
         */
        protected static final String API_URI = "/v2/api-docs";

        /**
         * 网关路由 对应配置文件的spring.cloud.gateway.route的路由集合
         */
        @Resource
        private RouteLocator routeLocator;

        /**
         * 网关应用名称
         */
        @Value("${spring.application.name}")
        private String appName;


        @Override
        public List<SwaggerResource> get() {
            List<SwaggerResource> resources = new ArrayList<>();
        /*SwaggerResource gatewayResource = new SwaggerResource();
        gatewayResource.setUrl(API_URI);
        gatewayResource.setName("gateway");
        resources.add(gatewayResource);*/

            Map<String, URI> routesMap = new HashMap<>(8);
            // 获取网关中配置的路由
            routeLocator.getRoutes().filter(route -> route.getUri().getHost() != null)
                    .filter(route -> !appName.equals(route.getUri().getHost()))
                    .subscribe(route -> routesMap.put(route.getUri().getHost(), route.getUri()));

            routesMap.forEach((name, uri) -> {
                String url = "/" + uri.getHost() + API_URI;
                SwaggerResource swaggerResource = new SwaggerResource();
                swaggerResource.setUrl(url);
                swaggerResource.setUrl(url);
                swaggerResource.setName(name);
                swaggerResource.setSwaggerVersion("2.0");
                resources.add(swaggerResource);
            });
            log.info("routesMap===============:" + routesMap.toString());
            return resources;
        }
    }

