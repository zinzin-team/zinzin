import React, { useState, useEffect } from 'react';
// import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import styles from './KakaoFriendsList.module.css';

const KakaoFriendsList = () => {
  // const navigate = useNavigate();

  const [friends, setFriends] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchFriends = async () => {
      const accessToken = sessionStorage.getItem('accessToken');
      if (!accessToken) {
        console.error('No token found in session storage');
        setLoading(false);
        // navigate("/logout");
        return;
      }

      try {
        const response = await axios.get(`${process.env.REACT_APP_API_URL}/api/mates/social-friends`, {
            headers: {
              "Authorization": `Bearer ${accessToken}`,
              "Content-Type": "application/json"
          },
          credentials: 'include',
        });
        console.log(response.data)
        setFriends(response.data);
        setLoading(false);
      } catch (error) {
        console.error('친구 목록을 가져오는 중 오류 발생:', error);
        setLoading(false);
      }
    };

    fetchFriends();
  }, []);

  if (loading) {
    return <div>로딩 중...</div>;
  }

  return (
    <div>
      <h2>카톡 친구</h2>
      <div className={styles.friendList}>
        {friends.map((friend, index) => (
          <div key={index} className={styles.friendItem}>
            <img
              src={friend.profileImage ? friend.profileImage : 'default-profile.png'}
              alt={`${friend.kakaoName} 프로필`}
              className={styles.profileImage}
            />
            <span className={styles.kakaoName}>{friend.kakaoName}</span>
            <button className={styles.actionButton}>
              {friend.relationship === null && '초대 보내기'}
              {friend.relationship === 'MEMBER' && '초대 보내기'}
              {friend.relationship === 'FOLLOW' && '나의 지인'}
              {friend.relationship === 'REQUEST_FOLLOW' && '지인 요청'}
              {friend.relationship === 'RECEIVE_REQUEST' && '요청 수락'}
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default KakaoFriendsList;
