// CallbackPage.jsx
import React, { useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const CallbackPage = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        // 예시: 백엔드에서 사용자 정보를 가져옴
        const response = await axios.get('/api/oauth2/kakao/callback');
        if (response.data) {
          const { accessToken, refreshToken, isUser, sub, role } = response.data;

          if (isUser) {
            // 이미 회원인 경우
            sessionStorage.setItem('userData', JSON.stringify({ accessToken, refreshToken, role }));
            navigate('/match');
          } else {
            // 회원이 아닌 경우
            sessionStorage.setItem('signupData', JSON.stringify({ sub, role }));
            navigate('/signup');
          }
        } else {
          console.error('Failed to fetch login data');
        }
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, [navigate]);

  return <div>Loading...</div>;
};

export default CallbackPage;
