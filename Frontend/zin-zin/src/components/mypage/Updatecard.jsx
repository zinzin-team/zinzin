import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import apiClient from '../../api/apiClient';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import styles from './Updatecard.module.css';  // 경로를 확인하고 올바르게 수정
import 'bootstrap-icons/font/bootstrap-icons.css';

const UpdateCard = () => {
    const { cardId } = useParams(); // URL 파라미터에서 cardId 가져오기
    const [selectedOption, setSelectedOption] = useState('option1');
    const [selectedImages, setSelectedImages] = useState([]);
    const [selectedFiles, setSelectedFiles] = useState([null, null, null]);
    const [selectedTags, setSelectedTags] = useState([]);
    const [introduction, setIntroduction] = useState('');
    const [charCount, setCharCount] = useState(0);
    const [isLoading, setIsLoading] = useState(true); // 로딩 상태 추가
    const MAX_TAGS = 5;
    const navigate = useNavigate();

    // 기존 카드 정보를 가져오는 함수
    useEffect(() => {
        const fetchCardData = async () => {
            try {
                const response = await apiClient.get(`/api/cards`, {
                    headers: {
                        "Content-Type": "application/json"
                    },
                    credentials: 'include',
                });

                const { info, images, tags } = response.data;
                setIntroduction(info);
                setSelectedImages(images);
                setSelectedTags(tags);
            } catch (error) {
                toast.dismiss();
                toast.error('카드 정보를 불러오는 데 실패했습니다.');
            } finally {
                setIsLoading(false); // 데이터 로드 완료 후 로딩 상태 해제
            }
        };

        fetchCardData();
    }, [cardId]);

    const handleOptionChange = (e) => {
        setSelectedOption(e.target.value);
    };

    const handleFileChange = (e, index) => {
        const file = e.target.files[0];

        if (file && file.size) {
            const maxFileSize = 5 * 1024 * 1024; // 5MB 사이즈 제한

            if (file.size > maxFileSize) {
                toast.dismiss();
                toast.warn("5MB 이하의 이미지만 업로드할 수 있습니다.");
                return;
            }

            setSelectedFiles((prevFiles) => {
                const newFiles = [...prevFiles];
                newFiles[index] = file;
                return newFiles;
            });
        }
    };

    const handleRemoveFile = (index) => {
        setSelectedFiles((prevFiles) => {
            const newFiles = [...prevFiles];
            newFiles[index] = null;
            return newFiles;
        });
    };

    const handleTagChange = (e) => {
        const { value, checked } = e.target;
        if (checked) {
            if (selectedTags.length < MAX_TAGS) {
                setSelectedTags((prevTags) => [...prevTags, value]);
            } else {
                e.target.checked = false;
                toast.dismiss();
                toast.warn(`태그는 최대 ${MAX_TAGS}개까지 선택할 수 있습니다.`);
            }
        } else {
            setSelectedTags((prevTags) => prevTags.filter(tag => tag !== value));
        }
    };

    const handleIntroductionChange = (e) => {
        setIntroduction(e.target.value);
        setCharCount(e.target.value.length);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (selectedTags.length < 5) {
            toast.dismiss();
            toast.error("태그를 5개 선택해야 합니다.");
            return;
        }

        const formData = new FormData();
        const jsonData = {
            cardId,
            info: introduction,
            tags: selectedTags,
            imageIndexes: [],
        };

        selectedFiles.forEach((file, index) => {
            if (file) {
                formData.append('images', file);
                jsonData.imageIndexes.push(index);
            }
        });

        const blob = new Blob([JSON.stringify(jsonData)], { type: 'application/json' });
        formData.append('cardRequest', blob);

        try {
            const response = await apiClient.put(`/api/cards/${cardId}`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
                timeout: 30000, // 타임아웃을 30초로 설정
            });

            toast.dismiss();
            toast.success('카드 수정 성공');
            navigate('/');
        } catch (error) {
            if (error.response && error.response.status === 413) {
                toast.dismiss();
                toast.error("업로드 가능한 이미지 용량을 초과하여 카드 수정에 실패했습니다.");
            } else {
                toast.dismiss();
                toast.error("카드 수정에 실패했습니다.");
            }
        }
    };

    const tagOptions = [
        "ENFP", "ENTP", "ENFJ", "ENTJ", "ESTP", "ESFP", "ESTJ", "ESFJ", "INFJ", "INTJ",
        "INFP", "INTP", "ISTJ", "ISFJ", "ISTP", "ISFP", "융통성 있는", "단순한", "침착한", 
        "의지가 강한", "아이디어가 많은", "꼼꼼한", "변덕스러운", "열정적인", "전략적인", "대인관계가 원만한", "변화를 즐기는", "모임을 즐기는", "유연한", "독창적인", "신뢰할 수 있는", "체계적인", "설득력 있는", "느긋한", "겸손한", "동정심이 많은", "호기심 많은", "생기발랄한",
        "미래지향적인", "활동적인", "완벽을 추구하는", "계획적인", "집중력 있는", "대인관계가 넓은", "신중한", "적응력이 뛰어난", "수용적인", "끈기 있는",
        "소신 있는", "지도력 있는", "지혜로운", "결단력 있는", "충동적인", "혁신적인", "엄격한", "현재에 충실한", "단호한",
        "분석적인", "유머러스한", "경청하는", "언변이 뛰어난", "우유부단한", "인심 좋은", "절제력 있는", "사교적인", "이성적인", "이해가 빠른",
        "창의적인", "성실한", "현실적인", "협조적인", "안정적인", "실행력 있는", "통찰력 있는", "독립적인", "온화한", "도전적인",
        "개척적인", "주도적인", "인내심 있는", "논리적인", "감성적인", "즐거움을 찾는", "에너지 있는", "이해심 많은", "인기 있는", "친절한",
        "활발한", "조용한", "외향적인", "내향적인", "모험적인", "이상적인", "대담한", "진취적인", "차분한", "직설적인", "정직한", "긍정적인", "책임감 있는"
    ];

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
        <div className={styles.createcard}>
            <ToastContainer 
                hideProgressBar={true}
                closeOnClick
                autoClose={700}
                limit={1}
                position="top-center"
            />
            <form onSubmit={handleSubmit}>
                <div className={styles.optionContainer}>
                    <input 
                        type="radio" 
                        id="option1" 
                        name="cardOption" 
                        value="option1" 
                        checked={selectedOption === 'option1'}
                        onChange={handleOptionChange}
                        className={styles.hiddenInput}
                    />
                    <label 
                        htmlFor="option1" 
                        className={`${styles.optionLabel} ${selectedOption === 'option1' ? styles.active : ''}`}
                    >
                        이미지
                    </label>
                    <input 
                        type="radio" 
                        id="option2" 
                        name="cardOption" 
                        value="option2" 
                        checked={selectedOption === 'option2'}
                        onChange={handleOptionChange} 
                        className={styles.hiddenInput}
                    />
                    <label 
                        htmlFor="option2" 
                        className={`${styles.optionLabel} ${selectedOption === 'option2' ? styles.active : ''}`}
                    >
                        소개글
                    </label>
                </div>
                {selectedOption === 'option1' && (
                    <div>
                        <div className={styles.cardcard}>
                            {[0, 1, 2].map((index) => (
                                <div className={styles.tmptmp} key={index} style={{ marginBottom: '10px' }}>
                                    <div className={styles.tmprelative}>
                                        <input 
                                            type="file"
                                            accept="image/*"
                                            id={`imageUpload${index}`}
                                            name={`imageUpload${index}`}
                                            key={selectedFiles[index] ? selectedFiles[index].name : ""}
                                            onChange={(e) => handleFileChange(e, index)} 
                                            style={{ display: "none" }}
                                        />
                                        {!selectedFiles[index] && (
                                            <img src={selectedImages[index]} alt="No Picture" style={{ width: '149.33px', height: '214.63px', objectFit: 'cover', marginRight: '10px', borderRadius: '12px' }} />
                                        )}
                                        {!selectedFiles[index] && (
                                            <label className={styles.labellabel} htmlFor={`imageUpload${index}`}>
                                                <img className={styles.addpic} src={`${process.env.REACT_APP_BASE_URL}/assets/addpicture.png`}/>
                                            </label>
                                        )}
                                        {selectedFiles[index] && (
                                            <div>
                                                <img 
                                                    src={URL.createObjectURL(selectedFiles[index])} 
                                                    alt={`preview ${index}`} 
                                                    style={{ width: '149.33px', height: '214.63px', objectFit: 'cover', marginRight: '10px' }}
                                                    className={styles.imgimg}
                                                />
                                                <div className={styles.labellabel}>
                                                    <button className={styles.deletepicture} type="button" onClick={() => handleRemoveFile(index)}> 
                                                        <img className={styles.deletepic} src={`${process.env.REACT_APP_BASE_URL}/assets/deletepicture.png`}/>
                                                    </button>
                                                </div>
                                            </div>
                                        )}
                                    </div>
                                </div>
                            ))}
                        </div>
                        <div className={styles.containerbtn}>
                            <button className={styles.nextbtn} onClick={() => setSelectedOption('option2')}>다음으로</button>
                        </div>
                    </div>
                )}
                {selectedOption === 'option2' && (
                    <div>
                        <div className={styles.tagtext}>
                            <p>나와 어울리는 태그 5가지를 선택해 주세요</p>
                            <div className={styles.tagtag} style={{ display: 'flex', flexWrap: 'wrap' }}>
                                {tagOptions.map((tag, index) => (
                                    <div className={styles.tagbox} key={index}>
                                        <input
                                            type="checkbox"
                                            id={`tag${index}`}
                                            value={tag}
                                            onChange={handleTagChange}
                                            checked={selectedTags.includes(tag)}
                                            className={styles.hiddenCheckbox}
                                        />
                                        <label
                                            htmlFor={`tag${index}`}
                                            className={`${styles.labeltag} ${selectedTags.includes(tag) ? styles.checked : ''}`}
                                        >
                                            {tag}
                                        </label>
                                    </div>
                                ))}
                            </div>
                            <p>나를 한줄로 소개해 보세요</p>
                            <textarea
                                value={introduction}
                                onChange={handleIntroductionChange}
                                maxLength={100}
                                rows={3}
                                className={styles.textareaCustom}
                            />
                            <div className={styles.charCount}>{charCount}/100</div>
                        </div>
                        <div className={styles.containerbtn}>
                            <button className={styles.nextbtn} type="submit">저장하기</button>
                        </div>
                    </div>
                )}
            </form>
        </div>
    );
};

export default UpdateCard;
