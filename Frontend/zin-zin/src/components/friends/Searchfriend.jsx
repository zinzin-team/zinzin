import React, { useState } from 'react';
import Modal from 'react-modal';
import axios from 'axios';
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

    if (!accessToken) {
      setError('로그인이 필요합니다.');
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

      // 첫 번째 API 호출: searchId로 사용자 검색
      const response = await axios.get(`/api/search/${encodeURIComponent(sanitizedSearchId)}`, {
        headers: {
          'Authorization': `Bearer ${accessToken}`,
          'Content-Type': 'application/json'
        },
        credentials: 'include',
      });

      const data = response.data;

      if (data.success) {
        setResult(data.member);
        // console.log(result)

        // 자신을 검색한 경우
        if (data.member.id === parseInt(memberId, 10)) {
          setIsMe(true);
        } else {
          // 두 번째 API 호출: 나의 친구 목록 가져오기
          const friendsResponse = await axios.get('/api/mates/social-friends', {
            headers: {
              'Authorization': `Bearer ${accessToken}`,
              'Content-Type': 'application/json'
            },
            credentials: 'include',
          });

          const friends = friendsResponse.data;

          // friends 배열에서 검색된 사용자를 찾기
          const relationshipWithSearchedUser = friends.find(
            (friend) => friend.memberId === data.member.id
          );

          // 찾은 friend 객체를 상태로 저장
          setRelationship(relationshipWithSearchedUser);
          console.log(relationship.relationship)
        }
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
    const accessToken = sessionStorage.getItem('accessToken');
    try {
      await axios.put('/api/mates', {
        targetMemberId: result.id,
        accepted: true
      }, {
        headers: {
          'Authorization': `Bearer ${accessToken}`,
          'Content-Type': 'application/json'
        }
      });

      toast.success(`${result.name}님과 지인이 되었습니다.`);
    } catch (error) {
      console.error('요청 처리 중 오류 발생:', error);
      toast.error('요청 처리 중 오류가 발생했습니다.');
    }
    closeModal();
  };

  const handleUnfriend = async () => {
    const accessToken = sessionStorage.getItem('accessToken');
    const userMemberId = sessionStorage.getItem('memberId');

    try {
      await axios.delete('/api/mates', {
        headers: {
          'Authorization': `Bearer ${accessToken}`,
          'Content-Type': 'application/json'
        },
        data: {
          userMemberId: userMemberId,
          targetMemberId: result.id
        }
      });

      setRelationship({ ...relationship, relationship: 'MEMBER' });
      toast.success(`${result.name}님과 지인관계가 해제되었습니다.`);
    } catch (error) {
      console.error('오류:', error.message);
      toast.error('지인 해제 중 오류가 발생했습니다.');
    }
    closeModal();
  };

  const handleInvite = async () => {
    const accessToken = sessionStorage.getItem('accessToken');
    const userMemberId = sessionStorage.getItem('memberId');

    try {
      await axios.post('/api/mates', {
        userMemberId: userMemberId,
        targetMemberId: result.id
      }, {
        headers: {
          'Authorization': `Bearer ${accessToken}`,
          'Content-Type': 'application/json'
        }
      });

      setRelationship({ ...relationship, relationship: 'REQUEST_FOLLOW' });
      toast.success(`${result.name}님에게 지인 요청을 보냈습니다.`);
    } catch (error) {
      console.error('지인 요청 중 오류 발생:', error);
      toast.error('지인 요청 중 오류가 발생했습니다.');
    }
    closeModal();
  };

  const renderButton = () => {
    if (isMe) {
      return (
        <button className={styles.meButton} disabled>
          me
        </button>
      );
    }

    if (!relationship) {
      // relationship이 null이거나 undefined일 경우에도 MEMBER 케이스로 처리
      return (
        <button
          className={styles.memberButton}
          onClick={() => openModal('invite')}
        >
          지인 요청 +
        </button>
      );
    }

    // relationship 상태에서 relationship 속성에 접근하여 버튼 표시
    switch (relationship.relationship) {
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
      <ToastContainer />
      <p>아이디로 친구를 찾을 수 있어요!</p>
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
      {loading && <p>검색 중...</p>}
      {error && <p className={styles.error}>{error}</p>}
      {result && (
        <div className={styles.result}>
          <img
            src={result.profileImage ? result.profileImage : `${process.env.REACT_APP_BASE_URL}/assets/default.png`}
            alt={`${result.name} 프로필`}
            className={styles.profileImage}
          />
          <span className={styles.kakaoName}>{result.name}</span>
          {renderButton()}
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
