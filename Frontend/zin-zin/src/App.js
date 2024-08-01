import React from 'react';
import { BrowserRouter, Route, Routes, useLocation } from "react-router-dom";
import HomeView from "./views/HomeView";
import ChatView from "./components/Chat";
import FriendsView from "./components/friends/Friends";
import LeaveView from "./components/Leave";
import LikeView from "./components/Like";
import MatchingView from "./components/Matching";
import MypageView from "./components/Mypage";
import SignupView from "./components/signup/Signup";
import Navbar from "./components/Navbar"; 
import CreatecardView from './components/Createcard';
import UpdatecardView from './components/Updatecard';
import Header from './components/Header';
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

  // 특정 경로에서 Navbar를 숨기기
  const hideHeaderPaths = ['/friend'];
  const hideNavbarPaths = ['/friend'];

  return (
    <div className="App"> 
      {!hideHeaderPaths.includes(location.pathname) && <Header />}
      <Routes>
        <Route path="/" element={<HomeView />} />
        <Route path="/chat" element={<ChatView />} />
        <Route path="/leave" element={<LeaveView />} />
        <Route path="/friend" element={<FriendsView />} />
        <Route path="/like" element={<LikeView />} />
        <Route path="/match" element={<MatchingView />} />
        <Route path="/mypage" element={<MypageView />} />
        <Route path="/signup" element={<SignupView />} />
        <Route path="/create-card" element={<CreatecardView />} />
        <Route path="/update-card" element={<UpdatecardView />} />
      </Routes>
      {!hideNavbarPaths.includes(location.pathname) && <Navbar />}
    </div>
  );
}

export default App;
