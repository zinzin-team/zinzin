import { Link } from "react-router-dom"; 
import React, { useState, useEffect } from "react";
import styles from './UserGuide.module.css';

const UserGuide = () => {
    const [isLoading, setIsLoading] = useState(true); // 로딩 상태 추가

    const faqs = [
        {
            question: "저는 왜 아무도 호감 표시를 안 해주나요?",
            answer: "음.."
        },
        {
            question: "전 남자친구가 추천으로 떴어요. 신고 할 수 있나요?",
            answer: "다시 추천 받고 싶지 않은 경우, 차단이 가능합니다. 차단 기능을 이용해주세요 :)"
        },
        {
            question: "분명 제 얼굴이 나온 사진 세 장을 등록했는데 사진이 반려됐어요. 못생겨서 그런가요?",
            answer: "등록하신 사진은 부적절 여부를 관리자가 판단 후에 승인 됩니다 :)"
        },
        {
            question: "질문",
            answer: "답변"
        },
    ];

    useEffect(() => {
        // 데이터를 가져오는 시뮬레이션을 위해 타임아웃을 사용
        const fetchData = () => {
            setTimeout(() => {
                setIsLoading(false); // 로딩이 완료되면 상태를 false로 변경
            }, 2000); // 2초 후에 로딩 완료
        };

        fetchData();
    }, []);

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

    return (
        <div className={styles.container}>
            <div className={styles.toptop}>
                <Link to="/mypage" className={styles.backButton}><i className="bi bi-chevron-left"/></Link>
                <h2>FAQ</h2>
            </div>
            <p className={styles.subtitle}>자주 묻는 질문들을 모아봤어요!</p>
            {faqs.map((faq, index) => (
                <div key={index} className={styles.faqItem}>
                    <p className={styles.question}>Q. {faq.question}</p>
                    <p className={styles.answer}>A. {faq.answer}</p>
                </div>
            ))}
        </div>
    );
}

export default UserGuide;
