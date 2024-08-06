import React, { useState } from 'react';
import FriendtabNavbar from '../components/friends/FriendtabNavbar';
import KakaoFriendsList from './KakaoFriendsList';
import AcquaintancesList from './AcquaintancesList';
import Searchfriend from './Searchfriend';
import RequestBox from './RequestBox';
import styles from './Friends.module.css';

const FriendsView = () => {
  const [activeTab, setActiveTab] = useState('friends');

  const requests = [
    { id: 1, name: '홍길동', profilePicture: 'profile1.jpg', buttonLabel: '요청 수락 +' },
    { id: 2, name: '이순신', profilePicture: 'profile2.jpg', buttonLabel: '요청 수락 +' },
  ];

  const friends = [
    { id: 1, name: '김민지', profilePicture: 'profile3.jpg', buttonLabel: '초대 보내기' },
    { id: 2, name: '조현아', profilePicture: 'profile4.jpg', buttonLabel: '나의 지인' },
  ];

  const acquaintances = [
    { id: 1, name: '김민지', profilePicture: 'profile3.jpg', buttonLabel: '지인 해제' },
    { id: 2, name: '조현아', profilePicture: 'profile4.jpg', buttonLabel: '지인 해제' },
  ];

  return (
    <div className={styles.friendsContainer}>
      <FriendtabNavbar activeTab={activeTab} setActiveTab={setActiveTab} />
      {activeTab === 'friends' && (
        <>
          <div className={styles.requestSection}>
            <p className={styles.requestText}>지인 요청을 수락할까요?</p>
            <RequestBox requests={requests} />
          </div>
          <KakaoFriendsList friends={friends} />
        </>
      )}
      {activeTab === 'acquaintances' && <AcquaintancesList acquaintances={acquaintances} />}
      {activeTab === 'search' && <Searchfriend />}
    </div>
  );
};

export default FriendsView;
