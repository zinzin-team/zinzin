import React from "react";
import styles from './Signup.module.css';

const CardContainer = () => {
  return (
    <div className={styles.cardContainer}>
      <div className={styles.fieldContainer}>
        <label className={styles.label}>이름</label>
        <p className={styles.description}>지인들이 알아볼 수 있도록 정확한 실명을 입력해주세요.</p>
        
      </div>
    </div>
  )
}

export default CardContainer