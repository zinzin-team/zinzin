import React from 'react';
import styles from './Friends.module.css';

const AcquaintancesList = ({ acquaintances }) => {
  return (
    <div className={styles.friendList}>
      {acquaintances.map(acquaintance => (
        <div key={acquaintance.id} className={styles.friendItem}>
          <img src={acquaintance.profilePicture} alt={`${acquaintance.name} 프로필`} className={styles.profilePicture} />
          <div className={styles.friendInfo}>
            <span>{acquaintance.name}</span>
            <button className={styles.acquaintanceButton}>{acquaintance.buttonLabel}</button>
          </div>
        </div>
      ))}
    </div>
  );
};

export default AcquaintancesList;
