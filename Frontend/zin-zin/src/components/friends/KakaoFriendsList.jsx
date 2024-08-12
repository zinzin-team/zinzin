import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import axios from 'axios';
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
        targetMemberId: selectedRequest.memberId,
        isAccepted
      }, {
        headers: {
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "application/json"
        }
      });

      setRequests(requests.filter(request => request.id !== selectedRequest.id));
      
      if (isAccepted) {
        toast.success(`${selectedRequest.kakaoName}님과 지인이 되어따`);
      } else {
        toast.info(`${selectedRequest.kakaoName}님의 지인 요청을 거절했습니다.`);
      }
    } catch (error) {
      console.error('요청 처리 중 오류 발생:', error);
      toast.error('요청 처리 중 오류가 발생했습니다.');
    }
    closeModal();
  };

  const handleUnfriend = async () => {
    const accessToken = sessionStorage.getItem('accessToken');
    const userMemberId = sessionStorage.getItem('userMemberId');
  
    try {
      await axios.delete('/api/mates', {
        headers: {
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "application/json"
        },
        data: {
          userMemberId: userMemberId,
          targetMemberId: selectedFriend.memberId
        }
      });
  
      setFriends(prevFriends =>
        prevFriends.map(friend =>
          friend.id === selectedFriend.memberId
            ? { ...friend, relationship: 'MEMBER' }
            : friend
        )
      );
  
      toast.success(`${selectedFriend.kakaoName}님과 지인관계가 해제되었습니다ㅠㅠ`);
    } catch (error) {
      console.error('오류:', error.message);
      // toast.error('지인 해제 중 오류가 발생했습니다.');
    }
  
    closeUnfriendModal();
  };

  const handleInvite = async () => {
    const accessToken = sessionStorage.getItem('accessToken');
    const userMemberId = sessionStorage.getItem('userMemberId');
    
    console.log('AccessToken:', accessToken);
    console.log('UserMemberId:', userMemberId);
    console.log('SelectedFriend:', selectedFriend);
  
    try {
      console.log('Sending invite request...');
      
      const response = await axios.post('/api/mates', {
        userMemberId: userMemberId,
        targetMemberId: selectedFriend.memberId
      }, {
        headers: {
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "application/json"
        }
      });
      
      console.log('Invite request successful:', response.data);
      
      setFriends(prevFriends =>
        prevFriends.map(friend =>
          friend.id === selectedFriend.memberId
            ? { ...friend, relationship: 'REQUEST_FOLLOW' }
            : friend
        )
      );
  
      toast.success(`${selectedFriend.kakaoName}님에게 지인 요청을 보냈습니당 :)`);
    } catch (error) {
      console.error('Error during invite request:', error);
      
      if (error.response && error.response.data) {
        const errorCode = error.response.data.code;
        console.log('Error code:', errorCode);
        
        if (errorCode === 'F001') {
          // toast.error('잘못된 요청입니다. 다시 시도해주세요.');
        } else if (errorCode === 'F002') {
          // toast.info('이미 지인 관계이거나 요청 중입니다.');
        } else {
          // toast.error('지인 요청 중 오류가 발생했습니다.');
        }
      } else {
        console.error('Unknown error occurred during invite request:', error);
        // toast.error('지인 요청 중 오류가 발생했습니다.');
      }
    }
  
    console.log('Closing invite modal...');
    closeInviteModal();
  };
  

  const handleNullButtonClick = () => {
    const loginUrl = "https://zin-zin.site/login";
    navigator.clipboard.writeText(loginUrl).then(() => {
      toast.success("초대링크를 클립보드에 저장했어요! :)");
    }).catch(err => {
      console.error('링크 복사 중 오류 발생:', err);
      // toast.error('링크 복사 중 오류가 발생했습니다.');
    });
  };

  if (loading) {
    return <div>로딩 중...</div>;
  }

  return (
    <div className={styles.container}>
      <ToastContainer />
      {requests.length > 0 && (
        <>
          <div className={styles.subHeader}>지인 요청을 수락할까요?</div>
          <div className={styles.requestContainer}>
            {requests.map((request, index) => (
              <div key={index} className={styles.requestItem}>
                <img
                  src={request.profileImage ? request.profileImage : '/assets/default-profile.png'}
                  alt={`${request.kakaoName} 프로필`}
                  className={styles.avatar}
                  onError={(e) => { e.target.src = '/assets/default-profile.png'; }}
                />
                <span className={styles.kakaoName}>{request.kakaoName}</span>
                <button className={styles.receiveRequestButton} onClick={() => openModal(request)}>요청 수락 +</button>
              </div>
            ))}
          </div>
        </>
      )}
      <div className={styles.friendsList}>
        {friends.map((friend, index) => (
          <div key={index} className={styles.friendItem}>
            <img
              src={friend.profileImage ? friend.profileImage : '/assets/default-profile.png'}
              alt={`${friend.kakaoName} 프로필`}
              className={styles.profileImage}
              onError={(e) => { e.target.src = '/assets/default-profile.png'; }}
            />
            <span className={styles.kakaoName}>{friend.kakaoName}</span>
            <button className={
              friend.relationship === 'FOLLOW' ? styles.followButton :
                friend.relationship === 'RECEIVE_REQUEST' ? styles.receiveRequestButton :
                  friend.relationship === 'MEMBER' ? styles.memberButton :
                    friend.relationship === 'REQUEST_FOLLOW' ? styles.requestFollowButton :
                      styles.nullButton
            } onClick={() => {
              if (friend.relationship === 'FOLLOW') {
                openUnfriendModal(friend);
              } else if (friend.relationship === 'RECEIVE_REQUEST') {
                openModal(friend);
              } else if (friend.relationship === 'MEMBER') {
                openInviteModal(friend);
              } else if (friend.relationship === null) {
                handleNullButtonClick();
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
            <h2>{selectedRequest.kakaoName}님의<br />지인 요청을 수락할까요?</h2>
            <button onClick={() => handleAccept(false)}>거절하기</button>
            <button onClick={() => handleAccept(true)}>수락하기</button>
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
            <h2>{selectedFriend.kakaoName}님과<br />지인관계를 해제할까요?</h2>
            <button onClick={closeUnfriendModal}>유지하기</button>
            <button onClick={handleUnfriend}>해제하기</button>
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
            <h2>{selectedFriend.kakaoName}님에게<br />지인을 요청할까요?</h2>
            <button onClick={closeInviteModal}>취소</button>
            <button onClick={handleInvite}>요청하기</button>
          </div>
        )}
      </Modal>
    </div>
  );
};

export default KakaoFriendsList;
