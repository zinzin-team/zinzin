import React from 'react';
import { BrowserRouter, Route, Routes, useLocation } from "react-router-dom";
import LoginPage from "./components/signup/LoginPage";
// import CallbackPage from "./components/signup/CallbackPage";
import SignupView from "./views/SignupView";
import HomeView from "./views/HomeView";
import ChatView from "./components/chating/Chat";
import FriendsView from "./components/friends/Friends";
import LeaveView from "./components/signup/Leave";
import LikeView from "./components/list/Like";
import MatchingView from "./components/matching/Matching";
import MypageView from "./components/mypage/Mypage";
import Navbar from "./components/navbar/Navbar"; 
import CreatecardView from './components/matching/Createcard';
import UpdatecardView from './components/mypage/Updatecard';
import Header from './components/header/Header';
import './App.css';

const App = () => {
  return (
    <BrowserRouter>
      <AppContent />
    </BrowserRouter>
  );
}

const AppContent = () => {
  const location = useLocation();

  // 특정 경로에서 Header와 Navbar를 숨기기
  const hideHeaderPaths = ['/friend', '/login', '/signup', '/create-card','/update-card'];
  const hideNavbarPaths = ['/friend', '/login', '/signup'];

  const isHeaderHidden = hideHeaderPaths.some(path => location.pathname.startsWith(path));
  const isNavbarHidden = hideNavbarPaths.some(path => location.pathname.startsWith(path));

  return (
    <div className="App">
      {!isHeaderHidden && <Header />}
      <Routes>
        <Route path="/" element={<HomeView />} />
        <Route path="/login" element={<LoginPage />} />
        {/* <Route path="/api/oauth2/kakao/callback" element={<CallbackPage />} /> */}
        <Route path="/signup/*" element={<SignupView />} />
        <Route path="/chat" element={<ChatView />} />
        <Route path="/leave" element={<LeaveView />} />
        <Route path="/friend" element={<FriendsView />} />
        <Route path="/like" element={<LikeView />} />
        <Route path="/match" element={<MatchingView />} />
        <Route path="/mypage" element={<MypageView />} />
        <Route path="/create-card" element={<CreatecardView />} />
        <Route path="/update-card" element={<UpdatecardView />} />
      </Routes>
      {!isNavbarHidden && <Navbar />}
    </div>
  );
}

export default App;
