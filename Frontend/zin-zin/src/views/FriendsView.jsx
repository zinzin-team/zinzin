import React, { useState } from 'react';
import FriendtabNavbar from '../components/friends/FriendtabNavbar';
import { Routes, Route, Navigate } from 'react-router-dom';
import KakaoFriendsList from './KakaoFriendsList';
import AcquaintancesList from './AcquaintancesList';
import Searchfriend from './Searchfriend';

const FriendsView = () => {
  const [activeTab, setActiveTab] = useState('friends');

  // return (
    // <Routes>
      // <Route path=""
    // </Routes>
    // <div className={styles.friendsContainer}>
    //   <FriendtabNavbar activeTab={activeTab} setActiveTab={setActiveTab} />
    //   {/* {activeTab === 'friends' && (
    //     <>
    //       <div className={styles.requestSection}>
    //         <p className={styles.requestText}>지인 요청을 수락할까요?</p>
    //         <RequestBox requests={requests} />
    //       </div>
    //       <KakaoFriendsList friends={friends} />
    //     </>
    //   )}
    //   {activeTab === 'acquaintances' && <AcquaintancesList acquaintances={acquaintances} />}
    //   {activeTab === 'search' && <Searchfriend />} */}
    // </div>
  // );
};

export default FriendsView;
