package fastcampus.toyproject2board.noticeBoard.dto.request;

import fastcampus.toyproject2board.noticeBoard.dto.NoticeDto;
import fastcampus.toyproject2board.noticeBoard.dto.UserAccountDto;

public record NoticeRequest(
        String title,
        String content,
        String hashtag
) {

    public static NoticeRequest of(String title, String content, String hashtag) {
        return new NoticeRequest(title, content, hashtag);
    }

    public NoticeDto toDto(UserAccountDto userAccountDto) {
        return NoticeDto.of(
                userAccountDto,
                title,
                content,
                hashtag
        );
    }

}
