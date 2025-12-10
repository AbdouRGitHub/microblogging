package com.abdou.microblogging.message;

import com.abdou.microblogging.account.Account;
import com.abdou.microblogging.account.AccountRepository;
import com.abdou.microblogging.common.exceptions.PostNotFoundException;
import com.abdou.microblogging.message.dto.MessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    MessageService(MessageRepository messageRepository,
                   AccountRepository accountRepository
    ) {
        this.messageRepository = messageRepository;
    }

    public ResponseEntity<Message> createPost(Account account,
                                              MessageDto messageDto
    ) {
        Message message = new Message(messageDto.content(), account);
        Message saved = messageRepository.save(message);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    public ResponseEntity<PagedModel<MessageDto>> getPaginatedPosts(int page) {
        Page<Message> posts =
                messageRepository.findLatestPosts(Pageable.ofSize(10)
                        .withPage(page - 1));
        return ResponseEntity.ok()
                .body(new PagedModel<>(posts.map(MessageDto::toPostResponseDto)));
    }

    public ResponseEntity<PagedModel<MessageDto>> getPaginatedComments(UUID postId,
                                                                       int page
    ) {
        Page<Message> posts =
                messageRepository.findLatestComments(Pageable.ofSize(10)
                        .withPage(page - 1), postId);
        return ResponseEntity.ok()
                .body(new PagedModel<>(posts.map(MessageDto::toPostResponseDto)));
    }

    public ResponseEntity<MessageDto> getPostInfo(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        return ResponseEntity.ok(MessageDto.toPostResponseDto(message));
    }

    public ResponseEntity<Message> updatePost(MessageDto messageDto, UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        if (messageDto.content() != null) {
            message.setContent(messageDto.content());
        }

        return ResponseEntity.ok(messageRepository.save(message));
    }

    public ResponseEntity<?> deletePost(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        messageRepository.delete(message);
        return ResponseEntity.ok().build();
    }
}
