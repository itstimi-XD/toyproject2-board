package fastcampus.toyproject2board.noticeBoard.dto.request;

import fastcampus.toyproject2board.noticeBoard.dto.NoticeCommentDto;
import fastcampus.toyproject2board.noticeBoard.dto.UserAccountDto;

public record NoticeCommentRequest(Long noticeId, String content) {

    public static NoticeCommentRequest of(Long noticeId, String content) {
        return new NoticeCommentRequest(noticeId, content);
    }

    public NoticeCommentDto toDto(UserAccountDto userAccountDto) {
        return NoticeCommentDto.of(
                noticeId,
                userAccountDto,
                content
        );
    }

}
