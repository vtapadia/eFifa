package com.vtapadia.fifa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .authorizeRequests()
                //.antMatchers("/index.html", "/home", "/main/**", "/**/*.js", "/**/*.css").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated();
        http
                .formLogin()
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .logout()
                .permitAll();
        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery("select user_id as username,password,'1' as enable from ef_user where user_id=?")
                .authoritiesByUsernameQuery("select user_id as username,role as authority from ef_roles where user_id=?")
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
        ;
    }
    @Bean
    public StandardPasswordEncoder passwordEncoder() {
        StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder();
        return passwordEncoder;
    }
}
