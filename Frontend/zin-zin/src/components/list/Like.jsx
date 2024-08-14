import React, { useEffect, useState } from "react";
import axios from 'axios';
import styles from './List.module.css';
import { useNavigate } from 'react-router-dom';


const Like = () => {
    const [successCount, setSuccessCount] = useState(0);
    const [matchingList, setMatchingList] = useState([]);
    const [currentPage, setCurrentPage] = useState(1); // 현재 페이지 상태 추가
    const [loading, setLoading] = useState(true); // 로딩 상태 추가
    const itemsPerPage = 10; // 페이지당 아이템 수
    const navigate = useNavigate();

   // matchingList.push({  mate1: {
    //     name: "호기심 많은 청둥오리",
    //     profileImage: "/assets/박상우.png" // 공개
    // },
    // mate2: {
    //     name: "김은지",
    //     profileImage: null // 공개
    // }})
    // matchingList.push({  mate1: {
    //     name: "조성훈",
    //     profileImage: "/assets/박상우.png" // 공개
    // },
    // mate2: {
    //     name: "김은지",
    //     profileImage: null // 공개
    // }})
    
    // matchingList.push({  mate1: {
    //     name: "조성훈",
    //     profileImage: "/assets/박상우.png" // 공개
    // },
    // mate2: {
    //     name: "김은지",
    //     profileImage: null // 공개
    // }})
    // matchingList.push({  mate1: {
    //     name: "조성훈",
    //     profileImage: "/assets/박상우.png" // 공개
    // },
    // mate2: {
    //     name: "김은지",
    //     profileImage: null // 공개
    // }})
    // matchingList.push({  mate1: {
    //     name: "조성훈",
    //     profileImage: "/assets/박상우.png" // 공개
    // },
    // mate2: {
    //     name: "김은지",
    //     profileImage: null // 공개
    // }})
    // matchingList.push({  mate1: {
    //     name: "조성훈",
    //     profileImage: "/assets/박상우.png" // 공개
    // },
    // mate2: {
    //     name: "김은지",
    //     profileImage: null // 공개
    // }})
    // matchingList.push({  mate1: {
    //     name: "조성훈",
    //     profileImage: "/assets/박상우.png" // 공개
    // },
    // mate2: {
    //     name: "산뜻한 우산",
    //     profileImage: null // 공개
    // }}) 

    useEffect(() => {
        const fetchSuccessCount = async () => {
            try {
                const token = sessionStorage.getItem('accessToken');
                const response = await axios.get('/api/success-count', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                if (response.data) {
                    setSuccessCount(response.data.successCount);
                }
            } catch (error) {
                console.error('Failed to fetch success count:', error);
            }
        };

        const fetchMatchingList = async () => {
            try {
                const token = sessionStorage.getItem('accessToken');
                const response = await axios.get('/api/matching-list', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                if (response.data) {
                    setMatchingList(response.data);
                }
            } catch (error) {
                console.error('Failed to fetch matching list:', error);
            } finally {
                setLoading(false); // 데이터 로드가 완료되면 로딩 상태를 false로 설정
            }
        };

        fetchSuccessCount();
        fetchMatchingList();
    }, []);

    // 현재 페이지에 표시할 아이템 계산
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentItems = matchingList.slice(indexOfFirstItem, indexOfLastItem);
    console.log(matchingList)

    // 페이지 변경 핸들러
    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    // 총 페이지 수 계산
    const totalPages = Math.ceil(matchingList.length / itemsPerPage);

    if (loading) {
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
        <div className={styles.list}>
            <div className={styles.tmptmp}>
                <p className={styles.titletitle}>지인 매칭 현황</p>
            </div>
            <div className={styles.header}>
                {successCount === 0 || successCount === null ? <p></p> : <p>총 {successCount}커플 탄생</p> }
            </div>
            <div className={styles.matchList}>
                {currentItems.length > 0 ? (
                    currentItems.map((match, index) => (
                        <div key={index} className={styles.matchItem}>
                            <div className={styles.mate}>
                                <img 
                                    src={match.mate1.profileImage ? `${match.mate1.profileImage}` : `${process.env.REACT_APP_BASE_URL}/assets/default.png`} 
                                    alt={match.mate1.name} 
                                    className={styles.profileImage} 
                                />
                                <p className={styles.matename}>{match.mate1.name}</p>
                            </div>
                            <div className={styles.heart}>
                                <i className="bi bi-heart-fill"></i>
                            </div>
                            <div className={styles.mate}>
                                <img 
                                    src={match.mate2.profileImage ? `${match.mate2.profileImage}` : `${process.env.REACT_APP_BASE_URL}/assets/default.png`} 
                                    alt={match.mate2.name} 
                                    className={styles.profileImage} 
                                    />
                                <p className={styles.matename}>{match.mate2.name}</p>
                            </div>
                        </div>
                    ))
                ) : (
                    <div className={styles.nomatch}>
                        <img src={`${process.env.REACT_APP_BASE_URL}/assets/Matchingcouple.png`} alt="Matching No Mode" />
                       <p>
                       지인 소식이 비어있어요
                        </p> 
                        <button className={styles.inviteButton} onClick={() => navigate('/friends')}>지인 초대하기</button>
                    </div>
                )}
            </div>
            <div className={styles.pagination}>
                {Array.from({ length: totalPages }, (_, index) => (
                    <button
                        key={index + 1}
                        onClick={() => handlePageChange(index + 1)}
                        className={currentPage === index + 1 ? styles.activePage : ''}
                    >
                        {index + 1}
                    </button>
                ))}
            </div>
        </div>
    )
}

export default Like;
