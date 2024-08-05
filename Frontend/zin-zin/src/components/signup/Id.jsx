import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './Signup.module.css';

const Id = () => {
  const [id, setId] = useState('');
  const navigate = useNavigate();

  const handleIdSubmit = () => {
    if (id.trim()) {
      // ID 제출 로직을 여기에 추가하세요
      navigate('/next/route');
    }
  };

  return (
    <div className={styles.container}>
      <div>
        <h2 className={styles.title}>김윤지 님, 환영해요!</h2>
        <p className={styles.description}>지인의 지인을 소개받고</p>
        <p className={styles.dot}>·</p>
        <p className={styles.description}>지인과 지인을 맺어줘요!</p>
      </div>
      <div className={styles.idContainer}>
        <div className={styles.inputGroup}>
          <label className={styles.inputTitle}>ID</label>
          <input
            type="text"
            placeholder="사용하실 아이디를 입력해주세요"
            value={id}
            onChange={(e) => setId(e.target.value)}
            className={styles.idInput}
          />
        </div>
        <button className={styles.submitButton} onClick={handleIdSubmit} disabled={!id.trim()}>
          중복 확인
        </button>
      </div>
    </div>
  );
};

export default Id;
