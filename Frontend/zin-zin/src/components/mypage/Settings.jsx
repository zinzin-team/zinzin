import React, { useState } from 'react';
import styles from './Settings.module.css'; // CSS 파일을 import
import Modal from 'react-modal';
import axios from 'axios';

const Settings = () => {
    const [matchingMode, setMatchingMode] = useState(true);
    const [isNamePublic, setIsNamePublic] = useState(true);
    const [showModal, setShowModal] = useState(false); // 모달 상태 추가

    const handleToggleMatchingMode = () => {
        setShowModal(true); // 토글 클릭 시 모달 표시
    };

    const handleNameVisibilityChange = (event) => {
        setIsNamePublic(event.target.value === 'public');
        // 이름 공개 여부를 서버에 업데이트하는 로직을 추가할 수 있습니다.
    };

    const handleModalConfirm = async () => {
        try {
            const accessToken = sessionStorage.getItem('accessToken');
            await axios.post(
                '/api/member/matching-mode',
                {
                    matchingMode: !matchingMode,
                    matchingVisibility: isNamePublic ? "PUBLIC" : "PRIVATE",
                },
                {
                    headers: {
                        "Authorization": `Bearer ${accessToken}`,
                        "Content-Type": "application/json"
                    },
                    credentials: 'include',
                }
            );

            // 매칭 모드 상태 업데이트
            setMatchingMode(prevState => {
                const newState = !prevState;
                console.log('변경된 매칭 모드 상태:', newState);
                return newState;
            });

            setShowModal(false); // 모달 닫기
            alert('매칭 모드가 변경되었습니다.');
        } catch (error) {
            console.error('매칭 모드 변경 중 오류 발생:', error);
            alert('매칭 모드 변경 중 오류가 발생했습니다.');
        }
    };

    const handleModalCancel = () => {
        setShowModal(false); // 모달 숨기기
    };

    return (
        <div className={styles.settingsContainer}>
            <h2>설정</h2>
            
            <div className={styles.settingSection}>
                <h3>아이디 변경</h3>
                <input type="text" placeholder="아이디를 입력하세요" className={styles.inputField} />
                <button className={styles.changeIdButton}>아이디 변경하기</button>
            </div>

            <div className={styles.settingSection}>
                <h3>매칭 모드 변경</h3>
                <div className={styles.matchingModeContainer}>
                    <p>매칭 모드 {matchingMode ? "ON" : "OFF"}</p>
                    <p>마지막 변경: 2024.07.02</p>
                    <div className={styles.toggleSwitch}>
                        <label className={styles.switch}>
                            <input type="checkbox" checked={matchingMode} onChange={handleToggleMatchingMode} />
                            <span className={styles.slider}></span>
                        </label>
                    </div>
                </div>
                <p>매칭이 되면 지인에게 실명을 공개할까요?</p>
                <div className={styles.radioGroup}>
                    <label className={styles.radioLabel}>
                        <input 
                            type="radio" 
                            name="nameVisibility" 
                            value="public" 
                            checked={isNamePublic} 
                            onChange={handleNameVisibilityChange} 
                        />
                        공개
                    </label>
                    <label className={styles.radioLabel}>
                        <input 
                            type="radio" 
                            name="nameVisibility" 
                            value="private" 
                            checked={!isNamePublic} 
                            onChange={handleNameVisibilityChange} 
                        />
                        비공개
                    </label>
                </div>
            </div>

            <div className={styles.footer}>
                <button className={styles.footerButton}>회원 탈퇴</button>
            </div>

            <Modal
                isOpen={showModal}
                onRequestClose={handleModalCancel}
                shouldCloseOnOverlayClick={false}
                contentLabel="매칭 모드 변경"
                className={styles.modal}
                overlayClassName={styles.overlay}
            >
                <div>
                    <h2>매칭 모드를<br />변경할까요?</h2>
                    <button onClick={handleModalCancel}>취소</button>
                    <button onClick={handleModalConfirm}>확인</button>
                </div>
            </Modal>
        </div>
    );
};

export default Settings;
