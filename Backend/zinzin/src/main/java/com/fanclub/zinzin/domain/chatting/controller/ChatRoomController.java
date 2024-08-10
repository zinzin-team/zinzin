package com.fanclub.zinzin.domain.chatting.controller;

import com.fanclub.zinzin.domain.chatting.dto.CreateChatRoomDto;
import com.fanclub.zinzin.domain.chatting.dto.ResponseChatRoomDto;
import com.fanclub.zinzin.domain.chatting.dto.ResponseMessageDto;
import com.fanclub.zinzin.domain.chatting.service.ChatRoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping
    public ResponseEntity<List<ResponseChatRoomDto>> getRooms(HttpServletRequest request) {
        return ResponseEntity.ok(chatRoomService.getChatRoomsByMemberId((Long) request.getAttribute("memberId")));
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<ResponseMessageDto>> getRoomMessages(HttpServletRequest request, @PathVariable Long roomId) {
        List<ResponseMessageDto> messages = chatRoomService.getRoomMessages(roomId, (Long) request.getAttribute("memberId"));
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseChatRoomDto> createChatRoom(HttpServletRequest request, @RequestBody CreateChatRoomDto chatRoomDto) {
        return ResponseEntity.ok(chatRoomService.createAndFetchChatRoom(chatRoomDto));
    }

    @DeleteMapping("/{roomId}/exit")
    public ResponseEntity<String> deleteChatRoom(HttpServletRequest request, @PathVariable Long roomId) {
        chatRoomService.deleteChatRoom(roomId, (Long) request.getAttribute("memberId"));
        return ResponseEntity.ok("채팅방 삭제 완료");
    }

    @PutMapping("/{roomId}/heart")
    public ResponseEntity<?> updateHeart(HttpServletRequest request, @PathVariable Long roomId, @RequestBody boolean isHeart) {
        chatRoomService.updateHeartToggle((Long) request.getAttribute("memberId"), roomId, isHeart);
        return ResponseEntity.ok("토글 업데이트 성공");
    }
}
