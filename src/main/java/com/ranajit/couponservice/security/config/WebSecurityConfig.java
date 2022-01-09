package com.ranajit.couponservice.security.config;

import com.ranajit.couponservice.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic();
        httpSecurity.authorizeRequests()
                .mvcMatchers(HttpMethod.GET,
                        "/couponapi/coupons/{code:^[A-Z,a-z,0-9]*$}", "/index","/showGetCoupon", "/getCoupon",
                        "/couponDetails")
                //.hasAnyRole("USER", "ADMIN")
                .permitAll()
                .mvcMatchers(HttpMethod.GET, "/showCreateCoupon", "/createCoupon", "/createResponse").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.POST, "/getCoupon").hasAnyRole("USER", "ADMIN")
                .mvcMatchers(HttpMethod.POST, "/couponapi/coupons", "/saveCoupon",
                        "/getCoupon").hasRole("ADMIN")
                .mvcMatchers("/login","/","/showRegistration","/registerUser").permitAll()
                .anyRequest().denyAll().and()
//                .csrf().disable()
                .logout().logoutSuccessUrl("/");

        httpSecurity.cors(corsCustomizer->{
            CorsConfigurationSource corsConfigurationSource = httpServletRequest -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(List.of("localhost:3000"));
                corsConfiguration.setAllowedMethods(List.of("GET"));
                return corsConfiguration;
            };
            corsCustomizer.configurationSource(corsConfigurationSource);
        });
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

}

