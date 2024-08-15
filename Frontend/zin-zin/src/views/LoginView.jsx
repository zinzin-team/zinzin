import React from "react";
import styles from "./LoginView.module.css";

const LoginView = () => {
  // 6개의 카드에 대한 앞면 이미지 경로를 배열로 정의
  const frontImages = [
    '/assets/z.png',
    '/assets/in.png',
    '/assets/s.png',
    '/assets/z2.png',
    '/assets/in2.png',
    '/assets/logo.png',
  ];

  // 6개의 카드에 대한 뒷면 이미지 경로를 배열로 정의
  const backImages = [
    '/assets/in.png',
    '/assets/z.png',
    '/assets/z2.png',
    '/assets/in2.png',
    '/assets/s.png',
    '/assets/logo.png',
  ];

  const handleKakaoLogin = () => {
    const REST_API_KEY = process.env.REACT_APP_KAKAO_REST_API_KEY;
    const REDIRECT_URI = process.env.REACT_APP_KAKAO_REDIRECT_URI;
    const link = `https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code&scope=profile_nickname,account_email,openid,friends,talk_message`;

    window.location.href = link;
  };

  return (
    <div className={styles.container}>
      <div className={styles.flipContainer}>
        <div className={styles.gridContainer}>
          {Array.from({ length: 6 }).map((_, index) => (
            <div className={styles.flip} key={index}>
              <div className={styles.card}>
                <div className={styles.front}>
                  <img src={frontImages[index]} alt="앞면" className={styles.cardImage} />
                </div>
                <div className={styles.back}>
                  <img src={backImages[index]} alt="뒷면" className={styles.cardImage} />
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
      <img
        src="/assets/kakao_login_large_wide.png"
        alt="카카오 로그인"
        className={styles.loginButton}
        onClick={handleKakaoLogin}
      />
    </div>
  );
};

export default LoginView;
