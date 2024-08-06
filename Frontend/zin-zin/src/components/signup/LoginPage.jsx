import React from "react";
import { useNavigate } from 'react-router-dom';

const CLIENT_ID = process.env.REACT_APP_KAKAO_REST_API_KEY;
const REDIRECT_URI = process.env.REACT_APP_KAKAO_REDIRECT_URI;
const KakaoURL = `https://kauth.kakao.com/oauth/authorize?client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=code`;

const LoginPage = () => {
  const navigate = useNavigate();

  const handleKakaoLogin = () => {
    window.location.href = KakaoURL;
    navigate('/callback');
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
