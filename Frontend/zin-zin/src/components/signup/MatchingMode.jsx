import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './Signup.module.css';

const MatchingMode = () => {
  const [name, setName] = useState('');
  const [gender, setGender] = useState('');
  const [birthDate, setBirthDate] = useState({ year: '', month: '', day: '' });
  const navigate = useNavigate();

  const isFormValid = () => {
    return (
      name.trim() !== '' &&
      gender !== '' &&
      birthDate.year.length === 4 &&
      birthDate.month.length === 2 &&
      birthDate.day.length === 2
    );
  };

  const handleSubmit = () => {
    if (isFormValid()) {
      navigate('/signup/id');
    }
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.title}>내 정보</h2>
      <p className={styles.description}>내 정보는 이후 수정이 불가합니다.</p>
      <div className={styles.infoContainer}>
        <div className={styles.inputGroup}>
          <label className={styles.inputTitle}>이름</label>
          <p className={styles.nameDescription}>지인들이 알아볼 수 있도록 정확한 실명을 입력해주세요!</p>
          <input
            type="text"
            placeholder="실명을 입력해주세요"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </div>
        <div className={styles.inputGroup}>
          <label className={styles.inputTitle}>성별</label>
          <label className={styles.radioLabel}>
            <input
              type="radio"
              value="남"
              checked={gender === '남'}
              onChange={(e) => setGender(e.target.value)}
              />
            남
          </label>
          <label className={styles.radioLabel}>
            <input
              type="radio"
              value="여"
              checked={gender === '여'}
              onChange={(e) => setGender(e.target.value)}
              />
            여
          </label>
        </div>
        <div className={styles.inputGroup}>
          <label className={styles.inputTitle}>생년월일</label>
          <input
            type="text"
            placeholder="YYYY"
            value={birthDate.year}
            maxLength={4}
            onChange={(e) => setBirthDate({ ...birthDate, year: e.target.value })}
          />
          <input
            type="text"
            placeholder="MM"
            value={birthDate.month}
            maxLength={2}
            onChange={(e) => setBirthDate({ ...birthDate, month: e.target.value })}
          />
          <input
            type="text"
            placeholder="DD"
            value={birthDate.day}
            maxLength={2}
            onChange={(e) => setBirthDate({ ...birthDate, day: e.target.value })}
          />
        </div>
        <button className={styles.submitButton} onClick={handleSubmit} disabled={!isFormValid()}>
          다음
        </button>
      </div>
    </div>
  );
};

export default MatchingMode;
