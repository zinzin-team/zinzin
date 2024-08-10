import React, { useEffect, useState } from "react";
import axios from 'axios';
import styles from './List.module.css';

const Like = () => {
    const [successCount, setSuccessCount] = useState(0);
    const [matchingList, setMatchingList] = useState([]);

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

    return (
        <div className={styles.list}>
            <div>
                지인 매칭 현황
            </div>
            <div>
                총 {successCount}커플 탄생
            </div>
            <div>
                {matchingList.length > 0 ? (
                    matchingList.map((match, index) => (
                        <div key={index}>
                            {match.mate1.name}과(와) {match.mate2.name}이(가) 매칭되었습니다.
                        </div>
                    ))
                ) : (
                    <div>매칭된 지인이 없습니다.</div>
                )}
            </div>
        </div>
    )
}

export default Like;
