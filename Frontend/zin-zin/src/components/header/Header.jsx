import React from 'react';
import { Link, useLocation } from "react-router-dom";
import styles from './Header.module.css';

const Header = () => {
  return (
    <header className={styles.header}>
            <div className={styles.icon}>
     <Link to="/friend" className={styles.friends}  >
        <img src="./assets/friend-icon.png" />
      </Link>
            </div>
    </header>
  );
};

export default Header;
