import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ element }) => {
  return (sessionStorage.getItem("accessToken")) ? element : <Navigate to="/login" />;
};

export default ProtectedRoute;
