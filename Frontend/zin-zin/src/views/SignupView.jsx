import React, { useState, useEffect } from "react";
import { Routes, Route, Navigate } from 'react-router-dom';
import Info from '../components/signup/Info';
import Id from '../components/signup/Id';
import MatchingMode from '../components/signup/MatchingMode';
import NameReveal from '../components/signup/NameReveal';

const SignupView = () => {
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
        const storedEmail = sessionStorage.getItem('email');
        const storedSub = sessionStorage.getItem('sub');
        if (storedEmail && storedSub) {
            setUserData((prevData) => ({
                ...prevData,
                email: storedEmail,
                sub: storedSub,
            }));
        }
    }, []);

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
