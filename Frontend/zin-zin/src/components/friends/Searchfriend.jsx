import React, { useState } from 'react';
import axios from 'axios';
import styles from './Searchfriend.module.css';

const Searchfriend = () => {
  const [searchId, setSearchId] = useState('');
  const [result, setResult] = useState(null);
  const [relationship, setRelationship] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [isMe, setIsMe] = useState(false);

  const handleSearch = async () => {
    setLoading(true);
    setError('');
    setResult(null);
    setRelationship(null);
    setIsMe(false);

    const accessToken = sessionStorage.getItem('accessToken');
    const userId = sessionStorage.getItem('userId'); // Assuming userId is stored in session

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
        }
      });

      const data = response.data;

      if (data.success) {
        setResult(data.member);

        // 자신을 검색한 경우
        if (data.member.id === parseInt(userId, 10)) {
          setIsMe(true);
        } else {
          // 두 번째 API 호출: 나의 친구 목록 가져오기
          const friendsResponse = await axios.get('/api/mates/social-friends', {
            headers: {
              'Authorization': `Bearer ${accessToken}`,
              'Content-Type': 'application/json'
            }
          });

          const friends = friendsResponse.data;
          const relationshipWithSearchedUser = friends.find(
            (friend) => friend.memberId === data.member.id
          );

          setRelationship(relationshipWithSearchedUser);
        }
      } else {
        setError('존재하지 않는 아이디입니다.');
      }
    } catch (err) {
      setError('검색 중 오류가 발생했습니다.');
    }
    setLoading(false);
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
      return (
        <button className={styles.nullButton}>
          초대 보내기
        </button>
      );
    }

    switch (relationship.relationship) {
      case 'FOLLOW':
        return (
          <button className={styles.followButton}>
            나의 지인
          </button>
        );
      case 'REQUEST_FOLLOW':
        return (
          <button className={styles.requestFollowButton}>
            요청 대기중
          </button>
        );
      case 'RECEIVE_REQUEST':
        return (
          <button className={styles.receiveRequestButton}>
            요청 수락 +
          </button>
        );
      case 'MEMBER':
      default:
        return (
          <button className={styles.memberButton}>
            지인 요청 +
          </button>
        );
    }
  };

  return (
    <div className={styles.container}>
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
    </div>
  );
};

export default Searchfriend;
