package com.abdou.microblogging.message;

import com.abdou.microblogging.account.Account;
import com.abdou.microblogging.message.dto.MessageDto;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class MessageController {
    private final MessageService messageService;
    
    MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping()
    public ResponseEntity<Message> createPost(@AuthenticationPrincipal Account account, @RequestBody MessageDto messageDto) {
        return messageService.createPost(account, messageDto);
    }

    @GetMapping()
    public ResponseEntity<PagedModel<MessageDto>> getPaginatedPosts(@RequestParam(defaultValue = "1") int page) {
        return messageService.getPaginatedPosts(page);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<PagedModel<MessageDto>> getPaginatedComments(@PathVariable("id") UUID postId, @RequestParam(defaultValue = "1") int page) {
        return messageService.getPaginatedComments(postId, page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageDto> getPostInfo(@PathVariable UUID id) {
        return messageService.getPostInfo(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Message> updatePost(@RequestBody MessageDto messageDto, @PathVariable UUID id) {
        return messageService.updatePost(messageDto, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable UUID id) {
        return messageService.deletePost(id);
    }
}
