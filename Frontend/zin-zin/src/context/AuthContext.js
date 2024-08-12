// AuthContext.js
import React, { createContext, useState, useContext, useEffect } from 'react';

// Context 생성
const AuthContext = createContext();

// Context Provider 정의
export const AuthProvider = ({ children }) => {
  const [authState, setAuthState] = useState({
    isAuthenticated: false,
    accessToken: null,
    refreshToken: null,
  });

  useEffect(() => {
    const accessToken = sessionStorage.getItem('accessToken');
    
    if (accessToken) {
      setAuthState({
        isAuthenticated: true,
        accessToken
      });
    }
  }, []);

  const login = (tokens) => {
    setAuthState({
      isAuthenticated: true,
      accessToken: tokens.accessToken,
      refreshToken: tokens.refreshToken,
    });
  };

  const logout = () => {
    setAuthState({
      isAuthenticated: false,
      accessToken: null,
      refreshToken: null,
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
  console.log(AuthContext)
  return useContext(AuthContext);
};
