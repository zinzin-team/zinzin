import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom"; 
import axios from "axios";
import { ToastContainer, toast } from "react-toastify";
import { MdOutlineChangeCircle, MdEdit } from "react-icons/md";
import { useAuth } from '../../context/AuthContext';
import Modal from 'react-modal'; // 모달 추가
import 'react-toastify/dist/ReactToastify.css';
import styles from './MypageView.module.css'; 

const MypageView = () => {
  const [userData, setUserData] = useState(null);
  const [selectedFile, setSelectedFile] = useState(null);
  const [showLogoutModal, setShowLogoutModal] = useState(false); // 로그아웃 모달 상태 추가

  const navigate = useNavigate(); 
  const { logout } = useAuth();

  const fetchUserData = async () => {
    const accessToken = sessionStorage.getItem('accessToken');
    if (!accessToken) {
      console.error('No token found in session storage');
      navigate("/logout"); 
      return;
    }

    try {
      const response = await axios.get("/api/member/me", {
        headers: {
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "application/json"
        },
        credentials: 'include',
      });
      console.log(response);
      setUserData(response.data);
    } catch (error) {
      console.error("Error fetching user data:", error);
    }
  };

  useEffect(() => {
    fetchUserData();
  }, [navigate]); 

  useEffect(() => {
    if (userData) {
      console.log("User Data:", userData);
    }
  }, [userData]);

  if (!userData) {
    return <div>로딩 중...</div>;
  }

  const matchingProfileImage = userData.card?.images?.[0] || userData.profileImage;

  const handleProfileImageClick = () => {
    document.getElementById('fileInput').click();
  };

  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      setSelectedFile(file);
      updateProfileImage(file);
    }
  };

  const updateProfileImage = async (file) => {
    const accessToken = sessionStorage.getItem('accessToken');

    const formData = new FormData();
    formData.append('profileImage', file);

    const jsonData = JSON.stringify({
      searchId: userData.searchId,
    });

    const blob = new Blob([jsonData], { type: 'application/json' });
    formData.append('memberRequest', blob);
  
    console.log('프로필 이미지 업데이트 준비 중...');
    console.log('AccessToken:', accessToken);
    console.log('파일 정보:', file);
    console.log('SearchId:', userData.searchId);
  
    try {
      console.log('프로필 이미지 업데이트 요청을 전송 중...'); 
      const response = await axios.put("/api/member/me", formData, {
        headers: {
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "multipart/form-data"
        },
        credentials: 'include',
      });
  
      console.log('프로필 이미지 업데이트 성공:', response.data);
  
      // 업데이트된 내용을 반영하기 위해 사용자 데이터를 다시 가져옴
      fetchUserData();
    } catch (error) {
      console.error("프로필 이미지 업데이트 중 오류 발생:", error);
  
      if (error.response) {
        console.error("응답 데이터:", error.response.data);
        console.error("응답 상태 코드:", error.response.status);
        console.error("응답 헤더:", error.response.headers);
      } else if (error.request) {
        console.error("요청이 이루어졌으나 응답이 없습니다:", error.request);
      } else {
        console.error("요청 설정 중 오류 발생:", error.message);
      }
    }
  };

  const handleNicknameChange = async () => {
    const accessToken = sessionStorage.getItem('accessToken');

    try {
      const response = await axios.post("/api/member/nickname", {}, {
        headers: {
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "application/json"
        },
        credentials: 'include',
      });

      console.log('닉네임 변경 성공:', response.data);
      setUserData(prevData => ({
        ...prevData,
        nickname: response.data.nickname,
      }));
      toast.success("닉네임이 변경되었습니다!");
    } catch (error) {
      console.error("닉네임 변경 중 오류 발생:", error);
    }
  };

  const handleInviteButtonClick = () => {
    const loginUrl = "https://zin-zin.site/login";
    navigator.clipboard.writeText(loginUrl).then(() => {
      console.log("초대링크를 클립보드에 저장했어요! :)");
      toast.success("초대링크를 클립보드에 저장했어요! :)");
    }).catch(err => {
      console.error('링크 복사 중 오류 발생:', err);
    });
  };

  const handleLogoutClick = () => {
    setShowLogoutModal(true); // 로그아웃 모달 표시
  };

  const handleLogoutConfirm = () => {
    sessionStorage.clear();
    logout();
    window.location.href = '/login';
  };

  const handleLogoutCancel = () => {
    setShowLogoutModal(false); // 로그아웃 모달 숨기기
  };

  return (
    <div className={styles.mypageContainer}>
      <ToastContainer 
        hideProgressBar={true}
        closeOnClick
        autoClose={800}
        limit={1}
      />
      <div className={styles.userInfoBox}>
        <div className={styles.userInfoTop}>
          <div className={styles.profileImageContainer}>
            <img 
              src={userData.profileImage === 'default.jpg' ? `${process.env.REACT_APP_BASE_URL}/assets/default.png` : userData.profileImage} 
              alt="프로필" 
              className={styles.profileImage} 
              onClick={handleProfileImageClick} 
              onError={(e) => { e.target.src = `${process.env.REACT_APP_BASE_URL}/assets/default.png`; }}
            />
            <button 
              className={styles.imageEditButton}
              onClick={handleProfileImageClick}
            >
              <MdEdit  size={18}/>
            </button>
            <input 
              type="file" 
              id="fileInput" 
              style={{ display: 'none' }} 
              onChange={handleFileChange} 
              accept="image/*"
            />
          </div>
          <div className={styles.userInfoRight}>
            <div className={styles.nicknameContainer}>
              <h3>{userData.nickname}</h3>
              <button className={styles.editNicknameButton} onClick={handleNicknameChange}>
                <MdOutlineChangeCircle size={24}/>
              </button>
            </div>
            <div className={styles.nameContainer}>
              <p className={styles.nameText}>{userData.name} 님</p>
              <p className={styles.searchIdText}>@{userData.searchId}</p>
            </div>    

            <div className={styles.buttonGroup}>
              <button onClick={() => navigate('/friends')}>카톡 친구</button>
              <button onClick={() => navigate('/friends/z-in')}>나의 지인</button>
            </div>
          </div>
        </div>
        <div className={styles.userInfoBottom}>
          <div className={styles.inviteContainer}>
            <input 
              type="text" 
              placeholder="https://zin-zin.site/login" 
              className={styles.inviteInput} 
              readOnly 
            />
            <button 
              className={styles.inviteButton}
              onClick={handleInviteButtonClick}
            >
              초대하기
            </button>
          </div>
        </div>
      </div>
      <div className={styles.matchingModeBox}>
        <div className={styles.matchingModeTop}>
          <h3 className={styles.matchingModeText}>매칭 모드 {userData.matchingMode ? "ON" : "OFF"}</h3>
          <p>마지막 변경: {new Date(userData.matchingModeLog).toLocaleDateString()}</p>
        </div>
        <div className={styles.matchingModeBottom}>
          {userData.matchingMode && userData.hasCard ? (
            <div className={styles.matchingContent}>
              <div className={styles.matchingProfileImageContainer}>
                <img 
                  src={matchingProfileImage ? matchingProfileImage : `${process.env.REACT_APP_BASE_URL}/assets/default.png`} 
                  alt="프로필" 
                  className={styles.profileImageSmall} 
                />
              </div>
              <div className={styles.matchingUserInfo}>
                <p>{userData.card ? userData.card.info : "소개말을 입력해 주세요."}</p>
                
                <button 
                  className={styles.cardEditButton} 
                  onClick={() => navigate(`/update-card/${userData.card?.id}`)}
                >
                  수정 하기
                </button>
              </div>
            </div>
          ) : (
            <div className={styles.noCardMessage}>
              <p>생성 된 카드가 없습니다</p>
            </div>
          )}
        </div>
      </div>
      <div className={styles.settingsBox}>
        <button onClick={() => navigate('/settings')}>설정</button>
        <button onClick={() => navigate('/userguide')}>사용자 가이드</button>
      </div>
      <button 
        className={styles.logoutButton}
        onClick={handleLogoutClick}
      >
        로그아웃
      </button>

      <Modal
        isOpen={showLogoutModal}
        onRequestClose={handleLogoutCancel}
        shouldCloseOnOverlayClick={false}
        contentLabel="로그아웃 확인"
        className={styles.modal}
        overlayClassName={styles.overlay}
      >
        <div>
          <h2>로그아웃 하시겠습니까?</h2>
          <button onClick={handleLogoutCancel}>취소</button>
          <button onClick={handleLogoutConfirm}>확인</button>
        </div>
      </Modal>
    </div>
  );
};

export default MypageView;