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

/* eslint-env browser, serviceworker, es6 */

"use strict";

// sw.js 파일은 서비스워커의 동작을 정의한 파일이다.
// 이 파일에서 self 객체는 서비스워커를 의미한다.

// push 이벤트의 이벤트 리스너를 작성하여 서버에서 푸시 요청이 왔을 때 알림을 띄운다.

self.addEventListener("push", function (event) {
  console.log("[Service Worker] Push Received.");
  console.log(`[Service Worker] Push had this data: "${event.data.text()}"`);

  const title = "대충 제목 적는 곳";
  const options = {
    body: "대충 내용 적는 곳",
    icon: "images/icon.png",
    badge: "images/badge.png",
  };

  event.waitUntil(self.registration.showNotification(title, options));
});
