import React, { useEffect, useState } from "react";
import { useNavigate } from 'react-router-dom';
import apiClient from '../api/apiClient';
import "./LeaveView.css";

const LeaveView = () => {
    const navigate = useNavigate();
    const [name, setName] = useState(""); // 상태로 name을 관리

    useEffect(() => {
        const storedName = sessionStorage.getItem('name');
        if (storedName) {
            setName(storedName);
        }
    }, []); // 컴포넌트가 처음 렌더링될 때 sessionStorage에서 name을 가져옴

    const handleDelete = async (event) => {
        event.preventDefault();
        try {
            const response = await apiClient.delete('/api/member/me', {
                headers: {
                    "Content-Type": "application/json"
                  },
                  credentials: 'include',
            });
            if (response.status === 200) {
                sessionStorage.clear();
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
                {name} 님 덕분에
                <br />
                수 많은 지인들이 서로 연결되고 있어요.
                <br />
                탈퇴 하신다니 너무 아쉬워요 <span role="img" aria-label="crying">😢</span>
            </p>
            <p className="leave-warning">
                회원 탈퇴 후 15일 간 회원 정보가 보관되며, 이후에는 회원 정보가 완전히 삭제되어 재가입이 필요 합니다.
            </p>
            <form className="leave-form" onSubmit={handleDelete}>
                <button type="submit" className="leave-button">탈퇴하기</button>
            </form>
        </div>
    );
}

export default LeaveView;
