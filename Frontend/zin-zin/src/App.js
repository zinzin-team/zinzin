import React from 'react';
import { useEffect } from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import HomeView from "./views/HomeView";
import ChatView from "./components/Chat";
import FriendsView from "./components/Friends";
import LeaveView from "./components/Leave";
import LikeView from "./components/Like";
import MatchingView from "./components/Matching";
import MypageView from "./components/Mypage";
import SignupView from "./components/Signup";
import Navbar from "./components/Navbar"; 
import './App.css';



function App() {
  return (
    <div className="App"> 
    <BrowserRouter>
    <Routes>
        <Route path="/" element={<HomeView />} />
        <Route path="/chat" element={<ChatView />} />
        <Route path="/friend" element={<FriendsView />} />
        <Route path="/leave" element={<LeaveView />} />
        <Route path="/like" element={<LikeView />} />
        <Route path="/match" element={<MatchingView />} />
        <Route path="/mypage" element={<MypageView />} />
        <Route path="/signup" element={<SignupView/>} />
    </Routes>
    <Navbar/>
  </BrowserRouter>
  </div>
  );
}

export default App;
