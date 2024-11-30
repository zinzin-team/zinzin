import React from 'react';
import { Link, useLocation } from "react-router-dom";
import styles from './Navbar.module.css';

const Navbar = () => {
  const location = useLocation();

  return (
    <header className={styles.nav}>
      <Link to="/" className={location.pathname === "/" || location.pathname === "/create-card" ? styles.selected : styles.notselected}>
        <img src={location.pathname === "/" || location.pathname === "/create-card" ? `${process.env.REACT_APP_BASE_URL}/assets/match-after.png` : `${process.env.REACT_APP_BASE_URL}/assets/match-before.png`} />
      </Link>
      <Link to="/like" className={location.pathname === "/like" ? styles.selected : styles.notselected}>
        <img src={location.pathname === "/like" ? `${process.env.REACT_APP_BASE_URL}/assets/list-after.png` : `${process.env.REACT_APP_BASE_URL}/assets/list-before.png`} />
      </Link>
      <Link to="/chat" className={location.pathname === "/chat" ? styles.selected : styles.notselected}>
        <img src={location.pathname === "/chat" ? `${process.env.REACT_APP_BASE_URL}/assets/chat-after.png` : `${process.env.REACT_APP_BASE_URL}/assets/chat-before.png`} className={styles.navMarginButtom} />
      </Link>
      <Link to="/mypage" className={location.pathname === "/mypage" || location.pathname === "/update-card" ? styles.selected : styles.notselected}>
        <img src={location.pathname === "/mypage" || location.pathname === "/update-card" ? `${process.env.REACT_APP_BASE_URL}/assets/mypage-after.png` : `${process.env.REACT_APP_BASE_URL}/assets/mypage-before.png`} className={styles.navMarginButtom} />
      </Link>
    </header>
  );
};

export default Navbar;
