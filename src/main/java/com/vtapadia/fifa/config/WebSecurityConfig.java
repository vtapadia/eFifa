package com.vtapadia.fifa.config;

import com.vtapadia.fifa.domain.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasAuthority(Roles.DefinedRole.ADMIN.name())
                .antMatchers("/league/**").hasAuthority(Roles.DefinedRole.LEAGUE.name())
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

        log.info("Encoded password String for admin [" + passwordEncoder().encode("admin") + "]");
    }
    @Bean
    public StandardPasswordEncoder passwordEncoder() {
        StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder();
        return passwordEncoder;
    }
}
