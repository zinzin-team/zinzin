import React, { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';

const KakaoRedirect = () => {
    const location = useLocation();
    const navigate = useNavigate();

    const handleOAuthKakao = async (code) => {
        try {
            const response = await axios.get(`http://localhost:8080/api/oauth2/kakao/callback?code=${code}`);
            const data = response.data;
            if (data.user) {
                alert("로그인 성공");
                console.log(data)
                sessionStorage.setItem('accessToken', data.accessToken);
                sessionStorage.setItem('refreshToken', data.refreshToken);

                // matchingMode 저장
                console.log(data.accessToken)
                const memberResponse = await axios.get('http://localhost:8080/api/member/me', {
                    headers: {
                        "Authorization": `Bearer ${data.accessToken}`
                    }
                });
                const memberData = memberResponse.data;
                sessionStorage.setItem('matchingMode', memberData.matchingMode);

                navigate("/");

            } else {
                console.log("회원 가입 필요:", data);
                alert("회원 가입 필요");
                sessionStorage.setItem('email', data.email);
                sessionStorage.setItem('sub', data.sub);
                navigate("/signup");
            }
        } catch (error) {
            console.error("로그인 실패:", error.response ? error.response.data : error.message);
            alert("로그인 중 오류가 발생했습니다.");
            navigate("/login");
        }
    };

    useEffect(() => {
        // URL에서 인가 코드 추출
        const searchParams = new URLSearchParams(location.search);
        const code = searchParams.get('code');
        if (code) {
            handleOAuthKakao(code);
        } else {
            alert("인가 코드가 제공되지 않았습니다.");
            navigate("/login");
        }
    }, [location, navigate]);

    return <div>로그인 처리 중...</div>;
};

export default KakaoRedirect;
