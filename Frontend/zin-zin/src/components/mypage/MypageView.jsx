import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom"; 
import axios from "axios";
import styles from './MypageView.module.css'; 
import LogoutButton from './LogoutButton'; 

const MypageView = () => {
  const [userData, setUserData] = useState(null);
  const navigate = useNavigate(); 
  const [selectedFile, setSelectedFile] = useState(null); 

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

  if (!userData) {
    return <div>로딩 중...</div>;
  }

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
    formData.append('searchId', userData.searchId); 

    try {
      await axios.put("/api/member/me", formData, {
        headers: {
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "multipart/form-data"
        }
      });

      fetchUserData();
    } catch (error) {
      console.error("Error updating profile image:", error);
    }
  };

  const matchingProfileImage = userData.card?.images?.[0] || userData.profileImage;

  return (
    <div className={styles.mypageContainer}>
      <div className={styles.userInfoBox}>
        <img 
          src={userData.profileImage} 
          alt="프로필" 
          className={styles.profileImage} 
          onClick={handleProfileImageClick} 
        />
        <input 
          type="file" 
          id="fileInput" 
          style={{ display: 'none' }} 
          onChange={handleFileChange} 
        />
        <div className={styles.userInfo}>
          <h2>{userData.nickname}</h2>
          <p>{userData.name} 님</p>
          <p>@{userData.searchId}</p>
          <div className={styles.buttonGroup}>
            <button onClick={() => navigate('/friends')}>카톡 친구</button>
            <button onClick={() => navigate('/friends/z-in')}>나의 지인</button>
          </div>
          {/* <input type="text" placeholder="초대 링크 생성" className={styles.inviteInput} /> */}
        </div>
      </div>
      <div className={styles.matchingModeBox}>
        <h3>매칭 모드 {userData.matchingMode ? "ON" : "OFF"}</h3>
        <p>마지막 변경: {new Date(userData.matchingModeLog).toLocaleDateString()}</p>
        <img 
          src={matchingProfileImage} 
          alt="프로필" 
          className={styles.profileImageSmall} 
        />
        <p>{userData.card ? userData.card.info : "소개말을 입력해 주세요."}</p>
        {/* 수정하기 버튼 클릭 시 cardId를 포함한 URL로 이동 */}
        <button 
          className={styles.introButton} 
          onClick={() => navigate(`/update-card/${userData.card?.id}`)}
        >
          수정 하기
        </button>
      </div>
      <div className={styles.settingsBox}>
        <button onClick={() => navigate('/settings')}>설정</button>
        <button>FAQ</button>
      </div>
      <div className={styles.logoutBox}>
        <LogoutButton />
      </div>
    </div>
  );
};

export default MypageView;
