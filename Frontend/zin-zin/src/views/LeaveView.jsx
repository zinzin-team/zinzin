import React, { useEffect, useState } from "react";
import { useNavigate, Link } from 'react-router-dom';
import apiClient from '../api/apiClient';
import "./LeaveView.css";

const LeaveView = () => {
    const navigate = useNavigate();
    const [name, setName] = useState("");
    const [searchId, setSearchId] = useState("");
    const [enteredId, setEnteredId] = useState("");
    const [isChecked1, setIsChecked1] = useState(false);
    const [isChecked2, setIsChecked2] = useState(false);
    const [isButtonActive, setIsButtonActive] = useState(false);
    const [errorMessage, setErrorMessage] = useState(""); // 추가: 에러 메시지 관리

    useEffect(() => {
        const storedName = sessionStorage.getItem('name');
        const storedSearchId = sessionStorage.getItem('searchId');
        if (storedName) {
            setName(storedName);
        }
        if (storedSearchId) {
            setSearchId(storedSearchId);
        }
    }, []);

    useEffect(() => {
        // 체크박스가 모두 체크되고 입력된 ID가 있는 경우에만 버튼 활성화
        if (isChecked1 && isChecked2 && enteredId) {
            setIsButtonActive(true);
        } else {
            setIsButtonActive(false);
        }
    }, [isChecked1, isChecked2, enteredId]);

    const handleDelete = async (event) => {
        event.preventDefault();
        if (enteredId !== searchId) {
            setErrorMessage("입력한 아이디가 일치하지 않습니다."); // 추가: 경고 메시지 설정
            return;
        }

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
                setErrorMessage("탈퇴 중 문제가 발생했습니다. 다시 시도해주세요."); // 추가: 에러 메시지 설정
            }
        } catch (error) {
            setErrorMessage("탈퇴 요청 중 에러가 발생했습니다. 다시 시도해주세요."); // 추가: 에러 메시지 설정
        }
    };

    return (
        <div className="leave-container">
            <div className="toptop">
                <Link to="/settings" className="backButton"><i className="bi bi-chevron-left"/></Link>
                <h2>회원 탈퇴</h2>
            </div>
            <div className="leave-message-container">
                <p className="leave-message">정말 정말 탈퇴 하시나요 <span role="img" aria-label="crying">😭</span></p>
                <p className="leave-reason">
                    {name} 님 덕분에
                    <br />
                    수 많은 지인들이 서로 연결되고 있어요.
                    <br />
                    탈퇴 하신다니 너무 아쉬워요 <span role="img" aria-label="crying">😢</span>
                </p>
            </div>
            <div className="leave-agreement">
                <h3>탈퇴 동의</h3>
                <p>회원 탈퇴 후 15일 이내에 서비스에 다시 로그인하면, 탈퇴 요청이 철회되고 다시 서비스를 이용할 수 있습니다.</p>
                <div className="checkbox-container">
                    <input
                        id="checkbox1"
                        type="checkbox"
                        checked={isChecked1}
                        onChange={(e) => setIsChecked1(e.target.checked)}
                    />
                    <label htmlFor="checkbox1">확인했습니다.</label>
                </div>
                <p>회원 탈퇴 후 15일 간 회원 정보가 보관되며, 이후에는 회원 정보가 완전히 삭제되어 재가입이 필요합니다.</p>
                <div className="checkbox-container">
                    <input
                        id="checkbox2"
                        type="checkbox"
                        checked={isChecked2}
                        onChange={(e) => setIsChecked2(e.target.checked)}
                    />
                    <label htmlFor="checkbox2">확인했습니다.</label>
                </div>
            </div>

            <p className="leave-warning">
                탈퇴를 위해 아이디를 정확하게 입력해주세요.
            </p>
            <div className="id-check">
                <div className="displayed-id">{searchId}</div>
                <input
                    type="text"
                    placeholder="아이디를 입력해주세요"
                    value={enteredId}
                    onChange={(e) => setEnteredId(e.target.value)}
                    className="input-field"
                />
            </div>

            {errorMessage && <p className="error-message">{errorMessage}</p>} {/* 추가: 에러 메시지 UI */}

            <form className="leave-form" onSubmit={handleDelete}>
                <button
                    type="submit"
                    className={`leave-button ${isButtonActive ? 'active' : 'inactive'}`}
                    disabled={!isButtonActive}
                >
                    탈퇴하기
                </button>
            </form>
        </div>
    );
}

export default LeaveView;
