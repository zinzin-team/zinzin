import React, { useState } from 'react';
import Modal from 'react-modal';
import apiClient from '../../api/apiClient';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import styles from './Searchfriend.module.css';

const Searchfriend = () => {
  const [searchId, setSearchId] = useState('');
  const [result, setResult] = useState(null);
  const [relationship, setRelationship] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [isMe, setIsMe] = useState(false);
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const [selectedAction, setSelectedAction] = useState(null);

  Modal.setAppElement('#root');

  const handleSearch = async () => {
    setLoading(true);
    setError('');
    setResult(null);
    setRelationship(null);
    setIsMe(false);

    const accessToken = sessionStorage.getItem('accessToken');
    const memberId = sessionStorage.getItem('memberId');
    const searchId2 =  sessionStorage.getItem('searchId');

    if (!accessToken) {
      setError('로그인이 필요합니다.');
      setLoading(false);
      return;
    }

    if(searchId === searchId2){
      setError('자신을 사랑하는 마음은 모든 위대한 사랑의 시작이다.');
      setLoading(false);
      return;
    }

    try {
      const sanitizedSearchId = searchId.replace(/[\/\\]/g, '');

      if (!sanitizedSearchId) {
        setError('유효한 검색어를 입력해주세요.');
        setLoading(false);
        return;
      }
      const response = await apiClient.get(`/api/search/${encodeURIComponent(sanitizedSearchId)}`, {
        headers: {
          'Content-Type': 'application/json'
        },
        credentials: 'include',
      });

      const data = response.data;
      if (data.success) {
        setResult(data.member);
      } else {
        setError('존재하지 않는 아이디입니다.');
      }
    } catch (err) {
      setError('검색 중 오류가 발생했습니다.');
    }
    setLoading(false);
  };

  const openModal = (action) => {
    setSelectedAction(action);
    setModalIsOpen(true);
  };

  const closeModal = () => {
    setSelectedAction(null);
    setModalIsOpen(false);
  };

  const handleAcceptRequest = async () => {
    try {
      await apiClient.put('/api/mates', {
        targetMemberId: result.id,
        accepted: true
      }, {
        headers: {
          'Content-Type': 'application/json'
        }
      });

      toast.dismiss();
      toast.success(`${result.name}님과 지인이 되었습니다.`);
    } catch (error) {
      toast.dismiss();
      toast.error('요청 처리 중 오류가 발생했습니다.');
    }
    closeModal();
  };

  const handleUnfriend = async () => {
    const userMemberId = sessionStorage.getItem('memberId');

    try {
      await apiClient.delete('/api/mates', {
        headers: {
          'Content-Type': 'application/json'
        },
        data: {
          userMemberId: userMemberId,
          targetMemberId: result.id
        }
      });

      setRelationship({ ...relationship, relationship: 'MEMBER' });
      toast.dismiss();
      toast.success(`${result.name}님과 지인관계가 해제되었습니다.`);

      setResult(prevResult => ({
        ...prevResult,
        relationships: ['MEMBER']
      }));
    } catch (error) {
      toast.dismiss();
      toast.error('지인 해제 중 오류가 발생했습니다.');
    }
    closeModal();
  };

  const handleInvite = async () => {
    const userMemberId = sessionStorage.getItem('memberId');

    try {
      await apiClient.post('/api/mates', {
        userMemberId: userMemberId,
        targetMemberId: result.id
      }, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
      setRelationship({ ...relationship, relationship: 'REQUEST_FOLLOW' });
      toast.dismiss();
      toast.success(`${result.name}님에게 지인 요청을 보냈습니다.`);

      setResult(prevResult => ({
        ...prevResult,
        relationships: ['REQUEST_FOLLOW']
      }));


    } catch (error) {
      toast.dismiss();
      toast.error('지인 요청 중 오류가 발생했습니다.');
    }
    closeModal();
  };

  const renderButton = (result) => {
    if (!result.relationships) {
      return (
        <button
          className={styles.memberButton}
          onClick={() => openModal('invite')}
        >
          지인 요청 +
        </button>
      );
    }

    switch (result.relationships[0]) {
      case 'FOLLOW':
        return (
          <button
            className={styles.followButton}
            onClick={() => openModal('unfriend')}
          >
            나의 지인
          </button>
        );
      case 'REQUEST_FOLLOW':
        return (
          <button className={styles.requestFollowButton} disabled>
            요청 대기중
          </button>
        );
      case 'RECEIVE_REQUEST':
        return (
          <button
            className={styles.receiveRequestButton}
            onClick={() => openModal('accept')}
          >
            요청 수락 +
          </button>
        );
      case 'MEMBER':
      default:
        return (
          <button
            className={styles.memberButton}
            onClick={() => openModal('invite')}
          >
            지인 요청 +
          </button>
        );
    }
  };

  return (
    <div className={styles.container}>
      <ToastContainer 
        hideProgressBar={true}
        closeOnClick
        autoClose={700}
        limit={1}
        position="top-center"
      />
      <p className={styles.searchInfo}>아이디를 통해 지인을 찾아보세요!</p>
      <div className={styles.searchContainer}>
        <input
          type="text"
          placeholder="아이디 입력"
          value={searchId}
          onChange={(e) => setSearchId(e.target.value)}
          onKeyPress={(e) => {
            if (e.key === 'Enter') {
              handleSearch();
            }
          }}
          className={styles.searchInput}
        />
        <button onClick={handleSearch} className={styles.searchButton}>
          검색
        </button>
      </div>
      {loading && (
        <div className={styles.spinner}>
          <div className={`${styles.heart} ${styles.heart1}`}></div>
          <div className={`${styles.heart} ${styles.heart2}`}></div>
          <div className={`${styles.heart} ${styles.heart3}`}></div>
          <div className={styles.loadingtext}>
            검색 중
            <span className={styles.dot1}>.</span>
            <span className={styles.dot2}>.</span>
            <span className={styles.dot3}>.</span>
          </div>
        </div>
      )}
{error && <p className={error === '자신을 사랑하는 마음은 모든 위대한 사랑의 시작이다.' ? styles.blueError : styles.error}>{error}</p>}
      {result && (
        <div className={styles.result}>
          <img
            src={!result.profileImagePath || result.profileImagePath === 'default.jpg' ? `${process.env.REACT_APP_BASE_URL}/assets/default.png` : result.profileImagePath}
            alt={`${result.name} 프로필`}
            className={styles.profileImage}
          />
          <span className={styles.kakaoName}>{result.name}</span>
          {renderButton(result)}
        </div>
      )}
      <Modal
        isOpen={modalIsOpen}
        onRequestClose={closeModal}
        shouldCloseOnOverlayClick={false}
        contentLabel="작업 확인"
        className={styles.modal}
        overlayClassName={styles.overlay}
      >
        {selectedAction === 'accept' && (
          <div>
            <h2>{result.name}님의<br />지인 요청을 수락할까요?</h2>
            <button onClick={closeModal}>취소</button>
            <button onClick={handleAcceptRequest}>수락하기</button>
          </div>
        )}
        {selectedAction === 'unfriend' && (
          <div>
            <h2>{result.name}님과<br />지인관계를 해제할까요?</h2>
            <button onClick={closeModal}>유지하기</button>
            <button onClick={handleUnfriend}>해제하기</button>
          </div>
        )}
        {selectedAction === 'invite' && (
          <div>
            <h2>{result.name}님에게<br />지인을 요청할까요?</h2>
            <button onClick={closeModal}>취소</button>
            <button onClick={handleInvite}>요청하기</button>
          </div>
        )}
      </Modal>
    </div>
  );
};

export default Searchfriend;
