import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './NameReveal.module.css';
import axios from 'axios';

const NameReveal = ({ userData, setUserData }) => {
    const navigate = useNavigate();
    const [matchingVisibility, setMatchingVisibility] = useState('PRIVATE');

    useEffect(() => {
        const storedData = JSON.parse(sessionStorage.getItem('userData'));
        if (storedData) {
            setUserData(storedData);
            // setMatchingVisibility(storedData.matchingVisibility || 'PRIVATE');
        }
    }, [setUserData]);

    const handleChange = (e) => {
        setMatchingVisibility(e.target.value);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const updatedData = { ...userData, matchingVisibility };
            setUserData(updatedData);
            sessionStorage.setItem('userData', JSON.stringify(updatedData));

            // API 요청에 사용할 데이터 준비
            const requestData = {
                email: userData.email,
                name: userData.name,
                sub: userData.sub,
                birth: userData.birth,
                gender: userData.gender,
                searchId: userData.searchId,
                matchingVisibility,
                matchingMode: userData.matchingMode,
                nickname: null
            };
            
            // const response = await axios.post(`${process.env.REACT_APP_API_URL}/api/member/register`, requestData, {
            const response = await axios.post('/api/member/register', requestData, {
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (response.ok) {
                console.log('회원가입 성공');
                navigate('/');
            } else {
                console.error('회원가입 실패');
                alert('회원가입에 실패했습니다.');
            }
        } catch (error) {
            console.error('회원가입 중 오류 발생:', error);
            alert('회원가입 중 오류가 발생했습니다.');
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
                <p className={styles.toggleLabel}>매칭 시 실명 공개</p>
                <div>
                    <label className={styles.radioLabel}>
                        <input 
                            type="radio" 
                            value="PUBLIC" 
                            checked={matchingVisibility === 'PUBLIC'} 
                            onChange={handleChange} 
                            className={styles.radioInput}
                        />
                        공개
                    </label>
                    <label className={styles.radioLabel}>
                        <input 
                            type="radio" 
                            value="PRIVATE" 
                            checked={matchingVisibility === 'PRIVATE'} 
                            onChange={handleChange} 
                            className={styles.radioInput}
                        />
                        비공개
                    </label>
                </div>
                <button className={styles.nextButton} type="submit">완료</button>
            </form>
        </div>
    );
};

export default NameReveal;
