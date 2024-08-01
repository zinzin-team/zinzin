import React from 'react';
import { Link, useLocation } from "react-router-dom";
import styles from './Navbar.module.css';

const Navbar = () => {
  const location = useLocation();

  return (
    <header className={styles.nav}>
      <Link to="/" className={location.pathname === "/" || location.pathname === "/create-card" ? styles.selected : styles.notselected}>
        <img src={location.pathname === "/" || location.pathname === "/create-card" ? "/assets/match-after.png" : "/assets/match-before.png"} />
      </Link>
      <Link to="/like" className={location.pathname === "/like" ? styles.selected : styles.notselected}>
        <img src={location.pathname === "/like" ? "/assets/list-after.png" : "/assets/list-before.png"} />
      </Link>
      <Link to="/chat" className={location.pathname === "/chat" ? styles.selected : styles.notselected}>
        <img src={location.pathname === "/chat" ? "/assets/chat-after.png" : "/assets/chat-before.png"} />
      </Link>
      <Link to="/mypage" className={location.pathname === "/mypage" || location.pathname === "/update-card" ? styles.selected : styles.notselected}>
        <img src={location.pathname === "/mypage" || location.pathname === "/update-card" ? "/assets/mypage-after.png" : "/assets/mypage-before.png"} />
      </Link>
    </header>
  );
};

export default Navbar;
