// AuthContext.js
import React, { createContext, useState, useContext } from 'react';

// Context 생성
const AuthContext = createContext();

// Context Provider 정의
export const AuthProvider = ({ children }) => {
  const [authState, setAuthState] = useState({
    isAuthenticated: false,
    accessToken: null,
    // refreshTosken: null,
  });

  const login = (tokens) => {
    sessionStorage.setItem('accessToken', tokens.accessToken);
    // sessionStorage.setItem('refreshToken', tokens.refreshToken);
    setAuthState({
      isAuthenticated: true,
      accessToken: tokens.accessToken,
      // refreshToken: tokens.refreshToken,
    });
  };

  const logout = () => {
    sessionStorage.removeItem('accessToken');
    // sessionStorage.removeItem('refreshToken');
    setAuthState({
      isAuthenticated: false,
      accessToken: null,
      // refreshToken: null,
    });
  };

  return (
    <AuthContext.Provider value={{ authState, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

// Custom hook for using the auth context
export const useAuth = () => {
  return useContext(AuthContext);
};
