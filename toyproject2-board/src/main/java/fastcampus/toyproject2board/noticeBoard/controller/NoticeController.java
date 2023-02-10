package fastcampus.toyproject2board.noticeBoard.controller;

import fastcampus.toyproject2board.noticeBoard.domain.constant.FormStatus;
import fastcampus.toyproject2board.noticeBoard.domain.constant.SearchType;
import fastcampus.toyproject2board.noticeBoard.dto.request.NoticeRequest;
import fastcampus.toyproject2board.noticeBoard.dto.response.NoticeResponse;
import fastcampus.toyproject2board.noticeBoard.dto.response.NoticeWithCommentsResponse;
import fastcampus.toyproject2board.noticeBoard.dto.security.BoardPrincipal;
import fastcampus.toyproject2board.noticeBoard.service.NoticeService;
import fastcampus.toyproject2board.noticeBoard.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/notices")
@Controller
public class NoticeController {

    private final NoticeService noticeService;
    private final PaginationService paginationService;

    @GetMapping
    public String notices(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ) {
        Page<NoticeResponse> notices = noticeService.searchNotices(searchType, searchValue, pageable).map(NoticeResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), notices.getTotalPages());

        map.addAttribute("notices", notices);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchTypes", SearchType.values());

        return "notices/index";
    }

    @GetMapping("/{noticeId}")
    public String notice(@PathVariable Long noticeId, ModelMap map) {
        NoticeWithCommentsResponse notice = NoticeWithCommentsResponse.from(noticeService.getNoticeWithComments(noticeId));

        map.addAttribute("notice", notice);
        map.addAttribute("noticeComments", notice.noticeCommentsResponse());
        map.addAttribute("totalCount", noticeService.getNoticeCount());

        return "notices/detail";
    }

    @GetMapping("/search-hashtag")
    public String searchNoticeHashtag(
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ) {
        Page<NoticeResponse> notices = noticeService.searchNoticesViaHashtag(searchValue, pageable).map(NoticeResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), notices.getTotalPages());
        List<String> hashtags = noticeService.getHashtags();

        map.addAttribute("notices", notices);
        map.addAttribute("hashtags", hashtags);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchType", SearchType.HASHTAG);

        return "notices/search-hashtag";
    }

    @GetMapping("/form")
    public String noticeForm(ModelMap map) {
        map.addAttribute("formStatus", FormStatus.CREATE);

        return "notices/form";
    }

    @PostMapping ("/form")
    public String postNewNotice(
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            NoticeRequest noticeRequest
    ) {
        noticeService.saveNotice(noticeRequest.toDto(boardPrincipal.toDto()));

        return "redirect:/notices";
    }

    @GetMapping("/{noticeId}/form")
    public String updateNoticeForm(@PathVariable Long noticeId, ModelMap map) {
        NoticeResponse notice = NoticeResponse.from(noticeService.getNotice(noticeId));

        map.addAttribute("notice", notice);
        map.addAttribute("formStatus", FormStatus.UPDATE);

        return "notices/form";
    }

    @PostMapping ("/{noticeId}/form")
    public String updateNotice(
            @PathVariable Long noticeId,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            NoticeRequest noticeRequest
    ) {
        noticeService.updateNotice(noticeId, noticeRequest.toDto(boardPrincipal.toDto()));

        return "redirect:/notices/" + noticeId;
    }

    @PostMapping ("/{noticeId}/delete")
    public String deleteNotice(
            @PathVariable Long noticeId,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal
    ) {
        noticeService.deleteNotice(noticeId, boardPrincipal.getUsername());

        return "redirect:/notices";
    }

}
