/**
 * Copyright 2022 taggd.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ouat.customerService.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@Order(1)
public class AuthConfiguration extends WebSecurityConfigurerAdapter {
    private static final String API_KEY_AUTH_HEADER_NAME = "AccessKey";
    private static final Logger log = LoggerFactory.getLogger(AuthConfiguration.class);

    @Value("${internal.secure.accessKey}")
    private String key;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ApiKeyAuthFilter filter = new ApiKeyAuthFilter(API_KEY_AUTH_HEADER_NAME);
        filter.setAuthenticationManager(new ApiKeyAuthManager(key));
        http.antMatcher("/internal/**").csrf().disable().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().addFilter(filter)
                .authorizeRequests().anyRequest().authenticated();
    }
}
