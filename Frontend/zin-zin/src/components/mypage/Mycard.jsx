import React from 'react';
import styles from './Mypage.module.css';

const MyCard = () => {
  return (
    <div className={styles.cardContainer}>
      <div className={styles.cardHeader}>
        <h3>매칭 모드 <span className={styles.matchingMode}>ON</span></h3>
        <p>마지막 변경: 2024.07.02</p>
      </div>
      <div className={styles.cardContent}>
        <img src="representative-image-url" alt="대표 이미지" className={styles.representativeImage} />
        <div className={styles.introduction}>
          <p>저는 착한 사람입니다<br />그리고 맛집을 좋아해요</p>
        </div>
      </div>
    </div>
  );
}

export default MyCard;
