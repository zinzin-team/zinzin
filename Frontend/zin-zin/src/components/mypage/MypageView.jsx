import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom"; 
import apiClient from '../../api/apiClient';
import { ToastContainer, toast } from "react-toastify";
import { MdOutlineChangeCircle, MdEdit } from "react-icons/md";
import { useAuth } from '../../context/AuthContext';
import Modal from 'react-modal'; // 모달 추가
import 'react-toastify/dist/ReactToastify.css';
import styles from './MypageView.module.css'; 
import ReactCardFlip from 'react-card-flip';

const MypageView = () => {
  const [userData, setUserData] = useState(null);
  const [selectedFile, setSelectedFile] = useState(null);
  const [showLogoutModal, setShowLogoutModal] = useState(false); // 로그아웃 모달 상태 추가
  const [isFront, setIsFront] = useState(true);
  const [isLoading, setIsLoading] = useState(true); // 로딩 상태 추가
  const [age, setAge] = useState(null);

  const navigate = useNavigate(); 
  const { logout } = useAuth();

  const calculateAge = (birthdate) => {
    const today = new Date();
    const birthDate = new Date(birthdate);
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();
    
    // 생일이 지나지 않았다면 나이에서 1을 뺍니다.
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }
    return age;
  };

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
          "Content-Type": "application/json"
        },
        credentials: 'include',
      });
      setUserData(response.data);

      const birth = response.data.birth;
      if (birth) {
        setAge(calculateAge(birth))
      }
    } catch (error) {
      console.error("Error fetching user data:", error);
    } finally {
      setIsLoading(false); // 데이터 로드가 완료되면 로딩 상태를 false로 설정
    }
  };

  useEffect(() => {
    fetchUserData();
  }, [navigate]); 

  useEffect(() => {
    // if (userData) {
      // console.log("User Data:", userData);
    // }
  }, [userData]);

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

  const matchingProfileImage = userData.card?.images?.[0] || userData.profileImage;

  const handleCreateCard = () => {
    navigate('/create-card');
  };
  
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
    const formData = new FormData();
    formData.append('profileImage', file);

    const jsonData = JSON.stringify({
      searchId: userData.searchId,
      a: 'a', // 백엔드에서 요청을 못 받아서 추가한 가짜 필드입니다.
    });

    const blob = new Blob([jsonData], { type: 'application/json' });
    formData.append('memberRequest', blob);

    try {
      const response = await apiClient.put("/api/member/me", formData, {
        headers: { 
          "Content-Type": "multipart/form-data"
        },
        credentials: 'include',
      });

      // console.log('프로필 이미지 업데이트 성공:', response.data);

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
    try {
      const response = await apiClient.post("/api/member/nickname", {}, {
        headers: {
          "Content-Type": "application/json"
        },
        credentials: 'include',
      });

      // console.log('닉네임 변경 성공:', response.data);
      setUserData(prevData => ({
        ...prevData,
        nickname: response.data.nickname,
      }));
      toast.dismiss();
      toast.success("닉네임이 변경되었습니다!");
    } catch (error) {
      console.error("닉네임 변경 중 오류 발생:", error);
    }
  };

  const handleInviteButtonClick = () => {
    const loginUrl = "https://zin-zin.site";
    navigator.clipboard.writeText(loginUrl).then(() => {
      // console.log("초대링크를 클립보드에 저장했어요! :)");
      toast.dismiss();
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

  const handleCardFlip = () => {
    setIsFront(!isFront);
  };
  
  const ImageStack = ({ images }) => {
    const [imageOrder, setImageOrder] = useState([0, 1, 2]);

    const handleImageClick = () => {
      setImageOrder(prevOrder => {
        const newOrder = [...prevOrder];
        const first = newOrder.shift(); // 첫번째 요소를 빼내고
        newOrder.push(first); // 마지막에 추가
        return newOrder;
      });
    };

    return (
      <div className={styles.imageStackContainer} onClick={handleImageClick}>
        {imageOrder.map((index, position) => (
          <img
            key={index}
            src={images[index]}
            className={`${styles.cardImage} ${styles[`position${position}`]}`}
            alt={`Image ${index}`}
          />
        ))}
      </div>
    );
  };

  return (
    <div className={styles.mypageContainer}>
      <ToastContainer 
        hideProgressBar={true}
        closeOnClick
        autoClose={700}
        limit={1}
        position="top-center"
      />
      <h2 className={styles.headerContainer}>마이페이지</h2>
      <div className={styles.userInfoBox}>
        <div className={styles.userInfoTop}>
          <div className={styles.profileImageContainer}>
            <img 
              src={userData.profileImage === 'default.jpg' ? `${process.env.REACT_APP_BASE_URL}/assets/default.png` : userData.profileImage} 
              alt="프로필" 
              className={styles.profileImage} 
              onError={(e) => { e.target.src = `${process.env.REACT_APP_BASE_URL}/assets/default.png`; }}
            />
            <button 
              className={styles.imageEditButton}
              onClick={handleProfileImageClick}
            >
              <MdEdit size={18} className={styles.imageEditIcon}/>
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
              링크 복사
            </button>
          </div>
        </div>
      </div>
      <div className={styles.matchingModeBox}>
          <div className={styles.editCardButtonContainer}>
          <h3 className={styles.boxTitle}>내 카드 정보</h3>
          {userData.hasCard && userData.matchingMode ? (
            <button 
              className={styles.cardEditButton}
              onClick={() => navigate(`/update-card/${userData.card?.id}`)}>
              <MdEdit  size={19}/>
            </button>

          ) : <div></div>
          }
          </div>
          <div className={styles.matchingModeBottom} style={{ padding: userData.matchingMode ? '10px 20px' : '0' }}>
            {userData.matchingMode ? (
              userData.hasCard ? (
                <div>
                  <ReactCardFlip isFlipped={!isFront} flipDirection="horizontal">
                    <div
                        className={`${styles.card} ${styles.front}`}
                    >
                        <ImageStack images={userData.card.images} />
                    </div>
                    <div>
                        <div className={styles.backContent}>
                            <div className={styles.tagContainer}>
                                <p className={styles.sectionTitle}>💖 나는 이런 사람이예요</p>
                                <div className={styles.longtext1}>
                                {userData.card.tags.map((tag, index) => (
                                    <span key={index} className={styles.tag}>{tag}</span>
                                ))}
                                </div>
                            </div>
                            <div className={styles.introContainer}>
                                <p className={styles.sectionTitle}>💖 나의 한줄 소개</p>
                                <p className={styles.longtext2}>{userData.card ? userData.card.info : "소개말을 입력해 주세요."}</p>
                            </div>
                          </div>
                      </div>
                  </ReactCardFlip>
                  <div className={styles.cardbottom}>
                    <div className={styles.cardbuttomtext}>
                        <p>{userData.nickname}{
                            userData.gender === 'MALE' ? <span className={styles.maleSpan}><i className="bi bi-gender-male"></i></span>
                                : <span className={styles.femaleSpan}><i className="bi bi-gender-female"></i></span>
                        }</p>
                    <p>만 {age}세</p>
                    </div>
                    <button className={styles.flipbutton} onClick={handleCardFlip}>
                        <svg className={styles.flipicon} fill="none" height="20" viewBox="0 0 20 20" width="20" xmlns="http://www.w3.org/2000/svg">
                            <g stroke="#FF9494" strokeLinecap="round" strokeWidth="1.5">
                                <path d="m3.33337 10.8333c0 3.6819 2.98477 6.6667 6.66663 6.6667 3.682 0 6.6667-2.9848 6.6667-6.6667 0-3.68188-2.9847-6.66664-6.6667-6.66664-1.29938 0-2.51191.37174-3.5371 1.01468"></path>
                                <path d="m7.69867 1.58163-1.44987 3.28435c-.18587.42104.00478.91303.42582 1.0989l3.28438 1.44986"></path>
                            </g>
                        </svg>

                        <span className={styles.fliplabel}></span>
                    </button>
                  </div>
                </div>
                ) : (
                  <div className={styles.noCardMessage}>
                    <p>생성된 카드가 없습니다.</p>
                    <button className={styles.inviteButton}  onClick={handleCreateCard}>카드 만들기</button>
                  </div>
                )
              ) : <div className={styles.matchingOffContent}><p><b>매칭 OFF</b> 상태입니다.</p><p>카드 정보를 원하시는 경우</p><p>설정에서 매칭 모드를 변경해주세요.</p></div>
            }
        </div>
      </div>
      <div className={styles.settingsBox}>
        <button className={styles.settingsButton} onClick={() => navigate('/settings')}>설정<i className="bi bi-chevron-right"/></button>
        <button className={styles.settingsButton} id={styles.userGuideButton} onClick={() => navigate('/userguide')}>FAQ<i className="bi bi-chevron-right"/></button>
      </div>
      <div className={styles.footer}>
        <button 
          className={styles.logoutButton}
          onClick={handleLogoutClick}
        >
          로그아웃
        </button>
      </div>

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
