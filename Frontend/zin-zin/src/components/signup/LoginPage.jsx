import React from "react";

const CLIENT_ID = process.env.REACT_APP_KAKAO_REST_API_KEY;
const REDIRECT_URI = process.env.REACT_APP_KAKAO_REDIRECT_URI;
const KakaoURL = `https://kauth.kakao.com/oauth/authorize?client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=code`;

const LoginPage = () => {  

  const handleKakaoLogin = () => {
    window.location.href = KakaoURL;
  };

  return (
    <div>
      <img
        src='assets/kakao_login_large_wide.png'
        alt='카카오 로그인'
        style={{
          cursor: 'pointer',
          width: '300px',
          height: 'auto',
          position: 'absolute',
          top: '70%',
          left: '50%',
          transform: 'translate(-50%, -50%)',
        }}
        onClick={handleKakaoLogin}
      />
    </div>
  );
};

export default LoginPage;
