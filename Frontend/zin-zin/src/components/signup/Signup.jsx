import React from "react";
import CardContainer from './CardContainer';
import styles from './Signup.module.css';

const Signup = () => {
    return (
        <div className={styles.container}>
            <p className={styles.title}>내 정보</p>
            <p className={styles.text}>내 정보는 이후 수정이 불가합니다.</p>
            <CardContainer />
        </div>
    )
}

export default Signup