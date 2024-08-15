import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import apiClient from '../../api/apiClient';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import styles from './AcquaintancesList.module.css';

const AcquaintancesList = () => {
  const [acquaintances, setAcquaintances] = useState([]);
  const [loading, setLoading] = useState(true);
  const [unfriendModalIsOpen, setUnfriendModalIsOpen] = useState(false);
  const [selectedAcquaintance, setSelectedAcquaintance] = useState(null);

  useEffect(() => {
    const fetchAcquaintances = async () => {
      const accessToken = sessionStorage.getItem('accessToken');
      if (!accessToken) {
        console.error('No token found in session storage');
        setLoading(false);
        return;
      }

      try {
        const response = await apiClient.get('/api/mates', {
          headers: {
            "Authorization": `Bearer ${accessToken}`,
            "Content-Type": "application/json"
          }
        });
        setAcquaintances(response.data);
        setLoading(false);
      } catch (error) {
        console.error('지인 목록을 가져오는 중 오류 발생:', error);
        setLoading(false);
        toast.dismiss()
        toast.error('지인 목록을 가져오는 중 오류가 발생했습니다.');
      }
    };

    fetchAcquaintances();
  }, []);

  const openUnfriendModal = (acquaintance) => {
    setSelectedAcquaintance(acquaintance);
    setUnfriendModalIsOpen(true);
  };

  const closeUnfriendModal = () => {
    setSelectedAcquaintance(null);
    setUnfriendModalIsOpen(false);
  };

  const handleUnfriend = async () => {
    const accessToken = sessionStorage.getItem('accessToken');
    const userMemberId = sessionStorage.getItem('userMemberId');
  
    try {
      const response = await apiClient.delete('/api/mates', {
        headers: {
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "application/json"
        },
        data: {
          userMemberId: userMemberId,
          targetMemberId: selectedAcquaintance.id
        }
      });
  
      if (response.status === 200) { // 성공적으로 요청이 완료된 경우
        toast.dismiss()
        toast.success(`${selectedAcquaintance.kakaoName?selectedAcquaintance.kakaoName:selectedAcquaintance.name}님과 지인관계가 해제되었습니다ㅠㅠ`);
        setAcquaintances(acquaintances.filter(acquaintance => acquaintance.id !== selectedAcquaintance.id));
      }
    } catch (error) {
      console.error('오류:', error.message);
        toast.dismiss()
        toast.error('지인 해제 중 오류가 발생했습니다.');
    }
  
    closeUnfriendModal();
    window.location.reload();
  };

  if (loading) {
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
    <div className={styles.container}>
      <ToastContainer 
        hideProgressBar={true}
        closeOnClick
        autoClose={700}
        limit={1}
        position="top-center"
      />
      <div className={styles.friendsList}>
        {acquaintances.map((acquaintance, index) => (
          <div key={index} className={styles.friendItem}>
            <img
              src={!acquaintance.profileImagePath || acquaintance.profileImagePath === 'default.jpg' ? `${process.env.REACT_APP_BASE_URL}/assets/default.png` : acquaintance.profileImagePath}
              alt={`${acquaintance.name} 프로필`}
              className={styles.profileImage}
            />
            <span className={styles.kakaoName}>{acquaintance.name}</span>
            <button className={styles.myAcquaintanceButton} onClick={() => openUnfriendModal(acquaintance)}>
              지인 해제
            </button>
          </div>
        ))}
      </div>
      <Modal
        isOpen={unfriendModalIsOpen}
        onRequestClose={closeUnfriendModal}
        shouldCloseOnOverlayClick={false}
        contentLabel="지인 끊기"
        className={styles.modal}
        overlayClassName={styles.overlay}
      >
        {selectedAcquaintance && (
          <div>
            <h2>{selectedAcquaintance.name}님과<br />지인관계를 해제할까요?</h2>
            <button onClick={closeUnfriendModal}>취소</button>
            <button onClick={handleUnfriend}>해제</button>
          </div>
        )}
      </Modal>
    </div>
  );
};

export default AcquaintancesList;
