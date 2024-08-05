package com.fanclub.zinzin.domain.chatting.controller;

import com.fanclub.zinzin.domain.chatting.dto.ResponseMessageDto;
import com.fanclub.zinzin.domain.chatting.service.ChatRoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/chatRoom")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final SimpMessageSendingOperations template;

    @GetMapping("/{id}")
    public ResponseEntity<?> getRooms(HttpServletRequest request, @PathVariable Long id) {
//        return ResponseEntity.ok(chatService.getChatRoomsByMemberId((Long) request.getAttribute("memberId")));
        return ResponseEntity.ok(chatRoomService.getChatRoomsByMemberId(id));
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<ResponseMessageDto>> getRoomMessages(@PathVariable Long roomId) {
        List<ResponseMessageDto> messages = chatRoomService.getRoomMessages(roomId);

        return ResponseEntity.ok(messages);
    }

}
