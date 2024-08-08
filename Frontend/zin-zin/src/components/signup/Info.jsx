import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './Info.module.css';

const Info = ({ userData, setUserData }) => {
    const navigate = useNavigate();

    useEffect(() => {
        // 컴포넌트가 마운트될 때 로컬 스토리지에서 데이터 불러오기
        const storedData = JSON.parse(sessionStorage.getItem('userData'));
        if (storedData) {
            setUserData(storedData);
        }
    }, [setUserData]);

    const isFormValid = () => {
        const yearIsValid = /^\d{4}$/.test(userData.birthYear);
        const monthIsValid = /^\d{2}$/.test(userData.birthMonth) && Number(userData.birthMonth) >= 1 && Number(userData.birthMonth) <= 12;
        const dayIsValid = /^\d{2}$/.test(userData.birthDay) && Number(userData.birthDay) >= 1 && Number(userData.birthDay) <= 31;
        return (
            userData.name.trim() !== '' &&
            userData.gender !== '' &&
            yearIsValid &&
            monthIsValid &&
            dayIsValid
        );
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (isFormValid()) {
            // "YYYY-MM-DD" 형식으로 birth 조합
            const formattedBirth = `${userData.birthYear}-${userData.birthMonth}-${userData.birthDay}`;
            const updatedData = {
                ...userData,
                birth: formattedBirth,
            };
            setUserData(updatedData);
            // 로컬 스토리지에 데이터 저장
            sessionStorage.setItem('userData', JSON.stringify(updatedData));
            navigate('/signup/id');
        }
    };

    return (
        <div className={styles.container}>
            <div>
                <h2 className={styles.title}>내 정보</h2>
                <p className={styles.description}>내 정보는 이후 수정이 불가합니다.</p>
            </div>
            <form className={styles.infoContainer} onSubmit={handleSubmit}>
                <div className={styles.inputGroup}>
                    <label className={styles.inputTitle}>이름</label>
                    <p className={styles.nameDescription}>지인들이 알아볼 수 있도록 정확한 실명을 입력해주세요!</p>
                    <input
                        type="text"
                        placeholder="실명을 입력해주세요"
                        value={userData.name}
                        onChange={(e) => setUserData({ ...userData, name: e.target.value })}
                    />
                </div>
                <div className={styles.inputGroup}>
                    <label className={styles.inputTitle}>성별</label>
                    <label className={styles.radioLabel}>
                        <input
                            type="radio"
                            value="MALE"
                            checked={userData.gender === 'MALE'}
                            onChange={(e) => setUserData({ ...userData, gender: 'MALE' })}
                        />
                        남
                    </label>
                    <label className={styles.radioLabel}>
                        <input
                            type="radio"
                            value="FEMALE"
                            checked={userData.gender === 'FEMALE'}
                            onChange={(e) => setUserData({ ...userData, gender: 'FEMALE' })}
                        />
                        여
                    </label>
                </div>
                <div className={styles.inputGroup}>
                    <label className={styles.inputTitle}>생년월일</label>
                    <div className={styles.birthContainer}>
                        <input
                            type="text"
                            placeholder="YYYY"
                            value={userData.birthYear}
                            maxLength={4}
                            onChange={(e) => setUserData({ ...userData, birthYear: e.target.value })}
                        />
                        <input
                            type="text"
                            placeholder="MM"
                            value={userData.birthMonth}
                            maxLength={2}
                            onChange={(e) => setUserData({ ...userData, birthMonth: e.target.value })}
                        />
                        <input
                            type="text"
                            placeholder="DD"
                            value={userData.birthDay}
                            maxLength={2}
                            onChange={(e) => setUserData({ ...userData, birthDay: e.target.value })}
                        />
                    </div>
                </div>
                <button className={styles.submitButton} type="submit" disabled={!isFormValid()}>
                    다음
                </button>
            </form>
        </div>
    );
};

export default Info;
