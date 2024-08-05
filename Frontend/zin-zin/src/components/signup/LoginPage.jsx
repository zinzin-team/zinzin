import React from "react";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';


const LoginPage = () => {
  const navigate = useNavigate();

  const handleKakaoLogin = async () => {
    try {
      const response = await axios.get('/api/oauth2/authorize');
      const { accessToken, refreshToken, isUser, sub, role } = response.data;
  
      if (isUser) {
        console.log('로그인 성공');
        navigate('/match')
        console.log('AccessToken:', accessToken);
        console.log('RefreshToken:', refreshToken);
      } else {
        console.log('회원 가입 필요');
        navigate('/signup')
        console.log('Sub:', sub);
        console.log('Role:', role);
      }
    } catch (error) {
      console.error('로그인 실패:', error);
    }
  };

  return (
    <div style={styles.container}>
      <img
        src='assets/kakao_login_large_wide.png'
        alt='카카오 로그인'
        style={styles.loginButton}
        onClick={handleKakaoLogin}
      />
    </div>
  );
};

const styles = {
  container: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    height: '100vh',
    width: '100%',
    position: 'relative',
  },
  loginButton: {
    cursor: 'pointer',
    width: '80%',
    maxWidth: '300px',
    minWidth: '200px', // 최소 너비 설정
    height: 'auto',
  },
};

export default LoginPage;
