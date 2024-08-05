import React from "react";
import { Routes, Route, Navigate } from 'react-router-dom';
import Info from '../components/signup/Info';
import Id from '../components/signup/Id';
// import MatchingMode from '../components/signup/MatchingMode';
// import NameReveal from '../components/signup/NameReveal';

const SignupView = () => {
    return (
        <div>
          <Routes>
            <Route path="info" element={<Info />} />
            <Route path="id" element={<Id />} />
            {/* <Route path="matchingmode" element={<MatchingMode />} /> */}
            {/* <Route path="namereveal" element={<NameReveal />} /> */}
            <Route path="*" element={<Navigate to="info" />} />
          </Routes>
        </div>
    );
};

export default SignupView;
