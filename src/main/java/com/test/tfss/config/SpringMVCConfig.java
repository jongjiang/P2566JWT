package com.test.tfss.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.test.tfss.service.interceptor.EmployeeSecurityInterceptor;

@Configuration
public class SpringMVCConfig implements WebMvcConfigurer {

	@Autowired
	EmployeeSecurityInterceptor employeeSecurityInterceptor;

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(employeeSecurityInterceptor)
		        .addPathPatterns("/**")
		        .excludePathPatterns("/auth/**");
	}

}
