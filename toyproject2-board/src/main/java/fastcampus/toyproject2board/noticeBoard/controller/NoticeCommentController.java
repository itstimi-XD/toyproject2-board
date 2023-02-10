package fastcampus.toyproject2board.noticeBoard.controller;

import fastcampus.toyproject2board.noticeBoard.dto.request.NoticeCommentRequest;
import fastcampus.toyproject2board.noticeBoard.dto.security.BoardPrincipal;
import fastcampus.toyproject2board.noticeBoard.service.NoticeCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class NoticeCommentController {

    private final NoticeCommentService noticeCommentService;

    @PostMapping ("/new")
    public String postNewNoticeComment(
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            NoticeCommentRequest noticeCommentRequest
    ) {
        noticeCommentService.saveNoticeComment(noticeCommentRequest.toDto(boardPrincipal.toDto()));


        return "redirect:/notices/" + noticeCommentRequest.noticeId();
    }

    @PostMapping ("/{commentId}/delete")
    public String deleteNoticeComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            Long noticeId
    ) {
        noticeCommentService.deleteNoticeComment(commentId, boardPrincipal.getUsername());

        return "redirect:/notices/" + noticeId;
    }

}
