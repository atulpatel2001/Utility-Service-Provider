package com.exe.online.configration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MySecurity {

    @Bean
    UserDetailsService getUserDetailsService() {
        return new UserDetailServiceImple();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider getDaoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.getUserDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth.requestMatchers("/admin/**")
                        .hasRole("ADMIN")
                        .requestMatchers("/user/**")
                        .hasRole("USER")
                        .requestMatchers("/service-provider/**").
                         hasRole("SERVICEMAN")
                        .requestMatchers("/**")
                        .permitAll()
                        .anyRequest().authenticated()).formLogin(form -> form.loginPage("/home/signin").loginProcessingUrl("/doLogin").defaultSuccessUrl("/utilityservices/user/index", true).successHandler((request, response, authentication) -> {
                    for (GrantedAuthority auth : authentication.getAuthorities()) {
                        if (auth.getAuthority().equals("ROLE_ADMIN")) {
                            response.sendRedirect("/utilityservices/admin/index");
                            return;
                        } else if (auth.getAuthority().equals("ROLE_SERVICEMAN")) {
                            response.sendRedirect("/utilityservices/service-provider/index");
                            return;
                        }

                    }
                    response.sendRedirect("/utilityservices/user/index");
                }));

        return httpSecurity.build();
    }
}

