package com.petsoft.employeemanagement.mvc.configuration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * 14.11.2019 18:34
 *
 * @author PetSoft
 */

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{EmployeeManagementConfiguration.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"*.action"};
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[]{new CORSFilter()};
    }
}
