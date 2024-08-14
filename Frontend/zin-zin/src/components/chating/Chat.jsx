import React, { useEffect, useState } from "react";
import apiClient from '../../api/apiClient';
import { Link, useNavigate } from 'react-router-dom';
import styles from './Chat.module.css';

const Chat = () => {
    const [chatRooms, setChatRooms] = useState([]);
    const [loading, setLoading] = useState(true); // 로딩 상태 추가
    const navigate = useNavigate();

    useEffect(() => {
        const fetchChatRooms = async () => {
            try {
                const response = await apiClient.get('/api/chatroom');
                console.log(response.data)
                if (response.data) {
                    setChatRooms(response.data);
                }
            } catch (error) {
                console.error('Error fetching chat rooms:', error);
            } finally {
                setLoading(false); // 데이터 로딩 완료 후 로딩 상태를 false로 설정
            }
        };

        fetchChatRooms();
        console.log(chatRooms)
    }, []);

    const handleRoomClick = (room) => {
        navigate(`/chat/${room.roomId}`, {
            state: {
                roomType: room.roomType,
                name: room.otherMember.name,
                nickname: room.otherMember.nickname,
                profileImage: room.otherMember.profileImage,
                memberId:  room.otherMember.memberId,
                heartToggle: room.heartToggle
            }
        });
    };

    if (loading) {
        return (
            <div className={styles.spinner}>
                <div className={`${styles.heart} ${styles.heart1}`}></div>
                <div className={`${styles.heart} ${styles.heart2}`}></div>
                <div className={`${styles.heart} ${styles.heart3}`}></div>
                <div className={styles.loadingtext}>
                    Loading
                    <span className={styles.dot1}>.</span>
                    <span className={styles.dot2}>.</span>
                    <span className={styles.dot3}>.</span>
                </div>
            </div>
        );
    }

    return (
        <div className={styles.allcontainer}>
            <div className={styles.topchatContainer}>
                {chatRooms && chatRooms.length > 0 ? 
                (
                    chatRooms.map(room => (
                        <div key={room.roomId} className={styles.chatRoom}>
                            <img src={room.otherMember.profileImage} alt="Profile" className={styles.topprofileImage} onClick={() => handleRoomClick(room)}/>
                            <div className={styles.roomInfo}>
                                {room.roomType === 'MATE' ? (
                                    <div className={styles.toptalk}>
                                        <img
                                            src={`${process.env.REACT_APP_BASE_URL}/assets/starshape.png`}
                                            alt="Star"
                                            className={styles.icon}
                                        />
                                        <p>{room.otherMember.name}</p>
                                    </div>
                                ) : (
                                    <div className={styles.toptalk}>
                                        <img
                                            src={`${process.env.REACT_APP_BASE_URL}/assets/heartshape.png`}
                                            alt="Heart"
                                            className={styles.icon}
                                        />
                                        <p>{room.otherMember.nickname}</p>
                                    </div>
                                )}
                            </div>
                        </div>
                    ))
                )
                : (
                    <div></div>
                )}
            </div>
            {chatRooms && chatRooms.length > 0 ?
            <div className={styles.titlebox}> <p className={styles.titletitle}>채팅</p></div> : <div></div>}
            <div className={styles.bottomchatContainer}>
                {chatRooms && chatRooms.length > 0 ? (
                    chatRooms.map(room => (
                        <div key={room.roomId} className={styles.chatRoombottom} onClick={() => handleRoomClick(room)}>
                            <img src={room.otherMember.profileImage} alt={room.otherMember.profileImage} className={styles.profileImage} />
                            <div className={styles.roomInfo}>
                                {room.roomType === 'MATE' ? (
                                    <div>
                                        <img
                                            src={`${process.env.REACT_APP_BASE_URL}/assets/starshape.png`}
                                            alt="Star"
                                            className={styles.icon2}
                                        />
                                    </div>
                                ) : (
                                    <div>
                                        <img
                                            src={`${process.env.REACT_APP_BASE_URL}/assets/heartshape.png`}
                                            alt="Heart"
                                            className={styles.icon2}
                                        />
                                    </div>
                                )}
                            </div>
                            <div className={styles.texttext}>
                                {room.roomType === 'MATE' ? (
                                    <p className={styles.namename}>{room.otherMember.name}</p>
                                ) : (
                                    <p className={styles.namename}>{room.otherMember.nickname}</p>
                                )}
                                <div className={styles.lastMessage}>
                                    {room.lastMessage === null ? <p className={styles.startstart}>채팅을 시작해보세요</p> : room.lastMessage}
                                </div>
                            </div>
                        </div>
                    ))
                ) : (
                    <div className={styles.nochattingroom}>
                        <img src={`${process.env.REACT_APP_BASE_URL}/assets/nochattingroom.png`} alt="Matching No Mode" />
                        <p className={styles.nochattingroomtext}>채팅방이 존재하지 않습니다</p>
                    </div>
                )}
            </div>
        </div>
    );
}

export default Chat;
