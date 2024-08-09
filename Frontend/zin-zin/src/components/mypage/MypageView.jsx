import React, { useEffect, useState } from "react";
// import axios from "axios";
import styles from './MypageView.module.css'; // CSS 모듈을 임포트하세요.
import LogoutButton from './LogoutButton'; // LogoutButton 컴포넌트를 임포트하세요.

const MypageView = () => {
  const [userData, setUserData] = useState(null);

  useEffect(() => {
    // const fetchUserData = async () => {
    //   try {
    //     const response = await axios.get("/api/member/me", {
    //       headers: {
    //         accesstoken: "token"
    //       }
    //     });
    //     setUserData(response.data);
    //   } catch (error) {
    //     console.error("Error fetching user data:", error);
    //   }
    // };

    // fetchUserData();

    // 더미 데이터 설정
    const dummyData = {
      email: "example@naver.com",
      name: "홍길동",
      nickname: "씩씩한 호랑이",
      birth: "2000-07-04",
      gender: "FEMALE",
      profileImage: "default.jpg",
      searchId: "gildong",
      matchingMode: true,
      matchingModeLog: "2024-08-01T17:17:10.398592",
      hasCard: true,
      card: {
        id: 1,
        info: "안녕하세요 뭐라할지 모르겠네요",
        images: [
          "drfeww1.jpg", "a12.jpg", "g214.jpg"
        ]
      }
    };

    setUserData(dummyData);
  }, []);

  if (!userData) {
    return <div>로딩 중...</div>;
  }

  return (
    <div className={styles.mypageContainer}>
      <div className={styles.userInfoBox}>
        <img src={userData.profileImage} alt="프로필" className={styles.profileImage} />
        <div className={styles.userInfo}>
          <h2>{userData.nickname}</h2>
          <p>{userData.name} 님</p>
          <p>@{userData.searchId}</p>
          <div className={styles.buttonGroup}>
            <button>카톡 친구</button>
            <button>내게 저장</button>
          </div>
          <input type="text" placeholder="초대 링크 생성" className={styles.inviteInput} />
        </div>
      </div>
      <div className={styles.matchingModeBox}>
        <h3>매칭 모드 {userData.matchingMode ? "ON" : "OFF"}</h3>
        <p>마지막 변경: {new Date(userData.matchingModeLog).toLocaleDateString()}</p>
        <img src={userData.profileImage} alt="프로필" className={styles.profileImageSmall} />
        <p>{userData.card ? userData.card.info : "소개말을 입력해 주세요."}</p>
        <button className={styles.introButton}>수정 하기</button>
      </div>
      <div className={styles.settingsBox}>
        <button>설정</button>
        <button>FAQ</button>
      </div>
      <div className={styles.logoutBox}>
        <LogoutButton /> {/* LogoutButton 컴포넌트를 사용하세요. */}
      </div>
    </div>
  );
};

export default MypageView;
