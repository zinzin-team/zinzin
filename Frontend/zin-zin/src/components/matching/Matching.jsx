import styles from './Matching.module.css';
import React, { useEffect, useState } from "react";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useSwipeable } from 'react-swipeable';
import Modal from 'react-modal';
import confetti from 'canvas-confetti'; 


Modal.setAppElement('#root'); // This is needed for accessibility reasons

const Matching = () => {
    const [cardData, setCardData] = useState(null);
    const [matchingCardData, setMatchingCardData] = useState(null);
    const [currentIndex, setCurrentIndex] = useState(0);
    const [currentImageIndex, setCurrentImageIndex] = useState(0);
    const [isFront, setIsFront] = useState(true);
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const [modalMessage, setModalMessage] = useState("");
    const navigate = useNavigate();


    function firework() {
        var duration = 20 * 100;
        var animationEnd = Date.now() + duration;
        var defaults = { startVelocity: 25, spread: 360, ticks: 50, zIndex: 0 };

        function randomInRange(min, max) {
            return Math.random() * (max - min) + min;
        }

        var interval = setInterval(function () {
            var timeLeft = animationEnd - Date.now();

            if (timeLeft <= 0) {
                return clearInterval(interval);
            }

            var particleCount = 50 * (timeLeft / duration);
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
    }

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('/api/cards', {
                    headers: { 'accesstoken': 'token' }
                });
                if (response.data) {
                    const { cardId, tags, info, images } = response.data;
                    setCardData({ cardId, tags, info, images });
                    sessionStorage.setItem('cardData', JSON.stringify({ cardId, tags, info, images }));
                } else {
                    console.error('Failed to fetch card data');
                }
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        const fetchMatchingCards = async () => {
            try {
                const response = await axios.get('/api/matchings');
                if (response.data) {
                    setMatchingCardData(response.data);
                } else {
                    console.error('Failed to fetch matching card data');
                }
            } catch (error) {
                console.error('Error fetching matching card data:', error);
            }
        };

        const storedCardData = sessionStorage.getItem('cardData');
        if (storedCardData) {
            setCardData(JSON.parse(storedCardData));
        } else {
            fetchData();
        }

        fetchMatchingCards();
    }, []);

    const handleCreateCard = () => {
        navigate('/create-card');
    };

    const handleNextCard = () => {
        if (matchingCardData && matchingCardData.length > 0) {
            let newIndex = currentIndex;
            do {
                newIndex = (newIndex + 1) % matchingCardData.length;
            } while (matchingCardData[newIndex].checked && newIndex !== currentIndex);

            if (newIndex !== currentIndex) {
                setCurrentIndex(newIndex);
                setCurrentImageIndex(0);
                setIsFront(true);
            }
        }
    };

    const handleLikeDislike = async (like) => {
        const currentCard = matchingCardData[currentIndex];
        try {
            const response = await axios.post(`/api/matchings/${currentCard.id}/like`, { like });
            if (response.data) {
                if (response.data.chattingRoom) {
                    firework();
                    setModalMessage("양방향 호감! 채팅방이 생성되었습니다.");
                    setModalIsOpen(true);
                    setTimeout(() => {
                        setModalIsOpen(false);
                        handleNextCard();
                    }, 2000);
                } else {
                        handleNextCard();
                }
            }
        } catch (error) {
            console.error('Error sending like/dislike:', error);
        }
    };

    const handleLike = () => {
        handleLikeDislike(true);
    };

    const handleDislike = () => {
        handleLikeDislike(false);
    };

    const questiontofriend = () => {
        
    };

    const handleImageSwipe = (direction) => {
        const images = matchingCardData[currentIndex].images;
        if (direction === 'left') {
            setCurrentImageIndex((prevIndex) => (prevIndex + 1) % images.length);
        } else if (direction === 'right') {
            setCurrentImageIndex((prevIndex) => (prevIndex - 1 + images.length) % images.length);
        }
    };

    const handleCardFlip = () => {
        setIsFront(!isFront);
    };
    const  reportbadperson = () => {
        alert("신고 되었습니다.")
    };
    

    const handlers = useSwipeable({
        onSwipedLeft: () => handleImageSwipe('left'),
        onSwipedRight: () => handleImageSwipe('right'),
        preventDefaultTouchmoveEvent: true,
        trackMouse: true
    });

    const renderCreateCardContent = () => (
        <div className={styles.match}>
            <img src="/assets/Matchingnocard.png" alt="Matching No Card" />
            <div className={styles.makecard}>
                <p>새로운 만남을 위해서</p>
                <p>내 카드를 만들어 주세요</p>
                <button onClick={handleCreateCard}>카드 만들기</button>
            </div>
        </div>
    );

    const renderViewOtherCardsContent = () => {
        if (!matchingCardData || matchingCardData.length === 0 || matchingCardData.filter(card => !card.checked).length === 0) {
            if (!matchingCardData || matchingCardData.length < 3) {
                return (
                    <div className={styles.match}>
                    <div className={styles.exhaustcard}>
                        <p className={styles.title}>지인이 부족해요...</p>
                        <img src="/assets/Nomorecard.png" alt="No More Card" className={styles.image} />
                        <p className={styles.subtitle}>더 많은 카드를 받기 위해서</p>
                        <button className={styles.inviteButton} onClick={() => navigate('/friend')}>지인 초대하기</button>
                    </div>
                </div>
                
                );
            } else {
                return (
                    <div className={styles.match}>
                        <div className={styles.exhaustcard}>
                            <p>카드가 떨어졌어요...</p>
                            <img src="/assets/exhaustcard.png" alt="Exhausted Card" />
                            <p>내일 접속해서 새로운 카드를 받으세요</p>
                        </div>
                    </div>
                );
            }
        }

        const visibleCards = matchingCardData.filter(card => !card.checked);
        const currentCard = visibleCards[currentIndex];

        return (
            <div className={styles.match}>
                <br />
                <p>다른 사람의 카드를 확인하세요!</p>
                <div
                    className={`${styles.card} ${isFront ? styles.front : styles.back}`}
                    onClick={handleCardFlip}
                    {...handlers}
                >
                    {isFront ? (
                        <div className={styles.frontContent}>
                            <img src={currentCard.images[currentImageIndex]} alt={`Card ${currentCard.id}`} />
                            <p>{currentCard.nickname}</p>
                            <p>{currentCard.age}세, {currentCard.gender}</p>
                            <button onClick={reportbadperson}>신고</button>
                        </div>
                    ) : (
                        <div className={styles.backContent}>
                            <p>{currentCard.info}</p>
                            <div>
                                {currentCard.tags.map((tag, index) => (
                                    <span key={index} className={styles.tag}>{tag}</span>
                                ))}
                            </div>
                        </div>
                    )}
                </div>
                <div className={styles.buttons}>
                    <button onClick={handleLike}>좋아요</button>
                    <button onClick={handleDislike}>싫어요</button>
                    <button onClick={questiontofriend}>질문하기</button>
                </div>
                <Modal
                    isOpen={modalIsOpen}
                    onRequestClose={() => setModalIsOpen(false)}
                    className={styles.modal}
                    overlayClassName={styles.overlay}
                >
                    <h1>매칭이 성공했어요~💘</h1>
                    <h2>{modalMessage}</h2>
                    <img src="Matchingcouple"/>
                    <h1>채팅방으로 바로가기 버튼 수정 예정</h1>
                    <button onClick={() => setModalIsOpen(false)}>닫기</button>
                </Modal>
            </div>
        );
    };

    const renderMatchingModeOffContent = () => (
        <div className={styles.match}>
            <div className={styles.matchoff}>
            <img src="/assets/NoMatchingMode.png" alt="Matching No Mode" />
            </div>
            <div className={styles.offModeContent}>
                <p>매칭 OFF 상태 입니다</p>
                <button onClick={() => navigate('/like')}>지인 현황보기</button>
            </div>
        </div>
    );

    const getContent = () => {
        // return renderCreateCardContent(); // 나중에 지우기
        if (!cardData) {
            return null;
        }

        const { matchingMode, existed } = cardData;

        if (matchingMode) {
            if (existed) {
                return renderViewOtherCardsContent();
            } else {
                return renderCreateCardContent();
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
