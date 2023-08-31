package dev.maarten.eve_seat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class OAuth2LoginConfig {
    @Value("${eve.client-id}")
    private String clientId;

    @Value("${eve.client-secret}")
    private String clientSecret;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .oauth2Login(withDefaults());
        return http.build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.eveClientRegistration());
    }

    private ClientRegistration eveClientRegistration() {
        return ClientRegistration.withRegistrationId("eve")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8080/login/oauth2/code/eve")
                .scope("publicData")
                .authorizationUri("https://login.eveonline.com/v2/oauth/authorize/")
                .tokenUri("https://login.eveonline.com/v2/oauth/token")
                .userInfoUri("https://esi.evetech.net/verify/?datasource=tranquility")
                .userNameAttributeName("CharacterName")
                .jwkSetUri("https://login.eveonline.com/oauth/jwks")
                .clientName("eve")
                .build();
    }
}
