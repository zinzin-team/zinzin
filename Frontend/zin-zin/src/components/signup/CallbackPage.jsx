import React, { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';

const KakaoCallback = () => {
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      const queryParams = new URLSearchParams(location.search);
      const code = queryParams.get('code');
      console.log("12312312312");
      
      if (code) {
        try {
          const response = await axios.get(`/oauth2/kakao/callback?code=${code}`);
          const memberAuthData = response.data;

          // 로그인 성공 후 필요한 작업 수행
          // 예: 사용자 상태 업데이트, 토큰 저장, 홈 페이지로 리다이렉트 등
          console.log(memberAuthData);
          // 예시: 홈 페이지로 리다이렉트
          navigate.push('/home');
        } catch (error) {
          console.error('로그인 실패', error);
          // 로그인 실패 시 필요한 작업 수행
        }
      }
    };

    fetchData();
  }, [location, navigate]);

  return <div>로그인 중...</div>;
};

export default KakaoCallback;
