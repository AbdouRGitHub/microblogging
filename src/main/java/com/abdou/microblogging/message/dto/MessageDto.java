package com.abdou.microblogging.message.dto;

import com.abdou.microblogging.account.dto.AccountDto;
import com.abdou.microblogging.message.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MessageDto(
        UUID id,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        AccountDto account
) {
    public static MessageDto toPostResponseDto(Message message) {
        return new MessageDto(message.getId(),
                message.getContent(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                AccountDto.toAccountDto(message.getAccount()));
    }

    public static List<MessageDto> toPostResponseDtoList(List<Message> messages) {
        return messages.stream().map(MessageDto::toPostResponseDto).toList();
    }
}
