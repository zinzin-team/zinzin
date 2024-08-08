package com.fanclub.zinzin.domain.chatting.controller;

import com.fanclub.zinzin.domain.chatting.dto.CreateChatRoomDto;
import com.fanclub.zinzin.domain.chatting.dto.ResponseMessageDto;
import com.fanclub.zinzin.domain.chatting.service.ChatRoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatRoom")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping
    public ResponseEntity<?> getRooms(HttpServletRequest request) {
        return ResponseEntity.ok(chatRoomService.getChatRoomsByMemberId((Long) request.getAttribute("memberId")));
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<ResponseMessageDto>> getRoomMessages(@PathVariable Long roomId) {
        //  유효성 검증 필요
        List<ResponseMessageDto> messages = chatRoomService.getRoomMessages(roomId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/createRoom")
    public ResponseEntity<String> createChatRoom(@RequestBody CreateChatRoomDto chatRoomDto) {
        chatRoomService.createChatRoom(chatRoomDto);
        return ResponseEntity.ok("채팅방 생성 완료");
    }

    @DeleteMapping("/{roomId}/exit")
    public ResponseEntity<String> deleteChatRoom(HttpServletRequest request, @PathVariable Long roomId) {
        chatRoomService.deleteChatRoom(roomId, (Long) request.getAttribute("memberId"));
        return ResponseEntity.ok("채팅방 삭제 완료");
    }

}
