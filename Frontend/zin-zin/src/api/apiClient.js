import axios from 'axios';

const apiClient = axios.create({
  baseURL: `${process.env.REACT_APP_BASE_URL}`,  // API의 기본 URL
  headers: {
    'Authorization': `Bearer ${sessionStorage.getItem('accessToken')}` // 초기 액세스 토큰 설정
  }
});

apiClient.interceptors.response.use(
  response => response,  // 성공적인 응답은 그대로 반환
  async error => {
    const originalRequest = error.config;

    // 401 오류 및 'T005' 코드 확인
    if (error.response.status === 401 && error.response.data.code === 'T005') {
      try {
        const response = await axios.get('/api/auth/refresh', {
          headers: {
            'Authorization': `Bearer ${sessionStorage.getItem('accessToken')}`
          }
        });

        // 새로운 액세스 토큰 저장
        sessionStorage.setItem('accessToken', response.data.token);

        // 원래 요청에 새로운 토큰을 추가하고 다시 요청
        originalRequest.headers['Authorization'] = `Bearer ${response.data.token}`;
        return axios(originalRequest);
      } catch (err) {
        console.error('Token refresh failed:', err);
        
        // 세션 정보 삭제
        sessionStorage.clear();

        // /login 페이지로 리디렉션
        window.location.href = '/login';

        return Promise.reject(err);
      }
    }

    return Promise.reject(error);
  }
);

export default apiClient;
