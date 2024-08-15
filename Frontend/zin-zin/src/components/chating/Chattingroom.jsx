import React, { useEffect, useState, useRef } from "react";
import apiClient from '../../api/apiClient';
import { useLocation, useParams, useNavigate ,Link} from 'react-router-dom';
import styles from './Chattingroom.module.css';
import { Stomp } from "@stomp/stompjs"; 
import Modal from 'react-modal'; 

const Chattingroom = () => {
    const location = useLocation();
    const { name, nickname, profileImage, memberId, heartToggle } = location.state || {};
    const [roomType, setRoomType] = useState(location.state?.roomType || "MATE");
    const { roomId } = useParams(); // URL에서 roomId를 가져옴
    const [messages, setMessages] = useState([]);
    const [inputValue, setInputValue] = useState('');
    const [isHeart, setIsHeart] = useState(heartToggle); // 하트 상태를 관리하는 state 추가
    const stompClient = useRef(null);
    const [connected, setConnected] = useState(false);
    const navigate = useNavigate();
    const [isGooutDropdownOpen, setIsGooutDropdownOpen] = useState(false); // 신고 드롭다운 상태
    const messagesEndRef = useRef(null); // 메시지 리스트 끝에 대한 ref 추가
    const [modalIsOpen, setModalIsOpen] = useState(false); // 모달 상태 추가
    const [finalModalIsOpen, setFinalModalIsOpen] = useState(false); // 두 번째 모달 상태 추가
    const [mates, setMates] = useState([]); // mates 데이터를 저장할 상태 추가
    const [selectedMate, setSelectedMate] = useState(null); // 선택된 mate를 저장할 상태 추가
    const [confirmGooutModalIsOpen, setConfirmGooutModalIsOpen] = useState(false); // 채팅방 나가기 확인 모달 상태 추가



    const handleMateSelect = (mate) => {
        console.log(mate)
        setSelectedMate(mate);
    };

    const openConfirmGooutModal = () => {
        setConfirmGooutModalIsOpen(true);
    };
    
    const closeConfirmGooutModal = () => {
        setConfirmGooutModalIsOpen(false);
    };

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
            const response = await apiClient.delete(`/api/chatroom/${roomId}/exit`);
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

        const validateAccess = async () => {
            try {
                const response = await apiClient.get(`/api/chatroom/${roomId}/auth`);
                console.log(response.data)
                if (!response.data.canConnect) {
                    navigate('/chat');
                }
            } catch (error) {
                console.error('Error validating chatroom access:', error);
                navigate('/chat');
            }
        };

        validateAccess();

        console.log("Room Type:", roomType);
        console.log("Name:", name);
        console.log("Nickname:", nickname);
        console.log("Profile Image:", profileImage);
        console.log("memberId:", memberId);
        console.log("heartToggle:", heartToggle);
        
        const fetchMessages = async () => {
            try {
                const response = await apiClient.get(`/api/chatroom/${roomId}/messages`);
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
            const response = await apiClient.put(`/api/chatroom/${roomId}/heart`, {
                heart: newIsHeart
            });
            // setModalIsOpen(true);
            console.log(response.data)
            if (response.data.heart) {
                setMates(response.data.mates); // mates 데이터를 저장
                setModalIsOpen(true); // 모달 열기
                setRoomType("LOVE");
            }
            // 서버로부터의 응답을 확인하고 추가적인 처리가 필요하다면 여기에 작성
            console.log('Heart status updated:', response.data);
        } catch (error) {
            console.error('Error toggling heart status:', error);
        }
    };

    const handleNext = async () => {
        if (selectedMate) {
            try {
                await apiClient.post('/api/success-count', {
                    targetId: selectedMate.memberId,
                    roomId: roomId
                });

                // 선택된 mate의 정보를 사용하여 최종 모달을 열기
                setModalIsOpen(false); // 첫 번째 모달 닫기
                setFinalModalIsOpen(true); // 최종 모달 열기
            } catch (error) {
                console.error('Error sending success count:', error);
            }
        }
    };

    const handleButtonClick = () => {
        window.open("https://gift.kakao.com/home", "_blank");
    };

    return (
        <div className={styles.chatContainer}>  
        <Modal 
    isOpen={modalIsOpen}
    onRequestClose={() => setModalIsOpen(false)}
    contentLabel="Mates Information"
    className={styles.modal}
    overlayClassName={styles.overlay} 
>
    <p className={styles.firstText}>서로 호감을 표현했어요!</p>
    <p className={styles.secondText1}>인연을 맺어준 데에</p>
    <p className={styles.secondText2}>가장 큰 도움을 준 지인을 선택해 보세요</p>
    <div className={styles.matesList}>
        <div className={styles.imgconimgcon}>
        {mates.map((mate, index) => (
            <div 
                key={index} 
                className={`${styles.mateItem} ${selectedMate === mate ? styles.selected : ''}`} 
                onClick={() => handleMateSelect(mate)}
            >
                <img src={!mate.profileImage || mate.profileImage === 'default.jpg' ? `${process.env.REACT_APP_BASE_URL}/assets/default.png` : mate.profileImage} alt={mate.name} className={styles.picpicpic} />

                <p>{mate.name}</p>
            </div>
        ))}
        </div>
        <div className={styles.buttoncontainer}>
        <button 
            onClick={handleNext} 
            disabled={!selectedMate}  // selectedMate가 null일 때 비활성화
            className={!selectedMate ? styles.disabledButton : styles.activeButton} // 비활성화 시 스타일 추가
        >
            다음
        </button>
        <button onClick={() => setModalIsOpen(false) } className={styles.closebtn}>닫기</button>
        </div>
    </div>
</Modal>
 

            {/* 최종 모달 */}
            <Modal 
                isOpen={finalModalIsOpen}
                onRequestClose={() => setFinalModalIsOpen(false)}
                contentLabel="Final Mate Information"
                className={styles.modal}
                overlayClassName={styles.overlay} 
            >
                {selectedMate && (
                    <div>
                        <p className={styles.secondText3}>인연을 맺어준 지인에게</p>
                        <p className={styles.secondText4}> 감사함을 표현해보세요</p>
                        <img src={!selectedMate.profileImage || selectedMate.profileImage === "default.jpg" ? `${process.env.REACT_APP_BASE_URL}/assets/default.png` : selectedMate.profileImage} alt={selectedMate.name} className={styles.picpicpic2}/>
                        <p className={styles.secondText5}>{selectedMate.name}</p>
                        <div className={styles.buttoncontainer}>
                        <button onClick={handleButtonClick} className={styles.kakaoGiftButton}>
                    보답하기
                </button>
                        <button onClick={() => setFinalModalIsOpen(false) } className={styles.closebtn}>닫기</button>
                        </div>
                    </div>
                )}
            </Modal>  

            <Modal
    isOpen={confirmGooutModalIsOpen}
    onRequestClose={closeConfirmGooutModal}
    contentLabel="Confirm Go Out"
    className={styles.modal2}
    overlayClassName={styles.overlay}
>
    <p className={styles.textfirst}>채팅방 나가기</p>
    <div className={styles.textdummy}>
    <p className={styles.textsecond}>채팅방에서 나가기를 하면 상대방의 메세지를 더 이상 받을 수 없습니다. 또한 지금까지의 내용이 모두 사라지게 됩니다.</p>
    <p  className={styles.textsecond}>채팅방에서 나가시겠습니까?</p>
    </div>
    <div className={styles.modalButtons}>
        <button className={styles.nono} onClick={closeConfirmGooutModal}>취소</button>
        <button className={styles.okayokay} onClick={goout}>확인</button>
    </div>
</Modal>
        <div className={styles.topfix}>
        <div className={styles.toptop}>
        <div>
        <Link to="/chat" className={styles.iconicon}><i className="bi bi-chevron-left"/></Link>
        </div>
        <div className={styles.imagecontainer}>
            <img src={!profileImage || profileImage === 'default.jpg' ? `${process.env.REACT_APP_BASE_URL}/assets/default.png` : profileImage} alt="profile" />
            {roomType === 'MATE' ? <div>{name}</div> : <div>{nickname}</div>}
        </div>
                    <div className={styles.reportcontainer}>
                    <i className="bi bi-three-dots" onClick={toggleGooutDropdown}></i>
                    {isGooutDropdownOpen && (
                        <div className={styles.dropdownMenu}>
<button onClick={openConfirmGooutModal}>채팅방 나가기</button>
</div>
                    )}
                </div>
        </div>
        {roomType === "LIKE" && (
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
            {roomType === "LOVE" && (
                <div className={styles.centercenter}>
                    <img src={`${process.env.REACT_APP_BASE_URL}/assets/prettyHeart.png`} className={styles.hearth} alt="heart" />
                </div>
            )}
            <div className={styles.customDivider}></div>
        </div>
        <div className={styles.messageList} style={{ marginTop: marginTopValue }}>
        {messages.length > 0 ? (
                    messages.map((message, index) => (
                        <div>
                            {message.memberId === memberId ? (
                                <div key={index} className={styles.yourMessage}>
                                    <div>
                                        <img src={!profileImage || profileImage === "default.jpg" ? `${process.env.REACT_APP_BASE_URL}/assets/default.png` : profileImage} alt="profile" />
                                        <div className={styles.text}>{message.message}</div>
                                    </div>
                                </div>
                            ) : (
                                <div key={index} className={styles.myMessage}>
                                    <div>
                                        <div className={styles.text}>{message.message}</div>
                                    </div>
                                </div>
                            )}
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
