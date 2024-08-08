import styles from './Matching.module.css';
import React, { useEffect, useState } from "react";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useSwipeable } from 'react-swipeable';
import Modal from 'react-modal';
import confetti from 'canvas-confetti'; 

Modal.setAppElement('#root'); // 이 설정은 접근성을 위해 필요합니다.

const Matching = () => {
    const [cardData, setCardData] = useState(null);
    const [matchingCardData, setMatchingCardData] = useState(null);
    const [currentIndex, setCurrentIndex] = useState(0);
    const [currentImageIndex, setCurrentImageIndex] = useState(0);
    const [isFront, setIsFront] = useState(true);
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const [modalIsOpen2, setModalIsOpen2] = useState(false);
    const [modalMessage, setModalMessage] = useState("");
    const [mates, setMates] = useState([]);
    const navigate = useNavigate();

    const testtest = () => {
        firework();
        setModalIsOpen(true);
    };

    function firework() {
        var duration = 20 * 150;
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
    }

    const fetchMatchingCards = async () => {
        try {
            const response = await axios.get('/api/matchings', {
                headers: { 'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzNjQyMzA1OCIsInJvbGUiOiJVU0VSIiwiZXhwIjo2MDAwMDAxNzIyOTMxMjY5LCJpYXQiOjE3MjI5MzEyNjksIm1lbWJlcklkIjo1fQ.2MzZDZcIucUDh0J6x1CjjKajTU_kOI47ijEmKY5AUhU'}
            });
            if (response.data && Array.isArray(response.data.matchings)) {
                setMatchingCardData(response.data.matchings);   
                console.log(response.data.matchings)
            } else {
                console.error('매칭 카드 데이터를 가져오는 데 실패했습니다.');
            }
        } catch (error) {
            console.error('매칭 카드 데이터를 가져오는 중 오류 발생:', error);
        }
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('/api/cards', {
                    headers: { 'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzNjQyMzA1OCIsInJvbGUiOiJVU0VSIiwiZXhwIjo2MDAwMDAxNzIyOTMxMjY5LCJpYXQiOjE3MjI5MzEyNjksIm1lbWJlcklkIjo1fQ.2MzZDZcIucUDh0J6x1CjjKajTU_kOI47ijEmKY5AUhU'}
                });
                if (response.data) {
                    const { cardId, tags, info, images } = response.data;
                    
                    setCardData({ cardId, tags, info, images });
                    sessionStorage.setItem('cardData', JSON.stringify({ cardId, tags, info, images }));
                } else {
                    console.error('카드 데이터를 가져오는 데 실패했습니다.');
                }
            } catch (error) {
                console.error('데이터를 가져오는 중 오류 발생:', error);
            }
        };

        const storedMatchingMode = true;

        if (storedMatchingMode) {
            const fetchDataAndSetCardData = async () => {
                await fetchData();
                const storedCardData = sessionStorage.getItem('cardData');
                console.log(storedCardData)
                if(storedCardData){
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
        const token = sessionStorage.getItem('accesstoken');
        try {
            const response = await axios.post('/api/matchings/like', 
                { 
                    cardId: currentCard.card.cardId,
                    like: like 
                }, 
                {
                    headers: {
                        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzNjQyMzA1OCIsInJvbGUiOiJVU0VSIiwiZXhwIjo2MDAwMDAxNzIyOTMxMjY5LCJpYXQiOjE3MjI5MzEyNjksIm1lbWJlcklkIjo1fQ.2MzZDZcIucUDh0J6x1CjjKajTU_kOI47ijEmKY5AUhU'
                    }
                }
            );
            currentCard.card.checked = true
            console.log(response.data)
            if (response.data) {
                setCurrentImageIndex(0);
                setIsFront(true);
                if (response.data.matchingSuccess) {
                    firework();
                    setModalMessage("양방향 호감! 채팅방이 생성되었습니다.");
                    setModalIsOpen(true);
                    fetchMatchingCards();
                } else {
                    fetchMatchingCards();
                }
            }
        } catch (error) {
            console.error('좋아요/싫어요 전송 중 오류 발생:', error);
        }
    };
    

    const handleLike = () => {
        handleLikeDislike(true);
    };

    const handleDislike = () => {
        handleLikeDislike(false);
    };

    const questiontofriend = () => {
        const currentCard = matchingCardData[currentIndex];
        setMates(currentCard.mates || []);
        setModalIsOpen2(true);
    };

    const handleImageSwipe = (direction) => {
        const images = matchingCardData[currentIndex].card.image;
        if (direction === 'left') {
            setCurrentImageIndex((prevIndex) => (prevIndex + 1) % images.length);
        } else if (direction === 'right') {
            setCurrentImageIndex((prevIndex) => (prevIndex - 1 + images.length) % images.length);
        }
    };

    const handleCardFlip = () => {
        setIsFront(!isFront);
    };

    const reportbadperson = () => {
        alert("신고 되었습니다.");
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
        if (!Array.isArray(matchingCardData) || matchingCardData.length === 0) {
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
        }

        const visibleCards = matchingCardData.filter(card => card && !card.checked);
        console.log(matchingCardData)
        if (visibleCards.length === 0) {
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

        const currentCard = matchingCardData[currentIndex];
        const currentCardInfo = currentCard.card;
        console.log(currentIndex)

        currentCardInfo.image = ['/assets/홍창기.png', '/assets/박상우.png', '/assets/김윤지.png'];

        return (
            <div className={styles.match}>
                <div
                    className={`${styles.card} ${isFront ? styles.front : styles.back}`}
                    onClick={handleCardFlip}
                    {...handlers}
                >
                    {isFront ? (
                        <div className={styles.frontContent}>
                            <img src={currentCardInfo.image[currentImageIndex]} alt={`Card ${currentCard.memberId}`} {...handlers} />  
                        </div>
                    ) : (
                        <div className={styles.backContent}>
                            <p>{currentCardInfo.info}</p>
                            <div>
                                {currentCardInfo.tags.map((tag, index) => (
                                    <span key={index} className={styles.tag}>{tag}</span>
                                ))}
                            </div>
                        </div>
                    )}
                </div>
                <p>{currentCard.nickname}</p>
                <p>{currentCard.age}세, {currentCard.gender}</p>
                <button onClick={reportbadperson}>신고</button>
                <div className={styles.buttons}>
                    <button onClick={handleLike}>좋아요</button>
                    <button onClick={handleDislike}>싫어요</button>
                    <button onClick={questiontofriend}>질문하기</button>
                    <button onClick={testtest}>모달열기</button>
                </div>

                <Modal
                    isOpen={modalIsOpen2}
                    onRequestClose={() => setModalIsOpen2(false)}
                    className={styles.modal2}
                    overlayClassName={styles.overlay}
                >
                    <h1>함께 아는 지인</h1>
                    <hr/>
                    {mates.length > 0 ? (
                        mates.map((mate, index) => (
                            <div key={index} className={styles.mate}>
                                {/* <img src={mate.profileImage} alt={mate.name} className={styles.profileImage} /> */}
                                <img src="/assets/홍창기.png" />
                                <p>{mate.name}</p>
                            </div>
                        ))
                    ) : (
                        <p>함께 아는 지인이 없습니다.</p>
                    )}
                    <button onClick={() => setModalIsOpen2(false)}>닫기</button>
                </Modal>

                <Modal
                    isOpen={modalIsOpen}
                    onRequestClose={() => setModalIsOpen(false)}
                    className={styles.modal}
                    overlayClassName={styles.overlay}
                >
                    <h1>매칭이 성공했어요~💘</h1>
                    <h2>{modalMessage}</h2>
                    <img src="assets/Matchingcouple.png" alt="Matching Couple"/>
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
        const matchingMode = true;
        if (matchingMode === true) {
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
