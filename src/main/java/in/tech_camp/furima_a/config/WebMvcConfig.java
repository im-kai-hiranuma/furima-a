package in.tech_camp.furima_a.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  /**
   * Add handlers to serve static resources such as images, js, and, css
   * files from specific locations under web application root, the classpath,
   * and others.
   * 
   * @see ResourceHandlerRegistry
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/uploads/**")
    .addResourceLocations("file:uploads/");

  }

}
