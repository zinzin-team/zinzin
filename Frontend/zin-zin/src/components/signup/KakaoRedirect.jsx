import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import apiClient from '../../api/apiClient';
import { useAuth } from '../../context/AuthContext';
import { jwtDecode } from 'jwt-decode';
import styles from './KakaoRedirect.module.css'; // 로딩 하트를 위한 CSS 모듈 추가

const KakaoRedirect = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const { login } = useAuth();
    const [loading, setLoading] = useState(true); // 로딩 상태 추가

    const handleOAuthKakao = async (code) => {
        try {
            const response = await apiClient.get(`/api/oauth2/kakao/callback?code=${code}`); // code : 인가 코드
            const data = response.data; // 카카오 응답 데이터

            if (data.user) {
                // 로그인 성공
                const tokens = {
                    accessToken: data.accessToken,
                };

                // Context API를 사용하여 로그인 상태 업데이트
                login(tokens);

                sessionStorage.setItem('accessToken', data.accessToken);

                try {
                    const decodedToken = jwtDecode(data.accessToken); // 토큰 전체 내용
                    const memberId = decodedToken.memberId; // 추출된 memberId
                    sessionStorage.setItem('memberId', memberId);
                } catch (decodeError) {
                    // console.error('토큰 디코딩 실패:', decodeError.message);
                    navigate("/login");
                    return; // 이후 코드 실행을 막기 위해 리턴
                }
        
                try {
                    const memberResponse = await apiClient.get('/api/member/me', {
                        headers: {
                            "Content-Type": "application/json"
                        },
                        credentials: 'include',
                    });
                    const memberData = memberResponse.data; // 회원 정보

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
                    // console.error('사용자 정보 가져오기 실패:', memberError.response ? memberError.response.data : memberError.message);
                    navigate("/login");
                }

            } else {
                // 회원 가입 필요
                sessionStorage.setItem('email', data.email);
                sessionStorage.setItem('sub', data.sub);
                navigate("/signup");
            }
        } catch (error) {
            // if (error.response) {
            //     console.error('로그인 실패 응답 데이터:', error.response.data);
            //     console.error('로그인 실패 응답 상태:', error.response.status);
            //     console.error('로그인 실패 응답 헤더:', error.response.headers);
            // } else if (error.request) {
            //     console.error('로그인 실패 요청 데이터:', error.request);
            // } else {
            //     console.error('로그인 실패 메시지:', error.message);
            // }
            navigate("/login");
        } finally {
            setLoading(false); // 로딩 완료 후 로딩 상태를 false로 설정
        }
    };
    
    useEffect(() => {
        // KakaoRedirect useEffect 호출됨

        // URL에서 인가 코드 추출
        const searchParams = new URLSearchParams(location.search);
        const code = searchParams.get('code');
        if (code) {
            // 인가 코드 발견
            handleOAuthKakao(code);
        } else {
            // 인가 코드가 제공되지 않음
            navigate("/login");
        }
    }, [location, navigate]);

    if (loading) {
        return (
            <div className={styles.spinner}>
                <div className={`${styles.heart} ${styles.heart1}`}></div>
                <div className={`${styles.heart} ${styles.heart2}`}></div>
                <div className={`${styles.heart} ${styles.heart3}`}></div>
                <div className={styles.loadingtext}>
                    Loading
                    <span className={styles.dot1}>.</span>
                    <span className={styles.dot2}>.</span>
                    <span className={styles.dot3}>.</span>
                </div>
            </div>
        );
    }

    return <div>로그인 처리 중...</div>;
};

export default KakaoRedirect;
