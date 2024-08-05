import React from 'react';
import styles from './Mypage.module.css';

const MypageList = () => {
  return (
    <div className={styles.listContainer}>
      <div className={styles.listItem}>
        <span>설정</span>
        <span className={styles.arrow}>&gt;</span>
      </div>
      <div className={styles.listItem}>
        <span>FAQ</span>
        <span className={styles.arrow}>&gt;</span>
      </div>
    </div>
  );
}

export default MypageList;
