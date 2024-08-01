import React from 'react';
import styles from './Mypage.module.css';

const MyProfile = () => {
  // 더미 데이터
  const profile = {
    picture: 'https://via.placeholder.com/150', // 임시 이미지 URL
    name: '김윤지',
    username: '유명한 책',
    handle: 'escape_solo',
    kakaoFriends: 138,
    connections: 16
  };

  return (
    <div className={styles.profileContainer}>
      <div className={styles.profileTop}>
        <img src={profile.picture} alt={`${profile.name}의 프로필 사진`} className={styles.profileImage} />
        <div className={styles.profileDetails}>
          <h3 className={styles.username}>{profile.username}</h3>
          <p className={styles.name}>{profile.name} 님 @{profile.handle}</p>
          <div className={styles.profileStats}>
            <span>카톡 친구 {profile.kakaoFriends}</span>
            <span>나의 지인 {profile.connections}</span>
          </div>
        </div>
      </div>
      <div className={styles.buttonContainer}>
        <input type="text" className={styles.inputField}  />
        <button className={styles.linkButton}>초대 링크 생성</button>
      </div>
    </div>
  );
}

export default MyProfile;



// import { getUserProfile } from '../../API/api';
// const MyProfile = () => {
//   const [profile, setProfile] = useState(null);

//   useEffect(() => {
//     const loadData = async () => {
//       try {
//         const response = await getUserProfile();
//         setProfile(response.data);
//       } catch (error) {
//         console.error('Failed to load profile data:', error);
//       }
//     };

//     loadData();
//   }, []);

//   if (!profile) return <div>Loading...</div>;

//   return (
//     <div className={styles.profileContainer}>
//       <img src={profile.picture} alt={`${profile.name}의 프로필 사진`} className={styles.profileImage} />
//       <div className={styles.profileDetails}>
//         <h2>{profile.name}</h2>
//         <p>{profile.nickname} @ {profile.handle}</p>
//         <div className={styles.profileStats}>
//           <span>카톡 친구 {profile.kakaoFriends}</span>
//           <span>나의 지인 {profile.connections}</span>
//         </div>
//         <button className={styles.linkButton}>초대 링크 생성</button>
//       </div>
//     </div>
//   );
// }

// export default MyProfile;

