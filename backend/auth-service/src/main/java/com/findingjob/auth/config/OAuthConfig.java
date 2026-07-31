package com.findingjob.auth.config;

import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthDefaultSource;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.AuthRequestBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OAuthConfig {

    @Value("${oauth.github.client-id:}")
    private String githubClientId;

    @Value("${oauth.github.client-secret:}")
    private String githubClientSecret;

    @Value("${oauth.github.redirect-uri:http://localhost:8001/api/auth/oauth/github/callback}")
    private String githubRedirectUri;

    @Value("${oauth.gitee.client-id:}")
    private String giteeClientId;

    @Value("${oauth.gitee.client-secret:}")
    private String giteeClientSecret;

    @Value("${oauth.gitee.redirect-uri:http://localhost:8001/api/auth/oauth/gitee/callback}")
    private String giteeRedirectUri;

    @Value("${oauth.wechat.client-id:}")
    private String wechatClientId;

    @Value("${oauth.wechat.client-secret:}")
    private String wechatClientSecret;

    @Bean
    public AuthRequest githubAuthRequest() {
        return AuthRequestBuilder.builder()
                .source(AuthDefaultSource.GITHUB.name())
                .authConfig(AuthConfig.builder()
                        .clientId(githubClientId)
                        .clientSecret(githubClientSecret)
                        .redirectUri(githubRedirectUri)
                        .build())
                .build();
    }

    @Bean
    public AuthRequest giteeAuthRequest() {
        return AuthRequestBuilder.builder()
                .source(AuthDefaultSource.GITEE.name())
                .authConfig(AuthConfig.builder()
                        .clientId(giteeClientId)
                        .clientSecret(giteeClientSecret)
                        .redirectUri(giteeRedirectUri)
                        .build())
                .build();
    }

    /**
     * MVP: provide a map of auth requests by provider name.
     * Wechat and Alipay are reserved for v2.
     */
    public Map<String, AuthRequest> getAuthRequests() {
        Map<String, AuthRequest> requests = new HashMap<>();
        requests.put("github", githubAuthRequest());
        requests.put("gitee", giteeAuthRequest());
        return requests;
    }
}
