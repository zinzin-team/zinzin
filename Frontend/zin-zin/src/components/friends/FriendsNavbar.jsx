import React from 'react';
import { NavLink } from 'react-router-dom';
import styles from './FriendsNavbar.module.css';

const FriendsNavbar = () => {
  return (
    <div className={styles.tabNavigation}>
      <NavLink
        to="/friends"
        end
        className={({ isActive }) => isActive ? `${styles.tabButton} ${styles.active}` : styles.tabButton}
      >
        카톡 친구
      </NavLink>
      <NavLink
        to="/friends/z-in"
        className={({ isActive }) => isActive ? `${styles.tabButton} ${styles.active}` : styles.tabButton}
      >
        지인
      </NavLink>
      <NavLink
        to="/friends/search"
        className={({ isActive }) => isActive ? `${styles.tabButton} ${styles.active}` : styles.tabButton}
      >
        친구 검색
      </NavLink>
    </div>
  );
};

export default FriendsNavbar;
