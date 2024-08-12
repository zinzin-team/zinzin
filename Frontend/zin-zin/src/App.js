import React from 'react';
import { BrowserRouter, Route, Routes, useLocation } from "react-router-dom";
import LoginView from "./views/LoginView";
import SignupView from "./views/SignupView";
import HomeView from "./views/HomeView";
import ChatView from "./components/chating/Chat";
import KakaoCallback from './components/signup/KakaoRedirect';
import FriendsView from "./views/FriendsView";
import LeaveView from "./views/LeaveView";
import LikeView from "./components/list/Like";
import MatchingView from "./components/matching/Matching";
import MypageView from "./components/mypage/MypageView";
import Settings from './components/mypage/Settings'
import UserGuide from './components/mypage/UserGuide'
import Navbar from "./components/navbar/Navbar"; 
import CreatecardView from './components/matching/Createcard';
import UpdateCard from './components/mypage/Updatecard';
import Header from './components/header/Header';
import './App.css';
import { AuthProvider } from './context/AuthContext';
import ChatroomView from './components/chating/Chattingroom'

const App = () => {
  return (
    <AuthProvider>
      <BrowserRouter>
        <AppContent />
      </BrowserRouter>
    </AuthProvider>
  );
};

const AppContent = () => {
  const location = useLocation();

  // 특정 경로에서 Header와 Navbar를 숨기기
  const hideHeaderPaths = ['/friend', '/login', '/signup', '/create-card','/update-card', '/callback', '/leave', '/mypage', '/settings', '/userguide'];
  const hideNavbarPaths = ['/friend', '/login', '/signup', '/callback',  '/leave'];
  const isChatRoom = location.pathname.startsWith('/chat/') && location.pathname.split('/').length === 3;

  const isHeaderHidden = hideHeaderPaths.some(path => location.pathname.startsWith(path)) || isChatRoom;
  const isNavbarHidden = hideNavbarPaths.some(path => location.pathname.startsWith(path)) || isChatRoom;

  return (
    <div className="App">
      {!isHeaderHidden && <Header />}
      <Routes>
        <Route path="/" element={<HomeView />} />
        <Route path="/login" element={<LoginView />} />
        <Route path="/callback" element={<KakaoCallback />} />
        <Route path="/signup/*" element={<SignupView />} />
        <Route path="/chat" element={<ChatView />} />
        <Route path="/chat/:roomId" element={<ChatroomView />} />
        <Route path="/friends/*" element={<FriendsView />} />
        <Route path="/like" element={<LikeView />} />
        <Route path="/match" element={<MatchingView />} />
        <Route path="/mypage" element={<MypageView />} />
        <Route path="/settings" element={<Settings />} />
        <Route path="/userguide" element={<UserGuide />} />
        <Route path="/create-card" element={<CreatecardView />} />
        <Route path="/update-card/:cardId" element={<UpdateCard />} />
        <Route path="/leave" element={<LeaveView />} />
      </Routes>
      {!isNavbarHidden && <Navbar />}
    </div>
  );
}

export default App;
