package com.brainsinjars.projectbackend.config.security;

import com.brainsinjars.projectbackend.pojo.Role;
import com.brainsinjars.projectbackend.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.SecureRandom;

/**
 * @author Nilesh Pandit
 * @since 10-03-2021
 */

@SuppressWarnings("CommentedOutCode")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthTokenFilter jwtFilter;
    private final RestAuthEntryPoint restAuthEntryPoint;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
                          JwtAuthTokenFilter jwtFilter,
                          RestAuthEntryPoint restAuthEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
        this.restAuthEntryPoint = restAuthEntryPoint;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        SecureRandom random = new SecureRandom();
        random.setSeed(random.generateSeed(20));
        return new BCryptPasswordEncoder(31, random);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() {
        return new ProviderManager(authenticationProvider());
    }

    private DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers(new String[]{"/auth/**"}).permitAll()
                .antMatchers(new String[]{"/**"}).permitAll()
                .anyRequest().hasAnyRole(Role.getAllRoles());

        http.formLogin().disable()
                .exceptionHandling()
                .authenticationEntryPoint(this.restAuthEntryPoint);

//
//        http.cors().and().csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .authorizeRequests().antMatchers("/authenticate").permitAll()
//                .antMatchers("/**").permitAll()
//                .anyRequest().hasAnyRole(Role.getAllRoles());

        // TODO: add forgot password (reset password functionality)
        // TODO: add sign up functionality
        // TODO: configure ant matchers for public and privileged mappings

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }


    /*
    * This CORS configuration gives us more fine grained control
    * over cross origin requests.
    */

    // The below CorsConfigurationSource can be added in http.cors() like so,
    // http.cors(httpCorsConfigurer -> httpCorsConfigurer.configurationSource(corsConfigurationSource()));
/*
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.setAllowedOrigins(Collections.unmodifiableList(Arrays.asList("localhost:3000", "localhost:5000")));
        corsConfig.setAllowedMethods(Collections.unmodifiableList(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH")));

        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        corsConfig.setAllowCredentials(true);


        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        corsConfig.setAllowedHeaders(Collections.unmodifiableList(Arrays.asList("Authorization", "Cache-Control", "Content-Type")));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
*/
}
