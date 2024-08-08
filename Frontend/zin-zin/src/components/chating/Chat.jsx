import React, { useEffect, useState } from "react";
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';
import styles from './Chat.module.css';

const Chat = () => {
    const [chatRooms, setChatRooms] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchChatRooms = async () => {
            try {
                const token = sessionStorage.getItem('accesstoken');
                const response = await axios.get('/api/chatRoom', {
                    headers: { 'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzNjQyMzA1OCIsInJvbGUiOiJVU0VSIiwiZXhwIjo2MDAwMDAxNzIyOTMxMjY5LCJpYXQiOjE3MjI5MzEyNjksIm1lbWJlcklkIjo1fQ.2MzZDZcIucUDh0J6x1CjjKajTU_kOI47ijEmKY5AUhU'}
                });
                console.log(response.data)
                if (response.data) {
                    setChatRooms(response.data);
                }
            } catch (error) {
                console.error('Error fetching chat rooms:', error);
            }
        };

        fetchChatRooms();
        console.log(chatRooms)
    }, []);

    const handleRoomClick = (roomId) => {
        navigate(`/chat/${roomId}`);
    };

    return (
        <div className={styles.chatContainer}>
            {chatRooms && chatRooms.length > 0 ? (
                chatRooms.map(room => (
                    <div key={room.roomId} className={styles.chatRoom} 
                    onClick={() => handleRoomClick(room.roomId)}>
                        <img src="/assets/홍창기.png" alt="Profile" className={styles.profileImage} />
                        <div className={styles.roomInfo}>
                            {room.roomType === 'Like' ? (
                                <div>
                                    <p>{room.otherMember.nickname}</p>
                                    <img
                                        src="/assets/heartshape.png"
                                        alt="Heart"
                                        className={styles.icon}
                                    />
                                </div>
                            ) : (
                                <div>
                                    <p>{room.otherMember.name}</p>
                                    <img
                                        src="/assets/starshape.png"
                                        alt="Star"
                                        className={styles.icon}
                                    />
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
