package gift.config;

import gift.jwt.JwtAuthenticationFilter;
import gift.jwt.JwtTokenProvider;
import gift.jwt.JwtUserDetailsService;
import gift.user.UserService;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		return new JwtAuthenticationFilter(jwtTokenProvider, jwtUserDetailsService);
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
				.requestMatchers(new AntPathRequestMatcher("/login/kakao")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/api/auth/login")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/user/signup")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/user/login")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/api/categories/web")).permitAll() // 추가
				.requestMatchers(new AntPathRequestMatcher("/web/products/list")).permitAll()
				.anyRequest().authenticated())
			.csrf((csrf) -> csrf.disable()) // CSRF 비활성화
			.headers((headers) -> headers.addHeaderWriter(
				new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
			.formLogin((formLogin) -> formLogin.loginPage("/user/login").defaultSuccessUrl("/web/products/list"))
			.logout((logout) -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
				.logoutSuccessUrl("/web/products/list").invalidateHttpSession(true))
			.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(
		AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.passwordEncoder(passwordEncoder())
			.usersByUsernameQuery("SELECT username, password, true FROM site_user WHERE username=?")
			.authoritiesByUsernameQuery("SELECT username, 'ROLE_USER' FROM site_user WHERE username=?");
	}

	@Bean
	public CommandLineRunner init(UserService userService) {
		return args -> {
			userService.create("admin", "admin@example.com", "1234");
		};
	}
}
