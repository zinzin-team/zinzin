import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './MatchingMode.module.css';

const MatchingMode = ({ userData, setUserData }) => {
    const navigate = useNavigate();
    const [matchingMode, setMatchingMode] = useState(false);

    useEffect(() => {
        const storedData = JSON.parse(sessionStorage.getItem('userData'));
        if (storedData) {
            setUserData(storedData);
        }
    }, [setUserData]);

    const handleToggle = () => {
        setMatchingMode(!matchingMode);
    };

    const handleSubmit = (e) => {
        e.preventDefault(); // form의 기본 제출 동작을 막기 위해 사용
        try {
            const updatedData = { ...userData, matchingMode };
            setUserData(updatedData);
            sessionStorage.setItem('userData', JSON.stringify(updatedData));
            navigate('/signup/namereveal');
        } catch (error) {
            console.error('매칭 모드 설정 중 오류 발생:', error);
            alert('매칭 모드 설정 중 오류가 발생했습니다.');
        }
    };

    return (
        <div className={styles.container}>
            <div>
                <h2 className={styles.title}>{userData.name} 님, 환영해요!</h2>
                <p className={styles.description}>지인의 지인을 소개받고</p>
                <p className={styles.description}>·</p>
                <p className={styles.description}>지인과 지인을 맺어줘요!</p>
            </div>
            <form className={styles.toggleContainer} onSubmit={handleSubmit}>
                <p className={styles.toggleLabel}>매칭 모드를 켤까요?</p>
                <div className={styles.toggle}>
                    <span>OFF</span>
                    <label className={styles.switch}>
                        <input type="checkbox" checked={matchingMode} onChange={handleToggle} />
                        <span className={styles.slider}></span>
                    </label>
                    <span>ON</span>
                </div>
                <button className={styles.nextButton} type="submit">다음</button>
            </form>
        </div>
    );
};

export default MatchingMode;
