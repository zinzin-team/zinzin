import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const KakaoCallback = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await axios.get('/api/oauth2/kakao/callback');
        const userInfo = response.data;

        if (userInfo.isMember) {
          navigate('/match');
        } else {
          navigate('/register');
        }
      } catch (error) {
        console.error('Error fetching user info', error);
      }
    };

    fetchUserInfo();
  }, [navigate]);

  return <div>카카오 로그인 중...</div>;
};

export default KakaoCallback;
