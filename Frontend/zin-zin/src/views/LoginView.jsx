import React from "react";

const LoginView = () => {
  const REST_API_KEY = '605b8f5958cd7c03b1f456fda77ab473';
  const REDIRECT_URI = 'http://localhost:3000/callback';
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
