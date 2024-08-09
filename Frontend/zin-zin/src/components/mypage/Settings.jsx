import React, { useState } from 'react';
import styles from './Settings.module.css'; // CSS 파일을 import

const Settings = () => {
    const [matchingMode, setMatchingMode] = useState(true);
    const [isNamePublic, setIsNamePublic] = useState(true);

    const handleToggleMatchingMode = () => {
        setMatchingMode(!matchingMode);
        // 매칭 모드를 서버에 업데이트하는 로직을 추가할 수 있습니다.
    };

    const handleNameVisibilityChange = (event) => {
        setIsNamePublic(event.target.value === 'public');
        // 이름 공개 여부를 서버에 업데이트하는 로직을 추가할 수 있습니다.
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
                    <div className={styles.toggleSwitch} onClick={handleToggleMatchingMode}>
                        <div className={matchingMode ? styles.toggleOn : styles.toggleOff}>
                            {matchingMode ? "ON" : "OFF"}
                        </div>
                    </div>
                </div>
                <p>매칭이 되면 지인에게 실명을 공개할까요?</p>
                <div className={styles.radioGroup}>
                    <label>
                        <input 
                            type="radio" 
                            name="nameVisibility" 
                            value="public" 
                            checked={isNamePublic} 
                            onChange={handleNameVisibilityChange} 
                        />
                        공개
                    </label>
                    <label>
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
        </div>
    );
};

export default Settings;
