/*
 *
 *  Push Notifications codelab
 *  Copyright 2015 Google Inc. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 *
 */

/* eslint-env browser, es6 */

"use strict";

const applicationServerPublicKey =
  "BGrRfCWTVgdljiKRh4WDsenFh_17zNWW-SqWgjnhvAIPbEl1R3KVugkCHlV3TLoxo2Sjqp35al9_Lr9OWqHDnl4"; // ooFrRJSdu0zDtGzfT0nDSG02UDFGnGuoW3a_YYC02vo

const pushButton = document.querySelector(".js-push-btn");

let isSubscribed = false;
let swRegistration = null;

function urlB64ToUint8Array(base64String) {
  const padding = "=".repeat((4 - (base64String.length % 4)) % 4);
  const base64 = (base64String + padding).replace(/\-/g, "+").replace(/_/g, "/");

  const rawData = window.atob(base64);
  const outputArray = new Uint8Array(rawData.length);

  for (let i = 0; i < rawData.length; ++i) {
    outputArray[i] = rawData.charCodeAt(i);
  }

  return outputArray;
}

async function registerServiceWorker() {
  // 브라우저가 서비스워커를 지원하는지 확인하고, 지원하지 않는다면 함수를 종료한다.
  if (!("serviceWorker" in navigator)) return;

  console.log("Service Worker and Push are supported");

  // getRegistration 함수를 통해 서비스워커의 등록정보를 확인한다.
  swRegistration = await navigator.serviceWorker.getRegistration();

  if (!swRegistration) {
    // 서비스워커가 등록되지 않았다면, 서비스워커를 등록한다.
    swRegistration = await navigator.serviceWorker.register("sw.js");
    console.log("Service Worker is registered", swRegistration);
  } else {
    console.log("Service Worker is already registered", swRegistration);
  }

  initializeUI();
}

async function initializeUI() {
  // pushButton element에 클릭 이벤트를 달아준다.
  pushButton.addEventListener("click", function () {
    pushButton.disabled = true;

    if (isSubscribed) {
      unsubscribeUser();
    } else {
      // 구독하지 않았다면 브라우저를 구독한다.
      subscribeUser();
    }
  });

  // 푸시 서비스 구독정보를 확인하고 isSubscribed 변수에 저장한다.
  const subscription = await swRegistration.pushManager.getSubscription();
  isSubscribed = subscription;
  console.log(isSubscribed ? "User is subscribed." : "User is NOT subscribed.");
  updateBtn();
}

async function subscribeUser() {
  const applicationServerKey = urlB64ToUint8Array(applicationServerPublicKey);
  const subscription = await swRegistration.pushManager.subscribe({
    userVisibleOnly: true,
    applicationServerKey: applicationServerKey,
  });

  if (subscription) {
    // 구독이 되면 푸시 서비스를 통해 받은 구독 정보를 서버에 전송한다.
    updateSubscriptionOnServer(subscription);

    console.log("User is subscribed.");
    isSubscribed = true;

    updateBtn();
  } else {
    console.error("Failed to subscribe the user : ", error);
  }
}

function unsubscribeUser() {
  swRegistration.pushManager
    .getSubscription()
    .then(function (subscription) {
      if (subscription) {
        return subscription.unsubscribe();
      }
    })
    .catch(function (error) {
      console.log("Error unsubscribing", error);
    })
    .then(function () {
      updateSubscriptionOnServer(null);

      console.log("User is unsubscribed.");
      isSubscribed = false;

      updateBtn();
    });
}

function updateSubscriptionOnServer(subscription) {
  // 구독이 되면 푸시 서비스를 통해 받은 구독 정보를 서버에 전송한다.
  // TODO: Send subscription to application server

  // 지금은 서버를 구현하지 않았으므로 실제 서버 API를 호출하는 대신 구독 정보를 웹 사이트에 보여주도록 한다.
  const subscriptionJson = document.querySelector(".js-subscription-json");
  const subscriptionDetails = document.querySelector(".js-subscription-details");

  if (subscription) {
    subscriptionJson.textContent = JSON.stringify(subscription);
    subscriptionDetails.classList.remove("is-invisible");
  } else {
    subscriptionDetails.classList.add("is-invisible");
  }
}

function updateBtn() {
  // 푸시 기능을 사용하기 위해서는 브라우저의 알림 권한이 필요하다.
  // 버튼 클릭 시 알림 권한이 없다면 구독하지 못하도록 한다.
  if (Notification.permission === "denied") {
    pushButton.textContent = "Push Messaging Blocked";
    pushButton.disabled = true;
    updateSubscriptionOnServer(null);
    return;
  }

  if (isSubscribed) {
    pushButton.textContent = "Disable Push Messaging";
  } else {
    pushButton.textContent = "Enable Push Messaging";
  }

  pushButton.disabled = false;
}

registerServiceWorker();
