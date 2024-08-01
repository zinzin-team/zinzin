import React from "react";
import MyProfile from './MyProfile';
import MyCard from './Mycard';
import MypageList from './MypageList';
import styles from './Mypage.module.css';
  
const Mypage = () => {
  return (
    <div className={styles.mypageContainer}>
      <MyProfile />
      <MyCard />
      <MypageList />
    </div>
  )
}

export default Mypage;
