import React from 'react';
import styles from './Friends.module.css';

const Searchfriend = () => {
  return (
    <div className={styles.searchFriendContainer}>
      <p>아이디로 친구를 찾을 수 있어요!</p>
      <input type="text" placeholder="검색" className={styles.searchInput} />
    </div>
  );
};

export default Searchfriend;
