package de.eicke.receipts;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
 
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
 
    private static final String RESOURCE_ID = "travelmanager-service";
     
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(false);
    }
 
    @Override
    public void configure(HttpSecurity http) throws Exception {
    	http.httpBasic().disable().
            authorizeRequests().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
                    "/swagger-ui.html", "/webjars/**", "/swagger-resources/configuration/ui", "/swagger-ui.html",
                    "/swagger-resources/configuration/security").anonymous()            
            	.anyRequest().hasRole("USER");
    }
}
