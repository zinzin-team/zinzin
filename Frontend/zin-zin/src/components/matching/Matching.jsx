import styles from './Matching.module.css';
import React, { useEffect, useState } from "react";
import apiClient from '../../api/apiClient';
import { useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import confetti from 'canvas-confetti';
import ReactCardFlip from 'react-card-flip';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

Modal.setAppElement('#root'); // 이 설정은 접근성을 위해 필요합니다.

const Matching = () => {
    const [cardData, setCardData] = useState(null);
    const [matchingCardData, setMatchingCardData] = useState(null);
    const [currentIndex, setCurrentIndex] = useState(0);
    const [currentImageIndex, setCurrentImageIndex] = useState(0);
    const [isFront, setIsFront] = useState(true);
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const [modalIsOpen2, setModalIsOpen2] = useState(false);
    const [mates, setMates] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [isReportDropdownOpen, setIsReportDropdownOpen] = useState(false); // 신고 드롭다운 상태
    const [isMatesDropdownOpen, setIsMatesDropdownOpen] = useState(false); // 지인 드롭다운 상태
    const navigate = useNavigate();
    const [roomData, setRoomData] = useState(null);  // 추가된 상태


    const testtest = () => {
        firework();
        setModalIsOpen(true);
    };

    const startChatWithMate = async (mateId) => {
        try {
            const response = await apiClient.post('/api/chatroom/create', {
                roomType: "MATE",
                targetId: mateId,
            });

            const room = response.data;
            if (room && room.roomId) {
                navigate(`/chat/${room.roomId}`, {
                    state: {
                        roomType: room.roomType,
                        name: room.otherMember.name,
                        nickname: room.otherMember.nickname,
                        profileImage: room.otherMember.profileImage,
                        memberId: room.otherMember.memberId,
                        heartToggle: room.heartToggle
                    }
                });
            } else {
                console.error('채팅방 생성에 실패했습니다.');
            }
        } catch (error) {
            console.error('채팅방 생성 중 오류 발생:', error);
        }
    };


    const firework = () => {
        var duration = 20 * 150;
        var animationEnd = Date.now() + duration;
        var defaults = { startVelocity: 25, spread: 360, ticks: 50, zIndex: 2000 };

        function randomInRange(min, max) {
            return Math.random() * (max - min) + min;
        }

        var interval = setInterval(function () {
            var timeLeft = animationEnd - Date.now();

            if (timeLeft <= 0) {
                return clearInterval(interval);
            }

            var particleCount = 150 * (timeLeft / duration);
            confetti(
                Object.assign({}, defaults, {
                    particleCount,
                    origin: { x: randomInRange(0.1, 0.3), y: Math.random() - 0.2 }
                })
            );
            confetti(
                Object.assign({}, defaults, {
                    particleCount,
                    origin: { x: randomInRange(0.7, 0.9), y: Math.random() - 0.2 }
                })
            );
        }, 250);
    };

    const fetchMatchingCards = async () => {
        try {
            const response = await apiClient.get('/api/matchings');
            if (response.data && Array.isArray(response.data.matchings)) {
                console.log(response.data)
                setMatchingCardData(response.data.matchings);
            } else {
                console.error('매칭 카드 데이터를 가져오는 데 실패했습니다.');
            }
        } catch (error) {
            console.error('매칭 카드 데이터를 가져오는 중 오류 발생:', error);
        } finally {
            setIsLoading(false); // 데이터 로드 완료 후 로딩 상태 해제
        }
    };

    const navigateToChat = () => {
        if (roomData) {
            navigate(`/chat/${roomData.roomId}`, {
                state: {
                    roomType: roomData.roomType,
                    name: roomData.otherMember.name,
                    nickname: roomData.otherMember.nickname,
                    profileImage: roomData.otherMember.profileImage,
                    memberId: roomData.otherMember.memberId,
                    heartToggle: roomData.heartToggle,
                }
            });
        }
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await apiClient.get('/api/cards');
                if (response.data) {
                    console.log(response.data)
                    const { cardId, tags, info, images } = response.data;
                    setCardData({ cardId, tags, info, images });
                    sessionStorage.setItem('cardData', JSON.stringify({ cardId, tags, info, images }));
                } else {
                    console.error('카드 데이터를 가져오는 데 실패했습니다.');
                }
            } catch (error) {
                console.error('데이터를 가져오는 중 오류 발생:', error);
            } finally {
                setIsLoading(false); // 데이터 로드 완료 후 로딩 상태 해제
            }
        };

        const storedMatchingMode = sessionStorage.getItem('matchingMode');
        // const storedMatchingMode = true;

        if (storedMatchingMode) {
            const fetchDataAndSetCardData = async () => {
                await fetchData();
                const storedCardData = sessionStorage.getItem('cardData');
                if (storedCardData) {
                    setCardData(JSON.parse(storedCardData));
                    fetchMatchingCards();
                }
            };
            fetchDataAndSetCardData();
        }
    }, []);

    useEffect(() => {
        if (matchingCardData && matchingCardData.length > 0) {
            const firstUncheckedIndex = matchingCardData.findIndex(card => !card.checked);
            if (firstUncheckedIndex !== -1) {
                setCurrentIndex(firstUncheckedIndex);
            }
        }
    }, [matchingCardData]);

    

    const handleCreateCard = () => {
        navigate('/create-card');
    };

    const handleLikeDislike = async (like) => {
        const currentCard = matchingCardData[currentIndex];
        try {
            const response = await apiClient.post('/api/matchings/like',
                {
                    cardId: currentCard.card.cardId,
                    like: like
                });
            console.log(currentCard)
            // currentCard.card.checked = true;
            if (response.data) {
                setCurrentImageIndex(0);
                setIsFront(true);
                console.log(currentCard)
                if (response.data.matchingSuccess) {
                    const chatRoomResponse = await apiClient.post('/api/chatroom/create', {
                        roomType: "LIKE",
                        targetId: currentCard.memberId,
                    });
                    
                    const room = chatRoomResponse.data;
                    console.log(room)
                    setRoomData(room);  
                    
                    firework();
                    setModalIsOpen(true);
                } else {
                    fetchMatchingCards();
                }
            }
        } catch (error) {
            console.error('좋아요/싫어요 전송 중 오류 발생:', error);
        }
    };

    useEffect(() => {
        if (!modalIsOpen) {
            fetchMatchingCards();
        }
    }, [modalIsOpen]);

    const handleLike = () => {
        handleLikeDislike(true);
    };

    const handleDislike = () => {
        handleLikeDislike(false);
    };

    const questiontofriend = () => {
        const currentCard = matchingCardData[currentIndex];
        setMates(currentCard.mates || []);
        console.log(currentCard.mates)
        setModalIsOpen2(true);
    };

    const handleCardFlip = () => {
        setIsFront(!isFront);
    };

    const reportbadperson = () => {
        toast.success("신고 되었습니다.");
    };

    const toggleReportDropdown = () => {
        setIsReportDropdownOpen(!isReportDropdownOpen);
    };

    const ImageStack = ({ images }) => {
        const [imageOrder, setImageOrder] = useState([0, 1, 2]);

        const handleImageClick = () => {
            setImageOrder(prevOrder => {
                const newOrder = [...prevOrder];
                const first = newOrder.shift(); // 첫번째 요소를 빼내고
                newOrder.push(first); // 마지막에 추가
                return newOrder;
            });
        };

        return (
            <div className={styles.imageStackContainer} onClick={handleImageClick}>
                {imageOrder.map((index, position) => (
                        <img
                            key={index}
                            src={images[index]}
                            className={`${styles.cardImage} ${styles[`position${position}`]}`}
                            alt={`Image ${index}`}
                        />
                ))}
            </div>
        );
    };

    const renderCreateCardContent = () => (
            <div className={styles.match}>
            <div className={styles.exhaustcard}>
            <img src={`${process.env.REACT_APP_BASE_URL}/assets/Matchingnocard.png`} alt="Matching No Card"  className={styles.image} />
            <div className={styles.textdummy}>
                <p className={styles.title}>새로운 만남을 위해서</p>
                <p className={styles.subtitle}>내 카드를 만들어 주세요</p>
                </div>
                <button className={styles.inviteButton}  onClick={handleCreateCard}>카드 만들기</button>
            </div>
        </div>  
    );

    const renderViewOtherCardsContent = () => {
        if (!Array.isArray(matchingCardData) || matchingCardData.length === 0) {
            return (
                <div className={styles.match}>
                    <div className={styles.exhaustcard}>
                        <img src={`${process.env.REACT_APP_BASE_URL}/assets/Nomorecard.png`} alt="No More Card" className={styles.image} />
                        <div className={styles.textdummy}>
                        <p className={styles.title}>지인이 부족해요...</p>
                        <p className={styles.subtitle}>더 많은 카드를 받기 위해서</p>
                        </div>
                        <button className={styles.inviteButton} onClick={() => navigate('/friends')}>지인 초대하기</button>
                    </div>
                </div>
            );
        }

        const visibleCards = matchingCardData.filter(card => card && !card.checked);
        if (visibleCards.length === 0) {
            if (!matchingCardData || matchingCardData.length < 3) {
                return (
                    <div className={styles.match}>
                        <div className={styles.exhaustcard}>
                            <img src={`${process.env.REACT_APP_BASE_URL}/assets/Nomorecard.png`} alt="No More Card" className={styles.image} />
                            <div className={styles.textdummy}>
                            <p className={styles.title}>지인이 부족해요...</p>
                            <p className={styles.subtitle}>더 많은 카드를 받기 위해서</p>
                            </div>
                            <button className={styles.inviteButton} onClick={() => navigate('/friends')}>지인 초대하기</button>
                        </div>
                    </div>
                );
            } else {
                return (
                    <div className={styles.match}>
                        <div className={styles.exhaustcard}>
                    <img src={`${process.env.REACT_APP_BASE_URL}/assets/exhaustcard.png`} alt="Exhausted Card" />
                    <div className={styles.textdummy}>
                    <p className={styles.title}>카드가 떨어졌어요...</p>
                    <p className={styles.subtitle}>내일 접속해서 새로운 카드를 받으세요</p>
                    </div>
                </div>
                    </div>
                );
            }
        }

        const currentCard = matchingCardData[currentIndex];
        console.log(currentCard.memberId)
        const currentCardInfo = currentCard.card;
        // currentCardInfo.images = ['/assets/임시3.png', '/assets/임시2.png', '/assets/임시1.png'];

        const calculateAge = (birthdate) => {
            const today = new Date();
            const birthDate = new Date(birthdate);
            let age = today.getFullYear() - birthDate.getFullYear();
            const monthDiff = today.getMonth() - birthDate.getMonth();
            
            // 생일이 지나지 않았다면 나이에서 1을 뺍니다.
            if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
                age--;
            }
            return age;
        };

        const age = calculateAge(currentCard.birth);

        return (
            <div className={styles.match}>
                <div className={styles.reportcontainer}>
                    <i className="bi bi-three-dots" onClick={toggleReportDropdown}></i>
                    {isReportDropdownOpen && (
                        <div className={styles.dropdownMenu}>
                            <button onClick={reportbadperson}>신고</button>
                        </div>
                    )}
                </div> 
                <ToastContainer 
                    hideProgressBar={true}
                    closeOnClick
                    autoClose={700}
                    limit={1}
                    position="top-center"
                />
                <ReactCardFlip isFlipped={!isFront} flipDirection="horizontal">
                    <div className={`${styles.card} ${styles.front}`}>
                        <ImageStack images={currentCardInfo.images} />
                    </div>
                    <div>
                        <div className={styles.backContent}>
                            <div className={styles.tagContainer}>
                                <p className={styles.sectionTitle}>💖 나는 이런 사람이예요</p>
                                <div className={styles.longtext1}>
                                {currentCardInfo.tags.map((tag, index) => (
                                    <span key={index} className={styles.tag}>{tag}</span>
                                ))}
                                </div>
                            </div>
                            <div className={styles.introContainer}>
                                <p className={styles.sectionTitle}>💖 나의 한줄 소개</p>
                                <p className={styles.longtext2}>{currentCardInfo.info}</p>
                            </div>
                            <div className={styles.matesContainer}>
                                <p className={styles.sectionTitle}>💖 함께 아는 지인들</p>
                                {currentCard.mates && currentCard.mates.length > 1 ? (
                                <>
        <p className={styles.matesSummary} onClick={() => setIsMatesDropdownOpen(!isMatesDropdownOpen)}>
            {isMatesDropdownOpen === false ? <i className="bi bi-caret-down-fill"></i> : <i className="bi bi-caret-up-fill"></i>}
            &nbsp;
            {currentCard.mates[0].name}님 외 {currentCard.mates.length - 1}명이 함께 아는 지인입니다
        </p>
        {isMatesDropdownOpen && (
            <ul className={styles.matesDropdown}>
                {currentCard.mates.map((mate, index) => (
                    <li key={index}>{mate.name}</li>
                ))}
            </ul>
        )}
    </>
) : currentCard.mates && currentCard.mates.length === 1 ? (
    <p className={styles.matesSummary}>
        {currentCard.mates[0].name}님과 함께 아는 사이입니다.
    </p>
) : (
    <p className={styles.matesSummary}>함께 아는 지인이 없습니다.</p>
)}
                            </div>
                        </div>
                    </div>
                </ReactCardFlip>
                <div className={styles.cardbottom}>
                    <div className={styles.cardbuttomtext}>
                        <p>{currentCard.nickname}{
                            currentCard.gender === 'MALE' ? <span className={styles.maleSpan}><i className="bi bi-gender-male"></i></span>
                                : <span className={styles.femaleSpan}><i className="bi bi-gender-female"></i></span>
                        }</p>
                        <p>만 {age}세</p>
                    </div>
                    <button className={styles.flipbutton} onClick={handleCardFlip}>
                    <svg className={styles.flipicon} fill="none" height="20" viewBox="0 0 20 20" width="20" xmlns="http://www.w3.org/2000/svg">
    <g stroke="#FF9494" strokeLinecap="round" strokeWidth="1.5">
        <path d="m3.33337 10.8333c0 3.6819 2.98477 6.6667 6.66663 6.6667 3.682 0 6.6667-2.9848 6.6667-6.6667 0-3.68188-2.9847-6.66664-6.6667-6.66664-1.29938 0-2.51191.37174-3.5371 1.01468"></path>
        <path d="m7.69867 1.58163-1.44987 3.28435c-.18587.42104.00478.91303.42582 1.0989l3.28438 1.44986"></path>
    </g>
</svg>

                        <span className={styles.fliplabel}></span>
                    </button>
                </div>
                <div className={styles.buttons}>
                    <div className={styles.dislikebutton} onClick={handleDislike}>
                        {/* <i className="bi bi-x-lg"></i> */}
                        <img src={`${process.env.REACT_APP_BASE_URL}/assets/dislike.png`} alt="dislike" />
                    </div>
                    <div className={styles.askbutton} onClick={questiontofriend}>
                        {/* <i className="bi bi-chat-square-text"></i> */}
                        <img src={`${process.env.REACT_APP_BASE_URL}/assets/chat-after.png`} alt="chat" />
                    </div>
                    <div className={styles.likebutton} onClick={handleLike}>
                        {/* <i className="bi bi-heart-fill"></i> */}
                        <img src={`${process.env.REACT_APP_BASE_URL}/assets/like.png`} alt="like" />
                    </div>
                    
                    {/* <button onClick={handleDislike}>싫어요</button>
                    <button onClick={questiontofriend}>질문하기</button>
                    <button onClick={handleLike}>좋아요</button> */}
                    {/* <button onClick={testtest}>모달열기</button> */}
                </div>

                <Modal
                    isOpen={modalIsOpen2}
                    onRequestClose={() => setModalIsOpen2(false)}
                    className={styles.asktomates}
                    overlayClassName={styles.overlay}
                >
                    <div className={styles.matetitle}>
                    <h1>함께 아는 지인</h1>
                    <hr />
                    </div>
                    <div className={styles.gotomatechat}>
                    {mates.length > 0 ? (
                        mates.map((mate, index) => (
                            <div key={index} className={styles.mate}>
                                <img src={mate.profileImage === 'default.jpg' ? `${process.env.REACT_APP_BASE_URL}/assets/default.png` : mate.profileImage} alt={mate.name} className={styles.profileImage} />
                                <p>{mate.name}</p>
                                <i className="bi bi-chat-dots-fill" onClick={() => startChatWithMate(mate.memberId)}></i>
                            </div>
                        ))
                    ) : (
                        <p>함께 아는 지인이 없습니다.</p>
                    )}
                    </div>
                    <button onClick={() => setModalIsOpen2(false)}>닫기</button>
                </Modal>

                <Modal
                    isOpen={modalIsOpen}
                    onRequestClose={() => setModalIsOpen(false)}
                    className={styles.successlove}
                    overlayClassName={styles.overlay}
                >
                    <h1>💘 매칭이 성공했어요</h1>
                    <img src={currentCardInfo.images[0]} alt="Matching Couple"/>
                    <button className={styles.gotochat} onClick={() => navigateToChat()}>채팅방으로 바로가기</button>
                    <button  className={styles.closemodal} onClick={() => setModalIsOpen(false)}>닫기</button>
                </Modal>
            </div>
        );
    };

    const renderMatchingModeOffContent = () => (

        
        <div className={styles.match}>
        <div className={styles.exhaustcard}>
        <img src={`${process.env.REACT_APP_BASE_URL}/assets/NoMatchingMode.png`} alt="Matching No Mode"  className={styles.image} />
        <div className={styles.textdummy}>
            <p className={styles.title}>매칭 OFF 상태 입니다</p>
            <p className={styles.subtitle}>&nbsp;</p>
        </div>
            <button className={styles.inviteButton} onClick={() => navigate('/like')}>지인 현황보기</button>
        </div>
    </div>
    );

    const getContent = () => {

        if (isLoading) {
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

        const matchingMode = sessionStorage.getItem('matchingMode');
        // const matchingMode = true;
        // console.log(typeof(matchingMode)) /// true
        // console.log(matchingMode === "true" ) /// false
        if (matchingMode === "true") {
            if (!cardData) {
                return renderCreateCardContent();
            } else {
                return renderViewOtherCardsContent();
            }
        } else {
            return renderMatchingModeOffContent();
        }
    };

    return (
        <div>
            {getContent()}
        </div>
    );
};

export default Matching;
