import React from "react";
import styles from './List.module.css';

const count = 0;

const Like = () => {
    return (
        <div className={styles.list}>
            <div>
            지인 매칭 현황
            </div>
            <div>
                총 {count}커플 탄생
            </div>
        </div>
    )
}

export default Like