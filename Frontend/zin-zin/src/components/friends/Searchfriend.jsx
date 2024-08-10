import React, { useState } from 'react';
import axios from 'axios';
import styles from './Searchfriend.module.css';

const Searchfriend = () => {
  const [searchId, setSearchId] = useState('');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSearch = async () => {
    setLoading(true);
    setError('');
    setResult(null);

    const accessToken = sessionStorage.getItem('accessToken');
    if (!accessToken) {
      setError('로그인이 필요합니다.');
      setLoading(false);
      return;
    }

    try {
      // /와 \ 문자를 제거
      const sanitizedSearchId = searchId.replace(/[\/\\]/g, '');
      
      if (!sanitizedSearchId) {
        setError('유효한 검색어를 입력해주세요.');
        setLoading(false);
        return;
      }

      const response = await axios.get(`/api/search/${encodeURIComponent(sanitizedSearchId)}`, {
        headers: {
          'Authorization': `Bearer ${accessToken}`,
          'Content-Type': 'application/json'
        }
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

  return (
    <div className={styles.container}>
      <h2>아이디로 친구를 찾을 수 있어요!</h2>
      <div className={styles.searchContainer}>
        <input
          type="text"
          placeholder="검색"
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
            src={result.profileImage ? result.profileImage : 'default-profile.png'}
            alt={`${result.name} 프로필`}
            className={styles.profileImage}
          />
          <span className={styles.kakaoName}>{result.name}</span>
          <button className={styles.addButton}>지인 요청 +</button>
        </div>
      )}
    </div>
  );
};

export default Searchfriend;
