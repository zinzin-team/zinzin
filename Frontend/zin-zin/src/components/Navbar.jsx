import React from 'react';
import { BrowserRouter, Route, Routes, useLocation } from "react-router-dom";
import styles from './Navbar.module.css';

const Navbar = () => {
  const location = useLocation();

  return (
    <header className={styles.nav}>
      <a href="/" className={location.pathname === "/" ? styles.selected : styles.notselected}>
        <img src="/assets/image 16.svg" />
      </a>
      <a href="/Like" className={location.pathname === "/Like" ? styles.selected : styles.notselected}>
        <img src="/assets/Union.svg" />
      </a>
      <a href="/Chat" className={location.pathname === "/Chat" ? styles.selected : styles.notselected}>
        <img src="/assets/homepage 4.svg" />
      </a>
      <a href="/Mypage" className={location.pathname === "/Mypage" ? styles.selected : styles.notselected}>
        <img src="/assets/homepage 5.svg" />
      </a>
    </header>
  );
};

export default Navbar;
