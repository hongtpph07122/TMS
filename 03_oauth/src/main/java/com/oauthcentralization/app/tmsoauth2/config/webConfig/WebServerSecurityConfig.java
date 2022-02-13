package com.oauthcentralization.app.tmsoauth2.config.webConfig;

import com.oauthcentralization.app.tmsoauth2.config.failureHandlerConfig.AuthenticationFailureHandlerConfigure;
import com.oauthcentralization.app.tmsoauth2.service.serviceImpl.UserDetailsServiceImpl;
import com.oauthcentralization.app.tmsoauth2.utilities.StringUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection"})
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebServerSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Autowired
    @Resource(name = "usersDetailService")
    private UserDetailsService userDetailsService;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwtSecret;

    @Value("${tms.oauth2-server.revoke.allow-call-api}")
    private boolean allowCallApi;

    @Value("${spring.profiles.active}")
    private String profileActive;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() {
        return new UserDetailsServiceImpl();
    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/oauth/token");
        web.ignoring().antMatchers("/webjars/**", "/resources/**");
        web.ignoring().antMatchers("/swagger-ui.html");
    }


    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceBean())
                .passwordEncoder(passwordEncoder());
    }


    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new AuthenticationFailureHandlerConfigure();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public Filter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("x-auth-token");
        config.addExposedHeader("x-total-count");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public JWTAuthorizationFilter jwtAuthorizationFilter() {
        return new JWTAuthorizationFilter(jwtSecret);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if (StringUtility.areEqualText(profileActive, "dev")) {
            http.addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class);
        }

        if (allowCallApi) {
            http.addFilterAfter(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        }

        http
                .authorizeRequests()
                .antMatchers("/oauth/check_token?token=").permitAll()
                .antMatchers("/login", "/logout.do").denyAll()
                .antMatchers("/**").authenticated()
                .and()
                .rememberMe()
                // .rememberMeCookieName("tms")
                // .tokenValiditySeconds(24 * 60 * 60) /* token validity seconds in one day: 24 * 60 * 60 */
                // .tokenRepository(persistentTokenRepository())
                .userDetailsService(userDetailsServiceBean())
        ;

    }


}
