package br.com.zup.adrianoavelino.proposta.compartilhada.seguranca;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${proposta.security.escopo}")
    private String escopo;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(request->
                request
                        .antMatchers(HttpMethod.GET, "/v1/propostas/**").hasAuthority(escopo)
                        .antMatchers(HttpMethod.POST, "/v1/propostas").hasAuthority(escopo)
                        .antMatchers(HttpMethod.POST, "/v1/cartoes/**/biometrias").hasAuthority(escopo)
                        .antMatchers(HttpMethod.POST, "/v1/cartoes/**/bloqueios").hasAuthority(escopo)
                        .antMatchers(HttpMethod.GET, "/actuator/**").hasAuthority(escopo)
                        .anyRequest().authenticated()
        ).oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }
}
