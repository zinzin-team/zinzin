import React from 'react';
import styles from './Friends.module.css';

const KakaoFriendsList = ({ friends }) => {
  return (
    <div className={styles.friendList}>
      {friends.map(friend => (
        <div key={friend.id} className={styles.friendItem}>
          <img src={friend.profilePicture} alt={`${friend.name} 프로필`} className={styles.profilePicture} />
          <div className={styles.friendInfo}>
            <span>{friend.name}</span>
            <button className={styles.friendButton}>{friend.buttonLabel}</button>
          </div>
        </div>
      ))}
    </div>
  );
};

export default KakaoFriendsList;
