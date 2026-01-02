package com.abdou.microblogging.like.dto;

public record LikeDetailsDto(int count, boolean liked) {

    public static LikeDetailsDto toDto(int count) {
        return new LikeDetailsDto(count, false);
    }

    public static LikeDetailsDto toDto(int count, boolean liked) {
        return new LikeDetailsDto(count, liked);
    }
}
