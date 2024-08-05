package com.fanclub.zinzin.global.auth;

import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.entity.Role;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.global.auth.dto.MemberAuthResponseDto;
import com.fanclub.zinzin.global.auth.entity.TempFriend;
import com.fanclub.zinzin.global.auth.repository.TempFriendRepository;
import com.fanclub.zinzin.global.error.code.AuthErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import com.fanclub.zinzin.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final TempFriendRepository tempFriendRepository;

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

    @Value("${kakao.friends-url}")
    private String friendsUrl;

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

    public Map<String, String> generateTokens(Long memberId, String sub, Role role) {
        String accessToken = jwtUtil.generateAccessToken(memberId, sub, role.USER);
        String refreshToken = jwtUtil.generateRefreshToken(memberId, sub, role.USER);
        Map<String, String> tokensMap = new HashMap<>();
        tokensMap.put("accessToken", accessToken);
        tokensMap.put("refreshToken", refreshToken);
        return tokensMap;
    }

    public MemberAuthResponseDto loginOrRegister(Member member, String[] claims, String kakaoAccesstoken) {
        String sub = claims[0];
        String email = claims[1];
        if (member != null) {
            Long memberId = member.getId();
            Map<String, String> tokensMap = generateTokens(memberId, sub, Role.USER);
            String accessToken = tokensMap.get("accessToken");
            String refreshToken = tokensMap.get("refreshToken");

            return MemberAuthResponseDto.createTokenResponse(accessToken, refreshToken);
        }
        saveKakaoFriends(sub, getKakaoFriends(kakaoAccesstoken));
        return MemberAuthResponseDto.createRegisterResponse(sub, email);
    }

    private List<Map<String, Object>> getKakaoFriends(String kakaoAccessToken) {
        String friendsRequestUrl = UriComponentsBuilder.fromHttpUrl(friendsUrl)
                .queryParam("access_token", kakaoAccessToken)
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(friendsRequestUrl, HttpMethod.GET, entity, (Class<Map<String, Object>>) (Class<?>) Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (List<Map<String, Object>>) response.getBody().get("elements");
            }
            throw new BaseException(AuthErrorCode.OAUTH2_AUTHENTICATION_FAILED);
        } catch (HttpClientErrorException e) {
            throw new BaseException(AuthErrorCode.OAUTH2_AUTHENTICATION_FAILED);
        }
    }

    private void saveKakaoFriends(String mySub, List<Map<String, Object>> friendsList) {
        List<TempFriend> tempFriends = friendsList.stream()
                .map(tempFriend -> new TempFriend(mySub, tempFriend.get("id").toString(), (String) tempFriend.get("profile_nickname")))
                .toList();

        tempFriendRepository.saveAll(tempFriends);
    }
}
