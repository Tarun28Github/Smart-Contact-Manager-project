package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.services.impl.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {

    // This is how to create a user with its username and password. And save it, in
    // memory.

    // @Bean
    // public UserDetailsService UserDetailsService(){

    // UserDetails user1=
    // User.withUsername("Tarun").password(passwordEncoder().encode("tarun123")).build();

    // return new InMemoryUserDetailsManager(user1);

    // }

    // @Bean
    // public PasswordEncoder passwordEncoder(){

    // return new BCryptPasswordEncoder();
    // }

    
    // creating user and saving user form/in database.

    @Autowired
    private AuthFailureHandler authFailureHandler;

    @Autowired
    private SecurityCustomUserDetailService userDetailService;

    @Autowired
    private OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessfulHandler;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailService);

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/scm/user/**").authenticated()
                        .anyRequest().permitAll()
                        );

                        // http.formLogin(Customizer.withDefaults());

                http.formLogin(login->
                login.loginPage("/scm/login")
                .loginProcessingUrl("/authenticate")
                .defaultSuccessUrl("/scm/user/dashboard")
                // .failureForwardUrl("/scm/login?error=true")
                .usernameParameter("email")
                .passwordParameter("password") 
                .failureHandler(authFailureHandler)
                );

                

                http.csrf(AbstractHttpConfigurer::disable);

                http.logout((logout) -> logout
                .permitAll()
                .logoutUrl("/do-logout")
                .logoutSuccessUrl("/scm/login?logout=true")

                
                
                );


                
                http.oauth2Login(oauth-> oauth.loginPage("/scm/login")
                .successHandler(oAuthAuthenticationSuccessfulHandler)
                );

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public AuthenticationManager  authenticationManager(AuthenticationConfiguration config) throws Exception{
    //     return  config.getAuthenticationManager();
    // }
}
