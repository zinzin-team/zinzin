import React, { useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import "./LeaveView.css";

const LeaveView = () => {
    const navigate = useNavigate();

    // useEffect(() => {
    //     const storedData = JSON.parse(sessionStorage.getItem('userData'));
    //     if (storedData) {
    //         setUserData(storedData);
    //     }
    // }, [setUserData]);

    const handleDelete = async (event) => {
        event.preventDefault();
        const accessToken = sessionStorage.getItem("accessToken");

        try {
            const response = await axios.delete(`${process.env.REACT_APP_API_URL}/api/member/me`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                    'Content-Type': 'application/json'
                }
            });
            if (response.status === 200) {
                alert("탈퇴가 완료되었습니다.");
                navigate("/login");
            } else {
                alert("탈퇴 중 문제가 발생했습니다. 다시 시도해주세요.");
            }
        } catch (error) {
            console.error("탈퇴 요청 중 에러가 발생했습니다.", error);
            alert("탈퇴 요청 중 에러가 발생했습니다. 다시 시도해주세요.");
        }
    };

    return (
        <div className="leave-container">
            <h1 className="leave-title">회원탈퇴</h1>
            <p className="leave-message">정말 정말 탈퇴 하시나요 <span role="img" aria-label="crying">😭</span></p>
            <p className="leave-reason">
                김윤지 님 덕분에
                <br />
                수 많은 지인들이 서로 연결되고 있어요.
                <br />
                탈퇴 하신다니 너무 아쉬워요 <span role="img" aria-label="crying">😢</span>
            </p>
            <p className="leave-warning">
                회원 탈퇴 후 15일 간 회원 정보가 보관되며, 이후에는 회원 정보가 완전히 삭제되어 재가입이 필요 합니다.
            </p>
            <form className="leave- form" onSubmit={handleDelete}>
                <button type="submit" className="leave-button">탈퇴하기</button>
            </form>
        </div>
    );
}

export default LeaveView;
