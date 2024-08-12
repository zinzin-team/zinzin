import React, { useEffect, useState } from "react";
import axios from 'axios';
import styles from './List.module.css';

const Like = () => {
    const [successCount, setSuccessCount] = useState(0);
    const [matchingList, setMatchingList] = useState([]);
    const [currentPage, setCurrentPage] = useState(1); // 현재 페이지 상태 추가
    const itemsPerPage = 10; // 페이지당 아이템 수

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
            }
        };

        fetchSuccessCount();
        fetchMatchingList();
    }, []);

    // 현재 페이지에 표시할 아이템 계산
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentItems = matchingList.slice(indexOfFirstItem, indexOfLastItem);

    // matchingList.push({  mate1: {
    //     name: "동해물과 백두산이 마르고",
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

    // 페이지 변경 핸들러
    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    // 총 페이지 수 계산
    const totalPages = Math.ceil(matchingList.length / itemsPerPage);

    return (
        <div className={styles.list}>
            <div className={styles.header}>
                <h2>지인 매칭 현황</h2>
                <p>총 {successCount}커플 탄생</p>
            </div>
            <div className={styles.matchList}>
                {currentItems.length > 0 ? (
                    currentItems.map((match, index) => (
                        <div key={index} className={styles.matchItem}>
                            <div className={styles.mate}>
                                <img 
                                    src={match.mate1.profileImage ? `${match.mate1.profileImage}` : "/assets/default.png"} 
                                    alt={match.mate1.name} 
                                    className={styles.profileImage} 
                                />
                                <p>{match.mate1.name}</p>
                            </div>
                            <div className={styles.heart}>
                                <i className="bi bi-heart-fill"></i>
                            </div>
                            <div className={styles.mate}>
                                <img 
                                    src={match.mate2.profileImage ? `${match.mate2.profileImage}` : "/assets/default.png"} 
                                    alt={match.mate2.name} 
                                    className={styles.profileImage} 
                                    />
                                <p>{match.mate2.name}</p>
                            </div>
                        </div>
                    ))
                ) : (
                    <div>매칭된 지인이 없습니다.</div>
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
