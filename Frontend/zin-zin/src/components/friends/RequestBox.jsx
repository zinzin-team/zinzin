import React from 'react';
import styles from './Friends.module.css';

const RequestBox = ({ requests }) => {
  return (
    <div className={styles.requestBox}>
      {requests.map(request => (
        <div key={request.id} className={styles.requestItem}>
          <img src={request.profilePicture} alt={`${request.name} 프로필`} className={styles.profilePicture} />
          <div className={styles.requestInfo}>
            <span>{request.name}</span>
            <button className={styles.requestButton}>{request.buttonLabel}</button>
          </div>
        </div>
      ))}
    </div>
  );
};

export default RequestBox;
