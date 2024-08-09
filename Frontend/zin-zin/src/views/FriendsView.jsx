import React from 'react';
import { Route, Routes, Link } from 'react-router-dom';
import FriendsNavbar from '../components/friends/FriendsNavbar';
import KakaoFriendsList from '../components/friends/KakaoFriendsList';
import AcquaintancesList from '../components/friends/AcquaintancesList';
import Searchfriend from '../components/friends/Searchfriend';
import styles from './FriendsView.module.css';

const FriendsView = () => {
  return (
    <div className={styles.friendsContainer}>
      <Link to="/" className={styles.link}><i className="bi bi-chevron-left"/></Link>
      <FriendsNavbar />
      <div className={styles.content}>
        <Routes>
          <Route path="/" element={<KakaoFriendsList />} />
          <Route path="/z-in" element={<AcquaintancesList />} />
          <Route path="/search" element={<Searchfriend />} />
        </Routes>
      </div>
    </div>
  );
};

export default FriendsView;
