import React from "react";

const LoginView = () => {
  const REST_API_KEY = process.env.REACT_APP_KAKAO_REST_API_KEY;
  const REDIRECT_URI = process.env.REACT_APP_KAKAO_REDIRECT_URI;
  const link = `https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code&scope=profile_nickname,account_email,openid,friends,talk_message`;


  const handleKakaoLogin = () => {
      window.location.href = link 
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
    minWidth: '200px',
    height: 'auto',
  },
};

export default LoginView;
