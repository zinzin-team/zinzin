import React, { useState, useEffect } from "react";
import { Routes, Route, Navigate, useLocation } from 'react-router-dom';
import Info from '../components/signup/Info';
import Id from '../components/signup/Id';
import MatchingMode from '../components/signup/MatchingMode';
import NameReveal from '../components/signup/NameReveal';

const SignupView = () => {
    const location = useLocation();
    const [userData, setUserData] = useState({
        email: '',
        name: '',
        sub: '',
        birth: '',
        gender: '',
        searchId: '',
        matchingMode: '',
        matchingVisibility: '',
    });

    useEffect(() => {
        if (location.state) {
            const { email, sub } = location.state;
            setUserData((prevData) => ({
                ...prevData,
                email,
                sub,
            }));
        }
    }, [location.state]);

    return (
      <Routes>
        <Route path="info" element={<Info userData={userData} setUserData={setUserData} />} />
        <Route path="id" element={<Id userData={userData} setUserData={setUserData} />} />
        <Route path="matchingmode" element={<MatchingMode userData={userData} setUserData={setUserData} />} />
        <Route path="namereveal" element={<NameReveal userData={userData} setUserData={setUserData} />} />
        <Route path="*" element={<Navigate to="info" />} />
      </Routes>
    );
};

export default SignupView;
