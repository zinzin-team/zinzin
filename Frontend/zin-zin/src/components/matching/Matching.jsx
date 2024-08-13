import styles from './Matching.module.css';
import React, { useEffect, useState } from "react";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import confetti from 'canvas-confetti';
import ReactCardFlip from 'react-card-flip';

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

    const testtest = () => {
        firework();
        setModalIsOpen(true);
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
            const token = sessionStorage.getItem('accessToken');
            const response = await axios.get('/api/matchings', {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            if (response.data && Array.isArray(response.data.matchings)) {
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

    useEffect(() => {
        const fetchData = async () => {
            try {
                const token = sessionStorage.getItem('accessToken');
                const response = await axios.get('/api/cards', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                if (response.data) {
                    const { cardId, tags, info, images } = response.data;
                    console.log(images)
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

        const storedMatchingMode = true;

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
        const token = sessionStorage.getItem('accessToken');
        try {
            const response = await axios.post('/api/matchings/like',
                {
                    cardId: currentCard.card.cardId,
                    like: like
                },
                {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                }
            );
            currentCard.card.checked = true;
            if (response.data) {
                setCurrentImageIndex(0);
                setIsFront(true);
                if (response.data.matchingSuccess) {
                    firework();
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
        const images = matchingCardData[currentIndex].card.images;
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
                            <p className={styles.title}>지인이 부족해요...</p>
                            <img src="/assets/Nomorecard.png" alt="No More Card" className={styles.image} />
                            <p className={styles.subtitle}>더 많은 카드를 받기 위해서</p>
                            <button className={styles.inviteButton} onClick={() => navigate('/friends')}>지인 초대하기</button>
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
        // currentCard.mates.shift();currentCard.mates.shift();
        // console.log(currentCard.mates.push({memberId: 111, name: '이나라', profileImage: '/assets/박상우.png'}))
        // console.log(currentCard.mates.push({memberId: 111, name: '이나라', profileImage: 'default.jpg'}))
        // console.log(currentCard.mates.push({memberId: 111, name: '이나라', profileImage: 'default.jpg'}))
        // console.log(currentCardInfo.tags.push("피자먹는"))
        // currentCardInfo.info = "안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안안"
        currentCardInfo.images = ['/assets/임시3.png', '/assets/임시2.png', '/assets/임시1.png'];

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

                <ReactCardFlip isFlipped={!isFront} flipDirection="horizontal">
                    <div
                        className={`${styles.card} ${styles.front}`}
                    >
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
                        <p>{currentCard.nickname}</p>
                        <p>{currentCard.age} {currentCard.gender === 'MALE' ? '남' : '여'}</p>
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
                    <i className="bi bi-x-lg"></i>
                    </div>
                    <div className={styles.askbutton} onClick={questiontofriend}>
                    <i className="bi bi-chat-square-text"></i>
                    </div>
                    <div className={styles.likebutton} onClick={handleLike}>
                    <i className="bi bi-heart-fill"></i>
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
                                <img src={mate.profileImage} alt={mate.name} className={styles.profileImage} />
                                <p>{mate.name}</p>
                                <i className="bi bi-chat-dots-fill"></i>
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
                    <button  className={styles.gotochat} >채팅방으로 바로가기</button>
                    <button  className={styles.closemodal} onClick={() => setModalIsOpen(false)}>닫기</button>
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
        if (isLoading) {
            return (
                <div className={styles.loading}>
                    <p>로딩 중...</p>
                </div>
            );
        }

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
