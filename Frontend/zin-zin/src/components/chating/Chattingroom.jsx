import React, { useEffect, useState, useRef } from "react";
import axios from 'axios';
import { useLocation, useParams, useNavigate } from 'react-router-dom';
import styles from './Chattingroom.module.css';
import { Stomp } from "@stomp/stompjs"; 

const Chattingroom = () => {
    const location = useLocation();

    const { roomType, name, nickname, profileImage, memberId, heartToggle } = location.state || {};
    const { roomId } = useParams(); // URL에서 roomId를 가져옴
    const [messages, setMessages] = useState([]);
    const [inputValue, setInputValue] = useState('');
    const [isHeart, setIsHeart] = useState(heartToggle); // 하트 상태를 관리하는 state 추가
    const stompClient = useRef(null);
    const [connected, setConnected] = useState(false);
    const navigate = useNavigate();

    // 입력 필드에 변화가 있을 때마다 inputValue를 업데이트
    const handleInputChange = (event) => {
        setInputValue(event.target.value);
    };

    // 웹소켓 연결 설정
    const connect = () => {
        const socket = new WebSocket("wss://zin-zin.site:443/api/ws");
        // stompClient.current = Stomp.over(socket);
        stompClient.current = Stomp.client("wss://zin-zin.site/api/ws");
        stompClient.current.connect({}, () => {
            setConnected(true);
            stompClient.current.subscribe(`/queue/chatroom/${roomId}`, (message) => {
                const newMessage = JSON.parse(message.body);
                setMessages((prevMessages) => [...prevMessages, newMessage]);
            });
        }, (error) => {
            console.error('Connection error', error);
        });
    };

    // 웹소켓 연결 해제
    const disconnect = () => {
        if (stompClient.current) {
            stompClient.current.disconnect(() => {
                setConnected(false);
                console.log("Disconnected");
            });
        }
    };

    const handleKeyPress = (event) => {
        if (event.key === 'Enter') {
            sendMessage();
        }
    };

    const goout = async () => {
        try {
            const token = sessionStorage.getItem('accessToken');
            const response = await axios.delete(`/api/chatRoom/${roomId}/exit`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }    
            });
            navigate('/chat');
            console.log(response.data)
        } catch (error) {
            console.error('Error exiting chat room:', error);
        }
    };

    useEffect(() => {
        console.log("Room Type:", roomType);
        console.log("Name:", name);
        console.log("Nickname:", nickname);
        console.log("Profile Image:", profileImage);
        console.log("memberId:", memberId);
        console.log("heartToggle:", heartToggle);
        
        const fetchMessages = async () => {
            try {
                const token = sessionStorage.getItem('accessToken');
                const response = await axios.get(`/api/chatRoom/${roomId}/messages`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }    
                });
                console.log(response.data);
                setMessages(response.data);
            } catch (error) {
                console.error('Error fetching messages:', error);
            }
        };

        const fetchHeartStatus = async () => {
            try {
                const token = sessionStorage.getItem('accessToken');
                const response = await axios.get(`/api/chatRoom/${roomId}/heart`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                setIsHeart(response.data.isHeart); 
            } catch (error) {
                console.error('Error fetching heart status:', error);
            }
        };

        connect();
        fetchMessages();
        fetchHeartStatus();

        return () => disconnect();
    }, [roomId, roomType, name, nickname, profileImage, memberId, heartToggle]);

    const sendMessage = () => {
        if (connected && inputValue) {
            const body = {
                memberId: 9,
                message: inputValue
            };
            stompClient.current.send(`/app/${roomId}`, {}, JSON.stringify(body));
            setInputValue('');
        } else {
            console.error('STOMP client is not connected');
        }
    };

    const handleHeartToggle = async (event) => {
        const newIsHeart = event.target.checked; // 체크박스의 새로운 상태를 가져옴
        setIsHeart(newIsHeart); // UI 업데이트

        try {
            const token = sessionStorage.getItem('accessToken');
            const response = await axios.put(`/api/chatRoom/${roomId}/heart`, {
                isHeart: newIsHeart
            }, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            // 서버로부터의 응답을 확인하고 추가적인 처리가 필요하다면 여기에 작성
            console.log('Heart status updated:', response.data);
        } catch (error) {
            console.error('Error toggling heart status:', error);
        }
    };

    return (
        <div className={styles.chatContainer}>  
            {roomType !== "Mate" && (
                <>
                    <input 
                        type="checkbox"  
                        id="heart-check" 
                        checked={isHeart} // 체크박스 상태를 isHeart 상태와 연동
                        onChange={handleHeartToggle} // 체크박스 상태 변화시 handleHeartToggle 호출
                    />
                    <label htmlFor="heart-check">하트뿅</label>
                </>
            )}
            {messages.length > 0 ? (
                messages.map((message, index) => (
                    <div key={index} className={styles.message}>
                        <div>
                            {message.memberId === memberId ? (
                                roomType === "Mate" ? name : nickname
                            ) : (
                                <div>내가 한 채팅입니다.</div>
                            )}
                        </div>
                        <div className={styles.text}>{message.message}</div>
                    </div>
                ))
            ) : (
                <div className={styles.noMessages}>새로운 채팅을 시작하세요</div>
            )}
            <div>
                <input
                    type="text"
                    value={inputValue}
                    onChange={handleInputChange}    
                    onKeyPress={handleKeyPress}    
                />
                <button onClick={sendMessage}>입력</button> 
            </div>
            <button onClick={goout}>채팅 나가기</button> 
        </div>
    );
}

export default Chattingroom;
