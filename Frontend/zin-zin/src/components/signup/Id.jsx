import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import styles from './Id.module.css';

const Id = ({ userData, setUserData }) => {
    const navigate = useNavigate();
    const [id, setId] = useState('');
    const [isValid, setIsValid] = useState(true);
    const [isDuplicate, setIsDuplicate] = useState(null);
    const [buttonText, setButtonText] = useState('중복 확인');
    const [isAvailable, setIsAvailable] = useState(false); // 추가된 상태: 사용 가능한 ID 여부

    useEffect(() => {
        const storedData = JSON.parse(sessionStorage.getItem('userData'));
        if (storedData) {
            setUserData(storedData);
        }
    }, [setUserData]);

    const handleIdChange = (e) => {
        const newId = e.target.value;
        if (newId.length <= 15) {
            setId(newId);
            validateId(newId);
            setIsDuplicate(null);
            setButtonText('중복 확인');
            setIsAvailable(false); // ID가 변경되면 사용 가능 여부 초기화
        }
    };

    const validateId = (id) => {
        const idPattern = /^[a-z0-9._-]{5,15}$/;
        setIsValid(idPattern.test(id));
    };

    const handleIdSubmit = async (e) => {
        e.preventDefault();
        if (id.trim() && isValid) {
            if (buttonText === '중복 확인') {
                try {
                    const response = await axios.get(`/api/member/register/search-id/${id}`);
        
                    if (response.data.duplicated) {
                        setIsDuplicate(true);
                        alert('이미 사용 중인 ID입니다.');
                    } else {
                        setIsDuplicate(false);
                        setIsAvailable(true); // 사용 가능한 ID 상태 업데이트
                        setButtonText('완료');
                    }
                } catch (error) {
                    console.error('ID 중복 확인 중 오류 발생:', error);
                    alert('ID 중복 확인 중 오류가 발생했습니다.');
                }
            } else if (buttonText === '완료') {
                const updatedData = { ...userData, searchId: id };
                setUserData(updatedData);
                sessionStorage.setItem('userData', JSON.stringify(updatedData));
                navigate('/signup/matchingmode');
            }
        }
    };

    return (
        <div className={styles.container}>
            <div>
                <h2 className={styles.title}>{userData.name} 님, 환영해요!</h2>
                <p className={styles.description}>지인의 지인을 소개받고</p>
                <p className={styles.description}>∙</p>
                <p className={styles.description}>지인과 지인을 맺어줘요!</p>
            </div>
            <form className={styles.idContainer} onSubmit={handleIdSubmit}>
                <div className={styles.inputGroup}>
                    <label className={styles.inputTitle}>ID</label>
                    <input
                        type="text"
                        placeholder="사용하실 아이디를 입력해주세요"
                        value={id}
                        onChange={handleIdChange}
                        className={styles.idInput}
                        maxLength={15}
                    />
                    {!isValid && <p className={styles.error}>ID는 5~15자의 영소문자, 숫자, _, -, .만 사용 가능합니다.</p>}
                    {isDuplicate && <p className={styles.error}>이미 사용 중인 ID입니다.</p>}
                    {isAvailable && <p className={styles.success}>사용 가능한 ID 입니다.</p>} {/* 추가된 문구 */}
                </div>
                <button className={styles.submitButton} type="submit" disabled={!id.trim() || !isValid}>
                    {buttonText}
                </button>
            </form>
        </div>
    );
};

export default Id;
