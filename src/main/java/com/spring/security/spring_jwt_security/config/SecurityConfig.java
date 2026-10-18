package com.spring.security.spring_jwt_security.config;

import com.spring.security.spring_jwt_security.JwtFilter;
import com.spring.security.spring_jwt_security.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class SecurityConfig {


    @Autowired
    JwtFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {

        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/newuser", "/login").permitAll()
                        .requestMatchers("/getAllUsers/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                //.authenticationProvider(getDaoUserDetailsService())
                //.authenticationManager(getDaoUserDetailsService())
                /*
                  This is for basi authentication, if you want to use JWT then you can remove this line and add JWT filter in the filter chain.
                 .httpBasic(withDefaults());

                 */
                .addFilterBefore(jwtAuthFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                // 2. Direct authentication failures to return a 401 Unauthorized
                // 3. Direct access denied failures to return a 403 Forbidden
                // If JWT token validation fails, it has to return 401 Unauthorized, so we need to configure the exception handling for that.
                // Else all the failures getting Http status as 403 rather 401.
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));

        return httpSecurity.build();
    }

    /*@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }*/

    @Bean
    public AuthenticationManager getDaoUserDetailsService(UserDetailsService userDetailsService) {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

    /*@Bean
    public UserDetailsService getUserInfoService(){
        return new MyUserDetailsService();
    }*/

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
