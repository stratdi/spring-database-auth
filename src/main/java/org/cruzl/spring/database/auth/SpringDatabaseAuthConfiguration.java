package org.cruzl.spring.database.auth;

import org.cruzl.spring.database.auth.model.services.DatabaseUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableCaching
@Configuration
@EnableWebSecurity
@SpringBootApplication
public class SpringDatabaseAuthConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier("databaseUserDetailsService")
	private DatabaseUserDetailsService databaseUserDetailsService;

	public SpringDatabaseAuthConfiguration() {
		log.info("Configuring SpringDatabaseAuthConfiguration...");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		
		http.authorizeRequests().antMatchers("/resources/**", "/login", "/logout").permitAll();
		http.authorizeRequests().requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

		http.authorizeRequests().anyRequest().authenticated();

		http.authorizeRequests().and().formLogin()//
				// Submit URL of login page.
				// .loginProcessingUrl("/j_spring_security_check") // Submit URL
				.loginPage("/login")//
				// .defaultSuccessUrl("/userAccountInfo")//
				.failureUrl("/login?error=true")//
				// .usernameParameter("username")//
				// .passwordParameter("password")
				// Config for Logout Page
				.and().logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout=true");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.databaseUserDetailsService).passwordEncoder(this.passwordEncoder());
	}
//
//	@Bean
//	public AuthenticationManager customAuthenticationManager() throws Exception {
//		return this.authenticationManager();
//	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CacheManager securityCacheManager() {
		return new ConcurrentMapCacheManager("users");
	}
}
