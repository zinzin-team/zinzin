import React from "react";
import styles from './Chat.module.css';

const Chat = () => {
    return (
        <div className={styles.chat}>
            채팅 탭
            <br/>
            <input type="checkbox"  id="heart-check"/>
            <label for="heart-check"></label>
        </div>
    )
}

export default Chat