package com.hike.security;

import com.hike.service.impl.CustomOAuth2UserService;
import com.hike.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.regex.Pattern;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private CustomUserDetailsService userDetailsService;
    private CustomOAuth2UserService oauth2UserService;
    private UserService userService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, CustomOAuth2UserService oauth2UserService, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.oauth2UserService = oauth2UserService;
        this.userService = userService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/","/404","/login", "/register","/subscribe","/parolaUitata/**","/resetParola/**","/contact","/user/getProfilePhoto/**","/blog","/blog/getBlogPhoto/**", "/blog/categorie/**", "/marcaje/getMarcajPhoto/**","/grupaMuntoasa/**","/trasee","/trasee/getTraseuPhotos/**", "/dezabonare/*")
                .permitAll()
                .requestMatchers(request ->
                        request.getServletPath().matches("/blog/\\d+(\\?[a-zA-Z]+)?")
                )
                .permitAll()
                .requestMatchers(request ->
                        request.getServletPath().matches("/trasee/\\d+(\\?[a-zA-Z]+)?")
                )
                .permitAll()
                .requestMatchers("/admin/**", "/blog/categorii/**", "/marcaje/**","/salvamont/**", "/trasee/propuneri","/trasee/*/sterge","/trasee/*/aproba","/trasee/*/respinge","/trasee/*/edit","/trasee/edit","/trasee/actualizareStiri")
                .hasAuthority("ADMIN")
                //for the blog additional role checks are done in the controller
                .requestMatchers("/user/**", "/blog/postarilemele", "/blog/adauga", "/blog/*/sterge", "/blog/*/*/sterge","/trasee/*/*/sterge", "/trasee/adauga","/trasee/traseeAdaugate","/trasee/traseeParcurse","/trasee/*/parcurs","/logout")
                .authenticated()
                .and()
                .formLogin()
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .loginProcessingUrl("/login")
                        .failureUrl("/login?error=true")
                .successHandler((request, response, authentication) -> {
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                        session.removeAttribute(WebAttributes.ACCESS_DENIED_403);
                        session.removeAttribute("LAST_EXCEPTION");
                        session.removeAttribute("USERNAME");
                    }
                    response.sendRedirect("/");
                })
                        .permitAll()
                .and()
                .oauth2Login()
                    .loginPage("/login")
                    .failureUrl("/login?error=true")
                    .userInfoEndpoint().userService(oauth2UserService)
                    .and()
                    .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                        Authentication authentication) throws IOException, ServletException {

                        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
                        userService.processOAuthPostLogin(oauthUser.getFullName(), oauthUser.getEmail(), oauthUser.getLastName(), oauthUser.getFirstName(), oauthUser.getGender(), oauthUser.getPhoto());
                        response.sendRedirect("/");
                    }
                })
                .and()
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessUrl("/")
                                .permitAll()
                )
                .exceptionHandling()
                .accessDeniedPage("/404")
                .and()
                .rememberMe()
                .key("hikeitkey")
                .tokenValiditySeconds(7 * 24 * 60 * 60);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/css/**", "/fonts/**", "/img/**", "/js/**", "/scss/**");
    }

    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
