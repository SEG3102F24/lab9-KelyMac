package seg3x02.tempconverterapi

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class SecurityConfiguration {

    data class AppUser(val username: String, val password: String, val role: String)

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers("/temperature-converter/**").permitAll()
                    .anyRequest().authenticated()
            }
            .httpBasic()
            .and()
            .csrf().disable()
            .build()
    }

    @Bean
    fun userDetailsService(encoder: PasswordEncoder): UserDetailsService {
        val users = listOf(
            AppUser("user1", "pass1", "USER"),
            AppUser("user2", "pass2", "ADMIN")
        )

        val manager = InMemoryUserDetailsManager()

        users.forEach { appUser ->
            val userDetails = User.builder()
                .username(appUser.username)
                .password(encoder.encode(appUser.password))
                .roles(appUser.role)
                .build()
            manager.createUser(userDetails)
        }

        return manager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
