import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './NameReveal.module.css';
import apiClient from '../../api/apiClient';
import { useAuth } from '../../context/AuthContext';
import { jwtDecode } from 'jwt-decode';


const NameReveal = ({ userData, setUserData }) => {
    const navigate = useNavigate();
    const [matchingVisibility, setMatchingVisibility] = useState('PRIVATE');
    const { login } = useAuth();

    useEffect(() => {
        const storedData = JSON.parse(sessionStorage.getItem('userData'));
        if (storedData) {
            setUserData(storedData);
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
            
            const response = await apiClient.post('/api/member/register', requestData, {
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (response.status === 200) {
                // 회원가입 성공
                const tokens = {
                    accessToken: response.data.accessToken,
                };
                login(tokens);
                sessionStorage.setItem('accessToken', response.data.accessToken);

                try {
                    const decodedToken = jwtDecode(response.data.accessToken); // 디코딩된 토큰
                    const memberId = decodedToken.memberId; // 추출된 memberId
                    sessionStorage.setItem('memberId', memberId);
                } catch (decodeError) {
                    // 토큰 디코딩 실패
                    navigate("/login");
                    return; // 이후 코드 실행을 막기 위해 리턴
                }
        
                try {
                    const memberResponse = await apiClient.get('/api/member/me', {
                        headers: {
                            "Content-Type": "application/json"
                        },
                        credentials: 'include',
                    });
                    const memberData = memberResponse.data; // 회원 정보

                    sessionStorage.setItem('email', memberData.email);
                    sessionStorage.setItem('birth', memberData.birth);
                    sessionStorage.setItem('card', memberData.card);
                    sessionStorage.setItem('gender', memberData.gender);
                    sessionStorage.setItem('hasCard', memberData.hasCard);
                    sessionStorage.setItem('matchingMode', memberData.matchingMode);
                    sessionStorage.setItem('matchingModeLog', memberData.matchingModeLog);
                    sessionStorage.setItem('name', memberData.name);
                    sessionStorage.setItem('nickname', memberData.nickname);
                    sessionStorage.setItem('profileImage', memberData.profileImage);
                    sessionStorage.setItem('searchId', memberData.searchId);

                    navigate("/");
                } catch (memberError) {
                    // console.error('사용자 정보 가져오기 실패:', memberError.response ? memberError.response.data : memberError.message);
                    navigate("/login");
                }
            } else {
                // console.error('회원가입 실패');
                alert('회원가입에 실패했습니다.');
            }
        } catch (error) {
            // console.error('회원가입 중 오류 발생:', error);
            alert('회원가입 중 오류가 발생했습니다.');
        }
    };

    const handleBack = () => {
        navigate('/signup/matchingmode');
    };

    return (
        <div className={styles.container}>
            <div>
                <h2 className={styles.title}>{userData.name} 님, 환영해요!</h2>
                <p className={styles.description}>지인의 지인을 소개받고</p>
                <p className={styles.description}>∙</p>
                <p className={styles.description}>지인과 지인을 맺어줘요!</p>
            </div>
            <form className={styles.toggleContainer} onSubmit={handleSubmit}>
                <div className={styles.inputGroup}>
                    <label className={styles.inputTitle}>매칭 시 실명 공개</label>
                    <p className={styles.customParagraph}>매칭이 되면 지인에게 실명을 공개할까요?</p>
                    <div className={styles.nameRevealContainer}>
                        <div className={styles.radioContainer}>
                            <input 
                                type="radio"
                                id="public"
                                value="PUBLIC" 
                                checked={matchingVisibility === 'PUBLIC'} 
                                onChange={handleChange} 
                                className={styles.radioInput}
                            />
                            <label htmlFor="public" className={styles.radioLabel}>공개</label>
                        </div>
                        <div className={styles.radioContainer}>
                            <input 
                                type="radio" 
                                id="private"
                                value="PRIVATE" 
                                checked={matchingVisibility === 'PRIVATE'} 
                                onChange={handleChange} 
                                className={styles.radioInput}
                            />
                            <label htmlFor="private" className={styles.radioLabel}>비공개</label>
                        </div>
                    </div>
                </div>
                <button className={styles.backButton} type="button" onClick={handleBack}>
                    이전
                </button>
                <button className={styles.nextButton} type="submit">
                    완료
                </button>
            </form>
        </div>
    );
};

export default NameReveal;
