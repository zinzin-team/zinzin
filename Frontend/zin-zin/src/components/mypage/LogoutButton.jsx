import React from 'react';
import { useAuth } from '../../context/AuthContext';

const LogoutButton = () => {
  const { logout } = useAuth();

  const handleLogout = () => {
    // 세션 스토리지 비우기
    sessionStorage.clear();

    // Context API를 사용하여 로그아웃 상태 업데이트
    logout();

    // 로그인 페이지로 리디렉션
    window.location.href = '/login';
  };

  return (
    <button onClick={handleLogout}>
      로그아웃
    </button>
  );
};

export default LogoutButton;
