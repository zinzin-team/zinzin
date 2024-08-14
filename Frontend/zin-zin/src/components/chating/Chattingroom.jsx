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

    const marginTopValue = roomType === "MATE" ? "85px" : "135px";

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
            <img src={profileImage}/>
            {/* <img src="/assets/홍창기.png"/> */}
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
            <div className={styles.centercenter}>
                <div title="Like" className={styles.heartContainer}>
                    <input
                        id="Give-It-An-Id"
                        className={styles.checkbox}
                        type="checkbox"
                        checked={isHeart}
                        onChange={handleHeartToggle} 
                    />
                    <div className={styles.svgContainer}>
                        <svg xmlns="http://www.w3.org/2000/svg" className={styles.svgOutline} viewBox="0 0 24 24">
                            <path d="M17.5,1.917a6.4,6.4,0,0,0-5.5,3.3,6.4,6.4,0,0,0-5.5-3.3A6.8,6.8,0,0,0,0,8.967c0,4.547,4.786,9.513,8.8,12.88a4.974,4.974,0,0,0,6.4,0C19.214,18.48,24,13.514,24,8.967A6.8,6.8,0,0,0,17.5,1.917Zm-3.585,18.4a2.973,2.973,0,0,1-3.83,0C4.947,16.006,2,11.87,2,8.967a4.8,4.8,0,0,1,4.5-5.05A4.8,4.8,0,0,1,11,8.967a1,1,0,0,0,2,0,4.8,4.8,0,0,1,4.5-5.05A4.8,4.8,0,0,1,22,8.967C22,11.87,19.053,16.006,13.915,20.313Z"></path>
                        </svg>
                        <svg xmlns="http://www.w3.org/2000/svg" className={styles.svgFilled} viewBox="0 0 24 24">
                            <path d="M17.5,1.917a6.4,6.4,0,0,0-5.5,3.3,6.4,6.4,0,0,0-5.5-3.3A6.8,6.8,0,0,0,0,8.967c0,4.547,4.786,9.513,8.8,12.88a4.974,4.974,0,0,0,6.4,0C19.214,18.48,24,13.514,24,8.967A6.8,6.8,0,0,0,17.5,1.917Z"></path>
                        </svg>
                        <svg xmlns="http://www.w3.org/2000/svg" height="100" width="100" className={styles.svgCelebrate}>
                            <polygon points="10,10 20,20"></polygon>
                            <polygon points="10,50 20,50"></polygon>
                            <polygon points="20,80 30,70"></polygon>
                            <polygon points="90,10 80,20"></polygon>
                            <polygon points="90,50 80,50"></polygon>
                            <polygon points="80,80 70,70"></polygon>
                        </svg>
                    </div>
                </div>
            </div>
            )}
            <div className={styles.customDivider}></div>
        </div>
        <div className={styles.messageList} style={{ marginTop: marginTopValue }}>
        {messages.length > 0 ? (
                    messages.map((message, index) => (
                        <div key={index} className={styles.message}>
                            <div>
                                {message.memberId === memberId ? (
                                    <div className={styles.itisyou}>
                                        <img src={profileImage}/>
                                        {/* <img src="/assets/홍창기.png"/> */}
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
