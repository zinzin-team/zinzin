import React from 'react';
import { Link, useLocation } from "react-router-dom";
import styles from './Header.module.css';

const Header = () => {
  return (
    <header className={styles.header}>
         <Link to="/" className={styles.icon2} >
              <img className={styles.logo} src={`${process.env.REACT_APP_BASE_URL}/assets/logo.png`} />
      </Link>
            <div className={styles.icon}>
     <Link to="/friends" className={styles.friends}  >
        <img src={`${process.env.REACT_APP_BASE_URL}/assets/friend-icon.png`} />
      </Link>
            </div>
    </header>
  );
};

export default Header;
