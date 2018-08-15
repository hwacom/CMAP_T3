package com.cmap.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.cmap.configuration.hibernate.HibernateConfiguration;
import com.cmap.configuration.security.WebSecurityConfig;
import com.opensymphony.sitemesh.webapp.SiteMeshFilter;

public class AppInitializer implements WebApplicationInitializer {
	 
    public void onStartup(ServletContext container) throws ServletException {
 
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(AppConfig.class);
        ctx.register(HibernateConfiguration.class);
        ctx.register(WebSecurityConfig.class);
        container.addListener(new ContextLoaderListener(ctx));
        container.addListener(new RequestContextListener());
        ctx.setServletContext(container);
        
        ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(ctx));
        container.addFilter("SiteMeshFilter", new SiteMeshFilter()).addMappingForUrlPatterns(null, true, "*");

 
        servlet.setLoadOnStartup(1);
//        servlet.setMultipartConfig(new MultipartConfigElement(null, 10485760, 1048576, 1048576));
        servlet.addMapping("/");
    }
 
}
