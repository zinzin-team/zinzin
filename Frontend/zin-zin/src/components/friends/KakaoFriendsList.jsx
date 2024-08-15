import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import apiClient from '../../api/apiClient';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import styles from './KakaoFriendsList.module.css';

const KakaoFriendsList = () => {
  const [friends, setFriends] = useState([]);
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [unfriendModalIsOpen, setUnfriendModalIsOpen] = useState(false);
  const [selectedFriend, setSelectedFriend] = useState(null);
  const [inviteModalIsOpen, setInviteModalIsOpen] = useState(false);
  const [modalType, setModalType] = useState(null); // 'accept' 또는 'reject'로 모달 타입을 구분

  Modal.setAppElement('#root');

  useEffect(() => {
    const fetchFriends = async () => {
      const accessToken = sessionStorage.getItem('accessToken');
      if (!accessToken) {
        console.error('No token found in session storage');
        setLoading(false);
        return;
      }

      try {
        const responseFriends = await apiClient.get('/api/mates/social-friends', {
          headers: {
            "Content-Type": "application/json"
          },
          credentials: 'include',
        });

        const responseRequests = await apiClient.get('/api/mates/requests', {
          headers: {
            "Content-Type": "application/json"
          },
          credentials: 'include',
        });

        setFriends(responseFriends.data);
        setRequests(responseRequests.data);
        setLoading(false);
      } catch (error) {
        console.error('친구 목록을 가져오는 중 오류 발생:', error);
        setLoading(false);
      }
    };

    fetchFriends();
  }, []);

  const openModal = (request) => {
    setSelectedRequest(request);
    setModalIsOpen(true);
  };

  const openAcceptModal = (item) => {
    const formattedItem = {
        id: item.id || item.memberId,
        profileImagePath: item.profileImagePath || item.profileImage,
        name: item.name || item.kakaoName,
    };
    setSelectedRequest(formattedItem);
    setModalType('accept');
    setModalIsOpen(true);
  };
  
  const openRejectModal = (item) => {
    const formattedItem = {
        id: item.id || item.memberId,
        profileImagePath: item.profileImagePath || item.profileImage,
        name: item.name || item.kakaoName,
    };
    setSelectedRequest(formattedItem);
    setModalType('reject');
    setModalIsOpen(true);
  };

  const closeModal = () => {
    setSelectedRequest(null);
    setModalIsOpen(false);
  };

  const openUnfriendModal = (friend) => {
    setSelectedFriend(friend);
    setUnfriendModalIsOpen(true);
  };

  const closeUnfriendModal = () => {
    setSelectedFriend(null);
    setUnfriendModalIsOpen(false);
  };

  const openInviteModal = (friend) => {
    setSelectedFriend(friend);
    setInviteModalIsOpen(true);
  };

  const closeInviteModal = () => {
    setSelectedFriend(null);
    setInviteModalIsOpen(false);
  };

  const handleAccept = async (accepted) => {
    try {
      await apiClient.put('/api/mates', {
        targetMemberId: selectedRequest.id,
        accepted
      }, {
        headers: {
          "Content-Type": "application/json"
        },
        credentials: 'include',
      });

      setRequests(requests.filter(request => request.id !== selectedRequest.id));
      
      if (accepted) {
        toast.dismiss();
        toast.success(`${selectedRequest.name} 님과 지인이 되었습니다.`);
      } else {
        toast.dismiss();
        toast.success(`${selectedRequest.name} 님의 지인 요청을 거절했습니다.`);
      }
    } catch (error) {
      console.error('요청 처리 중 오류 발생:', error);
      toast.dismiss();
      toast.error('요청 처리 중 오류가 발생했습니다.');
    }
    closeModal();
    window.location.reload();
  };

  const handleUnfriend = async () => {
    const userMemberId = sessionStorage.getItem('memberId');
  
    try {
      await apiClient.delete('/api/mates', {
        headers: {
          "Content-Type": "application/json"
        },
        data: {
          userMemberId: userMemberId,
          targetMemberId: selectedFriend.memberId
        },
        credentials: 'include',
      });
  
      setFriends(prevFriends =>
        prevFriends.map(friend =>
          friend.id === selectedFriend.memberId
            ? { ...friend, relationship: 'MEMBER' }
            : friend
        )
      );
  
      toast.dismiss();
      toast.success(`${selectedFriend.kakaoName} 님과 지인관계가 해제되었습니다ㅠㅠ`);
    } catch (error) {
      console.error('오류:', error.message);
    }
  
    closeUnfriendModal();
    window.location.reload();
  };

  const handleInvite = async () => {
    const userMemberId = sessionStorage.getItem('memberId');
  
    try {
      const response = await apiClient.post('/api/mates', {
        userMemberId: userMemberId,
        targetMemberId: selectedFriend.memberId
      }, {  
        headers: {
          "Content-Type": "application/json"
        }
      });
  
      setFriends(prevFriends =>
        prevFriends.map(friend =>
          friend.id === selectedFriend.memberId
            ? { ...friend, relationship: 'REQUEST_FOLLOW' }
            : friend
        )
      );
  
      toast.dismiss();
      toast.success(`${selectedFriend.kakaoName} 님에게 지인 요청을 보냈습니다.`);
    } catch (error) {
      console.error('Error during invite request:', error);
    }
  
    closeInviteModal();
    window.location.reload();
  };

  const handleNullButtonClick = () => {
    const loginUrl = "https://zin-zin.site/login";
    navigator.clipboard.writeText(loginUrl).then(() => {
      toast.dismiss();
      toast.success("초대링크를 클립보드에 저장했어요! :)");
    }).catch(err => {
      console.error('링크 복사 중 오류 발생:', err);
    });
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
      {requests.length > 0 && (
        <>
          <div className={styles.subHeader}>지인 요청을 수락할까요?</div>
          <div className={styles.requestContainer}>
            {requests.map((request, index) => (
              <div key={index} className={styles.requestItem}>
                <img
                  src={request.profileImagePath === 'default.jpg' ? `${process.env.REACT_APP_BASE_URL}/assets/default.png` : request.profileImagePath}
                  alt={`${request.name} 프로필`}
                  className={styles.avatar}
                  onError={(e) => { e.target.src = `${process.env.REACT_APP_BASE_URL}/assets/default.png`; }}
                />
                <span className={styles.kakaoName}>{request.name}</span>
                <button className={styles.geojeolButton} onClick={() => openRejectModal(request)}>거절</button>
                <button className={styles.surakButton} onClick={() => openAcceptModal(request)}>수락</button>
              </div>
            ))}
          </div>
        </>
      )}
      <div className={styles.friendsList}>
        {friends.map((friend, index) => (
          <div key={index} className={styles.friendItem}>
            <img
              src={friend.profileImage === 'default.jpg' ? `${process.env.REACT_APP_BASE_URL}/assets/default.png` : friend.profileImage}
              alt={`${friend.kakaoName} 프로필`}
              className={styles.profileImage}
              onError={(e) => { e.target.src = `${process.env.REACT_APP_BASE_URL}/assets/default.png`; }}
            />
            <span className={styles.kakaoName}>{friend.kakaoName}</span>

            {friend.relationship === 'RECEIVE_REQUEST' ? (
              <>
                <button className={styles.geojeolButton} onClick={() => openRejectModal(friend)}>거절</button>
                <button className={styles.surakButton} onClick={() => openAcceptModal(friend)}>수락</button>
              </>
            ) : (
              <button className={
                friend.relationship === 'FOLLOW' ? styles.followButton :
                friend.relationship === 'MEMBER' ? styles.memberButton :
                friend.relationship === 'REQUEST_FOLLOW' ? styles.requestFollowButton :
                styles.nullButton
              } onClick={() => {
                if (friend.relationship === 'FOLLOW') {
                  openUnfriendModal(friend);
                } else if (friend.relationship === 'MEMBER') {
                  openInviteModal(friend);
                } else if (friend.relationship === null) {
                  handleNullButtonClick();
                }
              }}>
                {friend.relationship === null && '초대 보내기'}
                {friend.relationship === 'MEMBER' && '지인 요청 +'}
                {friend.relationship === 'FOLLOW' && '지인 해제'}
                {friend.relationship === 'REQUEST_FOLLOW' && '요청 대기중'}
              </button>
            )} 
          </div>
        ))}
      </div>
      <Modal
        isOpen={modalIsOpen}
        onRequestClose={closeModal}
        shouldCloseOnOverlayClick={false}
        contentLabel="지인 요청"
        className={styles.modal}
        overlayClassName={styles.overlay}
      >
        {selectedRequest && modalType === 'accept' && (
          <div>
            <h2>{selectedRequest.name} 님의<br />지인 요청을 수락할까요?</h2>
            <button onClick={closeModal}>취소</button>
            <button onClick={() => handleAccept(true)}>수락</button>
          </div>
        )}
        {selectedRequest && modalType === 'reject' && (
          <div>
            <h2>{selectedRequest.name} 님의<br />지인 요청을 거절할까요?</h2>
            <button onClick={closeModal}>취소</button>
            <button onClick={() => handleAccept(false)}>거절</button>
          </div>
        )}
      </Modal>
      <Modal
        isOpen={unfriendModalIsOpen}
        onRequestClose={closeUnfriendModal}
        shouldCloseOnOverlayClick={false}
        contentLabel="지인 끊기"
        className={styles.modal}
        overlayClassName={styles.overlay}
      >
        {selectedFriend && (
          <div>
            <h2>{selectedFriend.kakaoName} 님과<br />지인관계를 해제할까요?</h2>
            <button onClick={closeUnfriendModal}>취소</button>
            <button onClick={handleUnfriend}>해제</button>
          </div>
        )}
      </Modal>
      <Modal
        isOpen={inviteModalIsOpen}
        onRequestClose={closeInviteModal}
        shouldCloseOnOverlayClick={false}
        contentLabel="지인 요청"
        className={styles.modal}
        overlayClassName={styles.overlay}
      >
        {selectedFriend && (
          <div>
            <h2>{selectedFriend.kakaoName} 님에게<br />지인을 요청할까요?</h2>
            <button onClick={closeInviteModal}>취소</button>
            <button onClick={handleInvite}>요청</button>
          </div>
        )}
      </Modal>
    </div>
  );
};

export default KakaoFriendsList;
