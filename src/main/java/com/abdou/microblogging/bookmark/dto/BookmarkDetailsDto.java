package com.abdou.microblogging.bookmark.dto;

public record BookmarkDetailsDto(int count, boolean bookmarked) {
    public static BookmarkDetailsDto toDto(int count) {
        return new BookmarkDetailsDto(count, false);
    }

    public static BookmarkDetailsDto toDto(int count, boolean bookmarked) {
        return new BookmarkDetailsDto(count, bookmarked);
    }
}
