import React, { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../../context/AuthContext';

const KakaoRedirect = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const { login } = useAuth();

    const handleOAuthKakao = async (code) => {
        console.log('handleOAuthKakao 호출됨');
        try {
            console.log('인가 코드:', code);
            // const response = await axios.get(`${process.env.REACT_APP_API_URL}/api/oauth2/kakao/callback?code=${code}`);
            const response = await axios.get(`/api/oauth2/kakao/callback?code=${code}`);
            const data = response.data;
            console.log('카카오 응답 데이터:', data);

            if (data.user) {
                console.log('로그인 성공');
                const tokens = {
                    accessToken: data.accessToken,
                    refreshToken: data.refreshToken,
                };
          
                // Context API를 사용하여 로그인 상태 업데이트
                login(tokens);
          
                sessionStorage.setItem('accessToken', data.accessToken);
                sessionStorage.setItem('refreshToken', data.refreshToken);
        
                try {
                    // const memberResponse = await axios.get(`${process.env.REACT_APP_API_URL}/api/member/me`, {
                    const memberResponse = await axios.get('/api/member/me', {
                        headers: {
                            "Authorization": `Bearer ${data.accessToken}`,
                            "Content-Type": "application/json"
                        },
                        credentials: 'include',
                    });
                    const memberData = memberResponse.data;
                    console.log('회원 정보:', memberData.matchingMode);
                    
                    sessionStorage.setItem('email', memberData.email);
                    sessionStorage.setItem('birth', memberData.birth);
                    sessionStorage.setItem('card', memberData.card);
                    sessionStorage.setItem('gender', memberData.gender);
                    sessionStorage.setItem('hasCard', memberData.hasCard);
                    sessionStorage.setItem('matchingMode', memberData.matchingMode);
                    sessionStorage.setItem('matchingModeLog', memberData.matchingModeLog);
                    sessionStorage.setItem('name', memberData.name);
                    sessionStorage.setItem('nickname', memberData.nickname);
                    sessionStorage.setItem('profileImage', memberData.profileImage);
                    sessionStorage.setItem('searchId', memberData.searchId);
        
                    navigate("/");
                } catch (memberError) {
                    console.error('사용자 정보 가져오기 실패:', memberError.response ? memberError.response.data : memberError.message);
                    navigate("/login");
                }
        
            } else {
                console.log('회원 가입 필요:', data);
                sessionStorage.setItem('email', data.email);
                sessionStorage.setItem('sub', data.sub);
                navigate("/signup");
            }
        } catch (error) {
            if (error.response) {
                console.error('로그인 실패 응답 데이터:', error.response.data);
                console.error('로그인 실패 응답 상태:', error.response.status);
                console.error('로그인 실패 응답 헤더:', error.response.headers);
            } else if (error.request) {
                console.error('로그인 실패 요청 데이터:', error.request);
            } else {
                console.error('로그인 실패 메시지:', error.message);
            }
            navigate("/login");
        }
    };
    
    useEffect(() => {
        console.log('KakaoRedirect useEffect 호출됨');
        // URL에서 인가 코드 추출
        const searchParams = new URLSearchParams(location.search);
        const code = searchParams.get('code');
        if (code) {
            console.log('인가 코드 발견:', code);
            handleOAuthKakao(code);
        } else {
            console.log('인가 코드가 제공되지 않았습니다.');
            navigate("/login");
        }
    }, [location, navigate]);

    return <div>로그인 처리 중...</div>;
};

export default KakaoRedirect;
