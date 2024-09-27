package com.leventsclone.leventsclone.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.*;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Configuration
@EnableWebSecurity
public class config {


    @Autowired
    DataSource dataSource;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/admin/*").hasAnyAuthority("admin", "seller")
                                .anyRequest()
                                .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())


                .formLogin((e) -> e.loginPage("/account/login")
                        .loginProcessingUrl("/login-user")
                        .failureUrl("/account/login?error")
                        .defaultSuccessUrl("/#account")
                        .passwordParameter("password")
                        .failureHandler(authenticationFailureHandler())
                        .usernameParameter("gmail")
                )
                .logout((e) -> e.logoutUrl("/logout").logoutSuccessUrl("/"))


                .rememberMe((e) ->
                        e.rememberMeParameter("remember_me").key("uniqueAndSecret").tokenValiditySeconds(1296000)
                                .tokenRepository(persistentTokenRepository()));
        return http.build();
    }



    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return  jdbcTokenRepository;
    }

//    @Bean
//    public AuthenticationFailureHandler authenticationFailureHandlers() {
//        LinkedHashMap<Class<? extends AuthenticationException>, AuthenticationFailureHandler> handlers = new LinkedHashMap<>();
//        handlers.put(CredentialsExpiredException.class, new ForwardAuthenticationFailureHandler("/"));
//        handlers.put(BadCredentialsException.class,  authenticationFailureHandler());
//        AuthenticationFailureHandler defaultHandler = new SimpleUrlAuthenticationFailureHandler("/account/login");
//
//        DelegatingAuthenticationFailureHandler dh = new DelegatingAuthenticationFailureHandler(handlers,
//                defaultHandler);
//        return dh;
//    }

}
