import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
// import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import styles from './KakaoFriendsList.module.css';

const KakaoFriendsList = () => {
  // const navigate = useNavigate();

  const [friends, setFriends] = useState([]);
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [unfriendModalIsOpen, setUnfriendModalIsOpen] = useState(false);
  const [selectedFriend, setSelectedFriend] = useState(null);
  const [inviteModalIsOpen, setInviteModalIsOpen] = useState(false);

  useEffect(() => {
    const fetchFriends = async () => {
      const accessToken = sessionStorage.getItem('accessToken');
      if (!accessToken) {
        console.error('No token found in session storage');
        setLoading(false);
        // navigate("/logout");
        return;
      }

      try {
        const responseFriends = await axios.get('/api/mates/social-friends', {
            headers: {
              "Authorization": `Bearer ${accessToken}`,
              "Content-Type": "application/json"
          },
          credentials: 'include',
        });
        const responseRequests = await axios.get('/api/mates/requests', {
            headers: {
              "Authorization": `Bearer ${accessToken}`,
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

  const handleAccept = async (isAccepted) => {
    const accessToken = sessionStorage.getItem('accessToken');
    const userMemberId = sessionStorage.getItem('userMemberId');
    try {
      await axios.put('/api/mates', {
        userMemberId,
        targetMemberId: selectedRequest.id,
        isAccepted
      }, {
        headers: {
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "application/json"
        }
      });
      // 요청 수락/거절 후 리스트 갱신 로직 추가
      setRequests(requests.filter(request => request.id !== selectedRequest.id));
    } catch (error) {
      console.error('요청 처리 중 오류 발생:', error);
    }
    closeModal();
  };

  const handleUnfriend = async () => {
    // 친구 끊기 로직 추가
    // 예: await axios.delete(`/api/mates/${selectedFriend.id}`, { headers: { ... } });
    setFriends(friends.filter(friend => friend.kakaoName !== selectedFriend.kakaoName));
    closeUnfriendModal();
  };

  const handleInvite = async () => {
    // const accessToken = sessionStorage.getItem('accessToken');
    // const userMemberId = sessionStorage.getItem('userMemberId');
    // try {
    //   await axios.post('/api/mates', {
    //     userMemberId,
    //     targetMemberId: selectedFriend.id
    //   }, {
    //     headers: {
    //       "Authorization": `Bearer ${accessToken}`,
    //       "Content-Type": "application/json"
    //     }
    //   });
    //   // 요청 후 리스트 갱신 로직 추가
    // } catch (error) {
    //   console.error('요청 처리 중 오류 발생:', error);
    // }
    closeInviteModal();
  };

  if (loading) {
    return <div>로딩 중...</div>;
  }

  return (
    <div className={styles.container}>
      {requests.length > 0 && (
        <>
          <div className={styles.subHeader}>지인 요청을 수락할까요?</div>
          <div className={styles.requestContainer}>
            {requests.map((request, index) => (
              <div key={index} className={styles.requestItem}>
                <img
                  src={request.profileImage ? request.profileImage : 'default-profile.png'}
                  alt={`${request.kakaoName} 프로필`}
                  className={styles.avatar}
                />
                <span className={styles.kakaoName}>{request.kakaoName}</span>
                <button className={styles.requestButton} onClick={() => openModal(request)}>요청 수락 +</button>
              </div>
            ))}
          </div>
        </>
      )}
      <div className={styles.friendsList}>
        {friends.map((friend, index) => (
          <div key={index} className={styles.friendItem}>
            <img
              src={friend.profileImage ? friend.profileImage : 'default-profile.png'}
              alt={`${friend.kakaoName} 프로필`}
              className={styles.profileImage}
            />
            <span className={styles.kakaoName}>{friend.kakaoName}</span>
            <button className={
              friend.relationship === 'FOLLOW' ? styles.myAcquaintanceButton :
              friend.relationship === 'RECEIVE_REQUEST' ? styles.requestButton :
              styles.inviteButton
            } onClick={() => {
              if (friend.relationship === 'FOLLOW') {
                openUnfriendModal(friend);
              } else if (friend.relationship === 'RECEIVE_REQUEST') {
                openModal(friend);
              } else if (friend.relationship === 'MEMBER') {
                openInviteModal(friend);
              }
            }}>
              {friend.relationship === null && '초대 보내기'}
              {friend.relationship === 'MEMBER' && '지인 요청 +'}
              {friend.relationship === 'FOLLOW' && '나의 지인'}
              {friend.relationship === 'REQUEST_FOLLOW' && '요청 대기중'}
              {friend.relationship === 'RECEIVE_REQUEST' && '요청 수락 +'}
            </button>
          </div>
        ))}
      </div>
      <Modal
        isOpen={modalIsOpen}
        onRequestClose={closeModal}
        shouldCloseOnOverlayClick={false}
        contentLabel="지인 요청 수락"
        className={styles.modal}
        overlayClassName={styles.overlay}
      >
        {selectedRequest && (
          <div>
            <h2>{selectedRequest.kakaoName}님과 지인을 맺을까요?</h2>
            <button onClick={() => handleAccept(true)}>네</button>
            <button onClick={() => handleAccept(false)}>아니오</button>
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
            <h2>{selectedFriend.kakaoName}님과 지인을 끊을까요?</h2>
            <button onClick={handleUnfriend}>네</button>
            <button onClick={closeUnfriendModal}>아니오</button>
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
            <h2>{selectedFriend.kakaoName}님에게 지인을 요청할까요?</h2>
            <button onClick={handleInvite}>네</button>
            <button onClick={closeInviteModal}>아니오</button>
          </div>
        )}
      </Modal>
    </div>
  );
};

export default KakaoFriendsList;
