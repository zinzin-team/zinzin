import React, { useEffect, useState, useRef } from "react";
import axios from 'axios';
import { useLocation, useParams, useNavigate ,Link} from 'react-router-dom';
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
    const [isGooutDropdownOpen, setIsGooutDropdownOpen] = useState(false); // 신고 드롭다운 상태
    const messagesEndRef = useRef(null); // 메시지 리스트 끝에 대한 ref 추가

    // 입력 필드에 변화가 있을 때마다 inputValue를 업데이트
    const handleInputChange = (event) => {
        setInputValue(event.target.value);
    };

    // 웹소켓 연결 설정
    const connect = () => {
        // const socket = new WebSocket("ws://localhost:8080/api/ws");
        const socket = new WebSocket("https://zin-zin.site/api/ws");
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

    const toggleGooutDropdown = () => {
        setIsGooutDropdownOpen(!isGooutDropdownOpen);
    };

    const goout = async () => {
        try {
            const token = sessionStorage.getItem('accessToken');
            const response = await axios.delete(`/api/chatroom/${roomId}/exit`, {
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

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
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
                const response = await axios.get(`/api/chatroom/${roomId}/messages`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }    
                });
                const reversedMessages = response.data.reverse(); // 배열을 역순으로 만듦
                console.log(reversedMessages);
                setMessages(reversedMessages); // 역순으로 된 배열을 저장
            } catch (error) {
                console.error('Error fetching messages:', error);
            }
        };

        connect();
        fetchMessages();
        scrollToBottom();

        return () => disconnect();
    }, [roomId, roomType, name, nickname, profileImage, memberId, heartToggle]);

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    const sendMessage = () => {
        const myMemberId = sessionStorage.getItem('memberId');
        if (connected && inputValue) {
            const body = {
                memberId: myMemberId,
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
            const response = await axios.put(`/api/chatroom/${roomId}/heart`, {
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

    console.log(profileImage)

    return (
        <div className={styles.chatContainer}>  
        <div className={styles.topfix}>
        <div className={styles.toptop}>
        <div>
        <Link to="/chat" className={styles.iconicon}><i className="bi bi-chevron-left"/></Link>
        </div>
        <div className={styles.imagecontainer}>
            {/* <img src={profileImage}/> */}
            <img src="/assets/홍창기.png"/>
            {roomType === 'MATE' ? <div>{name}</div> : <div>{nickname}</div>}
        </div>
                    <div className={styles.reportcontainer}>
                    <i className="bi bi-three-dots" onClick={toggleGooutDropdown}></i>
                    {isGooutDropdownOpen && (
                        <div className={styles.dropdownMenu}>
                            <button onClick={goout}>채팅방 나가기</button>
                        </div>
                    )}
                </div>
        </div>
                    {roomType !== "MATE" && (
                        <div className={styles.heartheart}>
                            <input 
                                type="checkbox"  
                                id="heart-check" 
                                checked={isHeart} // 체크박스 상태를 isHeart 상태와 연동
                                onChange={handleHeartToggle} // 체크박스 상태 변화시 handleHeartToggle 호출
                            />
                            <label htmlFor="heart-check">하트뿅</label>
                        </div>
                    )}
            <div className={styles.customDivider}></div>
        </div>
            <div className={styles.messageList}>
                {messages.length > 0 ? (
                    messages.map((message, index) => (
                        <div key={index} className={styles.message}>
                            <div>
                                {message.memberId === memberId ? (
                                    <div className={styles.itisyou}>
                                        {/* <img src={profileImage}/> */}
                                        <img src="/assets/홍창기.png"/>
                                        <div className={styles.text}>{message.message}</div>
                                    </div> 
                                ) : (
                                    <div className={styles.itisme}>
                                        <div className={styles.text}>{message.message}</div>
                                    </div>
                                    
                                )}
                            </div>
                        </div>
                    ))
                ) : (
                    <div className={styles.noMessages}>새로운 채팅을 시작하세요</div>
                )}
                <div ref={messagesEndRef} />
            </div>
            <div className={styles.chatchat}>
                <input
                    type="text"
                    value={inputValue}
                    onChange={handleInputChange}    
                    onKeyPress={handleKeyPress}    
                    placeholder="메세지를 입력해주세요"
                />
                <button onClick={sendMessage}>전송</button> 
            </div>
        </div>
    );
}

export default Chattingroom;
