import React from "react";
import styles from './UserGuide.module.css';

const UserGuide = () => {
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

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>FAQ</h1>
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
