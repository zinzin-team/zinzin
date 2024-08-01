import React from 'react';
import styles from './Friends.module.css';

const FriendtabNavbar = ({ activeTab, setActiveTab }) => {
  return (
    <div className={styles.tabNavigation}>
      <button
        className={`${styles.tabButton} ${activeTab === 'friends' ? styles.active : ''}`}
        onClick={() => setActiveTab('friends')}
      >
        카톡 친구 138
      </button>
      <button
        className={`${styles.tabButton} ${activeTab === 'acquaintances' ? styles.active : ''}`}
        onClick={() => setActiveTab('acquaintances')}
      >
        나의 지인 16
      </button>
    </div>
  );
};

export default FriendtabNavbar;
