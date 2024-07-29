package com.fanclub.zinzin.global.auth;

import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.global.error.code.AuthErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @Value("${kakao.client.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.auth-url}")
    private String authUrl;

    @Value("${kakao.token-url}")
    private String tokenUrl;

    @Value("${kakao.user-info-url}")
    private String userInfoUrl;

    @Value("${kakao.scope}")
    private String scope;

    public String getKakaoAuthorizeUrl() {
        return UriComponentsBuilder.fromHttpUrl(authUrl)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", scope)
                .toUriString();
    }

    public ArrayList<String> getKakaoTokens(String code) {
        try {
            String tokenRequestUrl = UriComponentsBuilder.fromHttpUrl(tokenUrl)
                    .queryParam("grant_type", "authorization_code")
                    .queryParam("client_id", clientId)
                    .queryParam("redirect_uri", redirectUri)
                    .queryParam("code", code)
                    .queryParam("client_secret", clientSecret)
                    .toUriString();

            Map<String, String> response = restTemplate.postForObject(tokenRequestUrl, null, Map.class);
            ArrayList<String> tokens = new ArrayList<>();
            tokens.add(response.get("access_token"));
            tokens.add(response.get("refresh_token"));
            tokens.add(response.get("id_token"));
            return tokens;
        } catch (Exception e) {
            throw new BaseException(AuthErrorCode.OAUTH2_AUTHENTICATION_FAILED);
        }
    }
}
