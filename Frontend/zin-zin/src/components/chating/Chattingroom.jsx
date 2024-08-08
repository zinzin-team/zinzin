import React, { useEffect, useState, useRef } from "react";
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import styles from './Chattingroom.module.css';
import { Stomp } from "@stomp/stompjs"; 

const Chattingroom = () => {
    const { roomId } = useParams(); // URL에서 roomId를 가져옴
    const [messages, setMessages] = useState([]);
    const [inputValue, setInputValue] = useState('');
    const stompClient = useRef(null);
    const [connected, setConnected] = useState(false);
    const navigate = useNavigate();

    // 입력 필드에 변화가 있을 때마다 inputValue를 업데이트
    const handleInputChange = (event) => {
        setInputValue(event.target.value);
    };

    // 웹소켓 연결 설정
    const connect = () => {
        const socket = new WebSocket("ws://localhost:8080/api/ws");
        stompClient.current = Stomp.over(socket);
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
            const token = sessionStorage.getItem('accesstoken');
            const response = await axios.delete(`/api/chatRoom/${roomId}/exit`, {
                headers: { 'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzNjQyMzA1OCIsInJvbGUiOiJVU0VSIiwiZXhwIjo2MDAwMDAxNzIyOTMxMjY5LCJpYXQiOjE3MjI5MzEyNjksIm1lbWJlcklkIjo1fQ.2MzZDZcIucUDh0J6x1CjjKajTU_kOI47ijEmKY5AUhU'}
            });
            navigate('/chat');
            console.log(response.data)
        } catch (error) {
            console.error('Error exiting chat room:', error);
        }
    };

    useEffect(() => {
        const fetchMessages = async () => {
            try {
                const token = sessionStorage.getItem('accesstoken');
                const response = await axios.get(`/api/chatRoom/${roomId}/messages`, {
                    headers: { 'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzNjQyMzA1OCIsInJvbGUiOiJVU0VSIiwiZXhwIjo2MDAwMDAxNzIyOTMxMjY5LCJpYXQiOjE3MjI5MzEyNjksIm1lbWJlcklkIjo1fQ.2MzZDZcIucUDh0J6x1CjjKajTU_kOI47ijEmKY5AUhU'}
                });
                console.log(response.data);
                setMessages(response.data);
            } catch (error) {
                console.error('Error fetching messages:', error);
            }
        };

        connect();
        fetchMessages();
        return () => disconnect();
    }, [roomId]);

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

    return (
        <div className={styles.chatContainer}>
            {messages.length > 0 ? (
                messages.map((message, index) => (
                    <div key={index} className={styles.message}>
                        <div className={styles.sender}>{message.senderName}</div>
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
