import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './Info.module.css';

const Info = ({ userData, setUserData }) => {
    const navigate = useNavigate();
    const [nameIsValid, setNameIsValid] = useState(true);

    useEffect(() => {
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
            nameIsValid &&
            userData.gender !== '' &&
            yearIsValid &&
            monthIsValid &&
            dayIsValid
        );
    };

    const handleNameChange = (e) => {
        const name = e.target.value;
        const namePattern = /^[가-힣]{1,10}$/;
        setNameIsValid(namePattern.test(name));
        setUserData({ ...userData, name: name });
    };

    const handleBirthChange = (e) => {
        const { name, value } = e.target;
        if (/^\d*$/.test(value)) { // 숫자만 허용
            setUserData({ ...userData, [name]: value });
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (isFormValid()) {
            const formattedBirth = `${userData.birthYear}-${userData.birthMonth}-${userData.birthDay}`;
            const updatedData = {
                ...userData,
                birth: formattedBirth,
            };
            setUserData(updatedData);
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
                        onChange={handleNameChange}
                        className={styles.nameInput}
                    />
                    {!nameIsValid && <p className={styles.error}>이름은 1글자 이상 10글자 이하의 한글로만 입력해주세요.</p>}
                </div>
                <div className={styles.inputGroup}>
                    <label className={styles.inputTitle}>성별</label>
                    <div className={styles.genderContainer}>
                        <div className={styles.radioContainer}>
                            <input
                                type="radio"
                                id="male"
                                name="gender"
                                value="MALE"
                                checked={userData.gender === 'MALE'}
                                onChange={(e) => setUserData({ ...userData, gender: 'MALE' })}
                                />
                            <label htmlFor="male" className={styles.radioLabel}>남</label>
                        </div>
                        <div className={styles.radioContainer}>
                            <input
                                type="radio"
                                id="female"
                                name="gender"
                                value="FEMALE"
                                checked={userData.gender === 'FEMALE'}
                                onChange={(e) => setUserData({ ...userData, gender: 'FEMALE' })}
                                />
                            <label htmlFor="female" className={styles.radioLabel}>여</label>
                        </div>
                    </div>
                </div>
                <div className={styles.inputGroup}>
                    <label className={styles.inputTitle}>생년월일</label>
                    <div className={styles.birthContainer}>
                        <input
                            type="text"
                            name="birthYear"
                            placeholder="YYYY"
                            value={userData.birthYear}
                            maxLength={4}
                            onChange={handleBirthChange}
                            className={styles.birthYear}
                        />
                        <input
                            type="text"
                            name="birthMonth"
                            placeholder="MM"
                            value={userData.birthMonth}
                            maxLength={2}
                            onChange={handleBirthChange}
                            className={styles.birthMonth}
                        />
                        <input
                            type="text"
                            name="birthDay"
                            placeholder="DD"
                            value={userData.birthDay}
                            maxLength={2}
                            onChange={handleBirthChange}
                            className={styles.birthDay}
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
