import React, { useEffect, useState } from 'react';
import { useNavigate, Link } from "react-router-dom"; 
import styles from './Settings.module.css'; 
import Modal from 'react-modal';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import apiClient from '../../api/apiClient';

const Settings = () => {
  const [matchingMode, setMatchingMode] = useState(true);
  const [isNamePublic, setIsNamePublic] = useState(true);
  const [userData, setUserData] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [showVisibilityModal, setShowVisibilityModal]  = useState(false);
  const [lastModified, setLastModified] = useState(null);
  const [profileImage, setProfileImage] = useState(null);
  const [isLoading, setIsLoading] = useState(true); // 로딩 상태 추가
  
  const [isEditingId, setIsEditingId] = useState(false); // 아이디 수정 모드 여부
  const [newSearchId, setNewSearchId] = useState(""); // 새로운 아이디 입력값
  const [isIdValid, setIsIdValid] = useState(false); // 아이디 유효성 검사 결과
  const [isIdDuplicate, setIsIdDuplicate] = useState(false); // 아이디 중복 여부
  const [idStatusMessage, setIdStatusMessage] = useState(""); // 아이디 상태 메시지
  const [buttonLabel, setButtonLabel] = useState("아이디 변경하기"); // 버튼 라벨

  const navigate = useNavigate();

  const fetchUserData = async () => {
    const accessToken = sessionStorage.getItem('accessToken');
    if (!accessToken) {
      console.error('No token found in session storage');
      navigate("/logout"); 
      return;
    }

    try {
      const response = await apiClient.get("/api/member/me", {
        headers: {
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "application/json"
        },
        credentials: 'include',
      });
      console.log(response);
      setUserData(response.data);
      setProfileImage(response.data.profileImage);
      setMatchingMode(response.data.matchingMode);
      setIsNamePublic(response.data.matchingVisibility === 'PUBLIC');
      setLastModified(new Date(response.data.matchingModeLog));
      setNewSearchId(response.data.searchId); // 초기 아이디 설정
    } catch (error) {
      console.error("Error fetching user data:", error);
    } finally {
      setIsLoading(false); // 데이터 로드 완료 후 로딩 상태 해제
    }
  };
	
  useEffect(() => {
    fetchUserData();
  }, [navigate]); 

  const handleToggleMatchingMode = () => {
    setShowModal(true); // 토글 클릭 시 모달 표시
  };

  const handleNameVisibilityChange = (event) => {
    setIsNamePublic(event.target.value === 'PUBLIC');
    setShowVisibilityModal(true);
  };

  const handleModalConfirm = async () => {
    try {
      await apiClient.post(
        '/api/member/matching-mode',
        {
          matchingMode: !matchingMode,
          matchingVisibility: isNamePublic ? "PUBLIC" : "PRIVATE",
        },
        {
          headers: {
            "Content-Type": "application/json"
          },
          credentials: 'include',
        }
      );

      setMatchingMode(prevState => !prevState);
      setLastModified(new Date());
      setShowModal(false);
      toast.dismiss();
      toast.success('매칭 모드가 변경되었습니다.');
      sessionStorage.setItem('matchingMode', !matchingMode);
    } catch (error) {
      console.error('매칭 모드 변경 중 오류 발생:', error);
      toast.dismiss();
      toast.error('매칭 모드 변경 중 오류가 발생했습니다.');
    }
  };

  const handleVisibilityModalConfirm = async () => {
    try {
      await apiClient.post(
        '/api/member/matching-mode',
        {
          matchingMode: matchingMode,
          matchingVisibility: isNamePublic ? "PUBLIC" : "PRIVATE",
        },
        {
          headers: {
            "Content-Type": "application/json"
          },
          credentials: 'include',
        }
      );

      setShowVisibilityModal(false);
      toast.dismiss();
      toast.success('실명 공개 여부가 변경되었습니다.');
    } catch (error) {
      console.error('실명 공개 여부 변경 중 오류 발생:', error);
      toast.dismiss();
      toast.error('실명 공개 여부 변경 중 오류가 발생했습니다.');
    }
  };

  const handleModalCancel = () => {
    setShowModal(false); // 모달 숨기기
  };

  const handleVisibilityModalCancel = () => {
    setShowVisibilityModal(false); // 모달 숨기기
  };

  // 아이디 변경 시작
  const handleEditIdClick = () => {
    setIsEditingId(true);
    setButtonLabel("중복 확인");
  };

  // 아이디 입력 시 유효성 검사
  const handleIdChange = (event) => {
    const value = event.target.value;
    setNewSearchId(value);

    const idRegex = /^[a-z0-9_.-]{5,15}$/;
    const isValid = idRegex.test(value);
    setIsIdValid(isValid);

    if (isValid) {
      setButtonLabel("중복 확인");
    } else {
      setButtonLabel("중복 확인");
    }

    setIdStatusMessage(""); // 상태 메시지 초기화
  };

  // 아이디 중복 확인
  const handleCheckDuplicate = async () => {
    if (!isIdValid) return;

    try {
      const response = await apiClient.get(`/api/member/search-id/${newSearchId}`);

      if (response.data.duplicated) {
        setIsIdDuplicate(true);
        setButtonLabel("중복 확인");
        setIdStatusMessage("중복된 아이디입니다.");
      } else {
        setIsIdDuplicate(false);
        setButtonLabel("완료");
        setIdStatusMessage("사용 가능한 아이디입니다.");
      }
    } catch (error) {
      console.error("중복 확인 중 오류 발생:", error);
    }
  };

  // 아이디 최종 변경 완료
  const handleIdChangeConfirm = async () => {
    if (isIdDuplicate || !isIdValid) return;

    const formData = new FormData();

    const jsonData = JSON.stringify({
      searchId: newSearchId,
      a: 'a', // 백엔드에서 요청을 못 받아서 추가한 가짜 필드입니다.
    });

    const blob = new Blob([jsonData], { type: 'application/json' });
    formData.append('memberRequest', blob);

    try {
      await apiClient.put("/api/member/me", formData, {
          headers: {
            "Content-Type": "multipart/form-data"
          },
          credentials: 'include',
      });

      setIsEditingId(false);
      setButtonLabel("아이디 변경하기");
      setIdStatusMessage("");
      setUserData((userData) => ({
        ...userData,
        searchId: newSearchId,
      }));
      toast.dismiss();
      toast.success('아이디가 성공적으로 변경되었습니다.');
    } catch (error) {
      console.error("아이디 변경 중 오류 발생:", error);
    }
  };

  if (isLoading) {
    return (
      <div className={styles.spinner}>
        <div className={`${styles.heart} ${styles.heart1}`}></div>
        <div className={`${styles.heart} ${styles.heart2}`}></div>
        <div className={`${styles.heart} ${styles.heart3}`}></div>
        <div className={styles.loadingtext}>
          Loading
          <span className={styles.dot1}>.</span>
          <span className={styles.dot2}>.</span>
          <span className={styles.dot3}>.</span>
        </div>
      </div>
    );
  }

  return (
    <div className={styles.settingsContainer}>
      <ToastContainer 
        hideProgressBar={true}
        closeOnClick
        autoClose={700}
        limit={1}
        position="top-center"
      />
      <div className={styles.toptop}>
        <Link to="/mypage" className={styles.backButton}><i className="bi bi-chevron-left"/></Link>
        <h2>설정</h2>
      </div>
      <div className={styles.settingSection}>
        <h3 className={styles.settingSectionTitle}>아이디 변경</h3>
        <input 
          type="text" 
          value={isEditingId ? newSearchId : (userData?.searchId || "")}
          onChange={handleIdChange} 
          placeholder={userData?.searchId || "아이디를 입력하세요"}
          className={styles.inputField} 
          disabled={!isEditingId} 
        />
        <button 
          className={styles.changeIdButton} 
          onClick={isEditingId ? (buttonLabel === "완료" ? handleIdChangeConfirm : handleCheckDuplicate) : handleEditIdClick} 
          disabled={buttonLabel !== "아이디 변경하기" && !isIdValid}
        >
          {buttonLabel}
        </button>
        {idStatusMessage && <p className={styles.idStatusMessage}>{idStatusMessage}</p>}
      </div>

      <div className={styles.settingSection}>
        <h3 className={styles.settingSectionTitle}>매칭 모드 변경</h3>
        <div className={styles.matchingModeContainer}>
          <div className={styles.toggleSwitch}>
            <label className={styles.switch}>
              <input type="checkbox" checked={matchingMode} onChange={handleToggleMatchingMode} />
              <span className={styles.slider}></span>
            </label>
          </div>
          <p className={styles.matchingModeLog}>마지막 변경: {new Date(userData?.matchingModeLog).toLocaleDateString()}</p>
        </div>

        {matchingMode && (
          <div className={styles.matchingVisibilitySettingDiv}>
            <p align="center">매칭이 되면 지인에게 실명을 공개할까요?</p>
            <div className={styles.radioGroup}>
              <div>
                  <input 
                      type="radio"
                      id="public"
                      value="PUBLIC" 
                      checked={isNamePublic} 
                      onChange={handleNameVisibilityChange} 
                      className={styles.radioInput}
                  />
                  <label htmlFor="public" className={styles.radioLabel}>공개</label>
              </div>
              <div>
                  <input 
                      type="radio"
                      id="private"
                      value="PRIVATE" 
                      checked={!isNamePublic} 
                      onChange={handleNameVisibilityChange} 
                      className={styles.radioInput}
                  />
                  <label htmlFor="private" className={styles.radioLabel}>비공개</label>
              </div>
            </div>
          </div>
        )}
      </div>

      <div className={styles.footer}>
        <button 
          className={styles.leaveButton}
          onClick={() => {navigate('/leave')}}
        >
          회원탈퇴
        </button>
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

      <Modal
        isOpen={showVisibilityModal}
        onRequestClose={handleVisibilityModalCancel}
        shouldCloseOnOverlayClick={false}
        contentLabel="실명 공개 여부 변경"
        className={styles.modal}
        overlayClassName={styles.overlay}
      >
        <div>
          <h2>실명 공개 여부를<br />변경할까요?</h2>
          <button onClick={handleVisibilityModalCancel}>취소</button>
          <button onClick={handleVisibilityModalConfirm}>확인</button>
        </div>
      </Modal>
    </div>
  );
};

export default Settings;
