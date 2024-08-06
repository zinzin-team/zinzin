import React, { useEffect, useState } from "react";
import axios from 'axios';
import styles from './Chat.module.css';

const Chat = () => {
    const [chatRooms, setChatRooms] = useState([]);

    useEffect(() => {
        const fetchChatRooms = async () => {
            try {
                const token = sessionStorage.getItem('accesstoken');
                const response = await axios.get('/api/chat/rooms', {
                    headers: {
                        'accesstoken': token 
                    }
                });
                if (response.data.code === 200) {
                    setChatRooms(response.data.data);
                }
            } catch (error) {
                console.error('Error fetching chat rooms:', error);
            }
        };

        fetchChatRooms();
    }, []);

    return (
        <div className={styles.chatContainer}>
            {chatRooms && chatRooms.length > 0 ? (
                chatRooms.map(room => (
                    <div key={room.roomId} className={styles.chatRoom}>
                        <img src={room.otherMember.profileImage} alt="Profile" className={styles.profileImage} />
                        <div className={styles.roomInfo}>
                            {room.roomType === 'Like' ? (
                                <div>
                                    <p>{room.otherMember.nickname}</p>
                                    <img src="/assets/heartshape.png" alt="Heart" className={styles.icon} />
                                </div>
                            ) : (
                                <div>
                                    <p>{room.otherMember.name}</p>
                                    <img src="/assets/starshape.png" alt="Star" className={styles.icon} />
                                </div>
                            )}
                        </div>
                        <div className={styles.lastMessage}>
                            {room.lastMessage}
                        </div>
                    </div>
                ))
            ) : (
                <div className={styles.nochattingroom}>
                    <img src="/assets/nochattingroom.png" alt="Matching No Mode" />
                    <p className={styles.nochattingroomtext}>채팅방이 존재하지 않습니다</p>
                </div>
            )}
        </div>
    );
}

export default Chat;
