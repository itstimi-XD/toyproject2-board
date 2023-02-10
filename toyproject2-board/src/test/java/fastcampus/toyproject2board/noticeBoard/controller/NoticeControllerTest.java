package fastcampus.toyproject2board.noticeBoard.controller;

import fastcampus.toyproject2board.noticeBoard.config.TestSecurityConfig;
import fastcampus.toyproject2board.noticeBoard.domain.constant.FormStatus;
import fastcampus.toyproject2board.noticeBoard.domain.constant.SearchType;
import fastcampus.toyproject2board.noticeBoard.dto.NoticeDto;
import fastcampus.toyproject2board.noticeBoard.dto.NoticeWithCommentsDto;
import fastcampus.toyproject2board.noticeBoard.dto.UserAccountDto;
import fastcampus.toyproject2board.noticeBoard.dto.request.NoticeRequest;
import fastcampus.toyproject2board.noticeBoard.dto.response.NoticeResponse;
import fastcampus.toyproject2board.noticeBoard.service.NoticeService;
import fastcampus.toyproject2board.noticeBoard.service.PaginationService;
import fastcampus.toyproject2board.noticeBoard.util.FormDataEncoder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시글")
@Import({TestSecurityConfig.class, FormDataEncoder.class})
@WebMvcTest(NoticeController.class)
class NoticeControllerTest {

    private final MockMvc mvc;

    private final FormDataEncoder formDataEncoder;

    @MockBean private NoticeService noticeService;
    @MockBean private PaginationService paginationService;


    public NoticeControllerTest(
            @Autowired MockMvc mvc,
            @Autowired FormDataEncoder formDataEncoder
    ) {
        this.mvc = mvc;
        this.formDataEncoder = formDataEncoder;
    }


    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingNoticesView_thenReturnsNoticesView() throws Exception {
        // Given
        given(noticeService.searchNotices(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));

        // When & Then
        mvc.perform(get("/notices"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("notices/index"))
                .andExpect(model().attributeExists("notices"))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attributeExists("searchTypes"));
        then(noticeService).should().searchNotices(eq(null), eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 검색어와 함께 호출")
    @Test
    public void givenSearchKeyword_whenSearchingNoticesView_thenReturnsNoticesView() throws Exception {
        // Given
        SearchType searchType = SearchType.TITLE;
        String searchValue = "title";
        given(noticeService.searchNotices(eq(searchType), eq(searchValue), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));

        // When & Then
        mvc.perform(
                get("/notices")
                        .queryParam("searchType", searchType.name())
                        .queryParam("searchValue", searchValue)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("notices/index"))
                .andExpect(model().attributeExists("notices"))
                .andExpect(model().attributeExists("searchTypes"));
        then(noticeService).should().searchNotices(eq(searchType), eq(searchValue), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 페이징, 정렬 기능")
    @Test
    void givenPagingAndSortingParams_whenSearchingNoticesView_thenReturnsNoticesView() throws Exception {
        // Given
        String sortName = "title";
        String direction = "desc";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1, 2, 3, 4, 5);
        given(noticeService.searchNotices(null, null, pageable)).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages())).willReturn(barNumbers);

        // When & Then
        mvc.perform(
                        get("/notices")
                                .queryParam("page", String.valueOf(pageNumber))
                                .queryParam("size", String.valueOf(pageSize))
                                .queryParam("sort", sortName + "," + direction)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("notices/index"))
                .andExpect(model().attributeExists("notices"))
                .andExpect(model().attribute("paginationBarNumbers", barNumbers));
        then(noticeService).should().searchNotices(null, null, pageable);
        then(paginationService).should().getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages());
    }

    @DisplayName("[view][GET] 게시글 페이지 - 인증 없을 땐 로그인 페이지로 이동")
    @Test
    void givenNothing_whenRequestingNoticePage_thenRedirectsToLoginPage() throws Exception {
        // Given
        long noticeId = 1L;

        // When & Then
        mvc.perform(get("/notices/" + noticeId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
        then(noticeService).shouldHaveNoInteractions();
        then(noticeService).shouldHaveNoInteractions();
    }

    @WithMockUser
    @DisplayName("[view][GET] 게시글 페이지 - 정상 호출, 인증된 사용자")
    @Test
    public void givenNothing_whenRequestingNoticeView_thenReturnsNoticeView() throws Exception {
        // Given
        Long noticeId = 1L;
        long totalCount = 1L;
        given(noticeService.getNoticeWithComments(noticeId)).willReturn(createNoticeWithCommentsDto());
        given(noticeService.getNoticeCount()).willReturn(totalCount);

        // When & Then
        mvc.perform(get("/notices/" + noticeId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("notices/detail"))
                .andExpect(model().attributeExists("notice"))
                .andExpect(model().attributeExists("noticeComments"))
                .andExpect(model().attributeExists("noticeComments"))
                .andExpect(model().attribute("totalCount", totalCount));
        then(noticeService).should().getNoticeWithComments(noticeId);
        then(noticeService).should().getNoticeCount();
    }

    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingNoticeSearchView_thenReturnsNoticeSearchView() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/notices/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("notices/search"));
    }

    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingNoticeSearchHashtagView_thenReturnsNoticeSearchHashtagView() throws Exception {
        // Given
        List<String> hashtags = List.of("#java", "#spring", "#boot");
        given(noticeService.searchNoticesViaHashtag(eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(noticeService.getHashtags()).willReturn(hashtags);
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(1, 2, 3, 4, 5));

        // When & Then
        mvc.perform(get("/notices/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("notices/search-hashtag"))
                .andExpect(model().attribute("notices", Page.empty()))
                .andExpect(model().attribute("hashtags", hashtags))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attribute("searchType", SearchType.HASHTAG));
        then(noticeService).should().searchNoticesViaHashtag(eq(null), any(Pageable.class));
        then(noticeService).should().getHashtags();
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출, 해시태그 입력")
    @Test
    public void givenHashtag_whenRequestingNoticeSearchHashtagView_thenReturnsNoticeSearchHashtagView() throws Exception {
        // Given
        String hashtag = "#java";
        List<String> hashtags = List.of("#java", "#spring", "#boot");
        given(noticeService.searchNoticesViaHashtag(eq(hashtag), any(Pageable.class))).willReturn(Page.empty());
        given(noticeService.getHashtags()).willReturn(hashtags);
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(1, 2, 3, 4, 5));

        // When & Then
        mvc.perform(
                get("/notices/search-hashtag")
                        .queryParam("searchValue", hashtag)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("notices/search-hashtag"))
                .andExpect(model().attribute("notices", Page.empty()))
                .andExpect(model().attribute("hashtags", hashtags))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attribute("searchType", SearchType.HASHTAG));
        then(noticeService).should().searchNoticesViaHashtag(eq(hashtag), any(Pageable.class));
        then(noticeService).should().getHashtags();
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @WithMockUser
    @DisplayName("[view][GET] 새 게시글 작성 페이지")
    @Test
    void givenNothing_whenRequesting_thenReturnsNewNoticePage() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/notices/form"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("notices/form"))
                .andExpect(model().attribute("formStatus", FormStatus.CREATE));
    }

    @WithUserDetails(value = "unoTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 새 게시글 등록 - 정상 호출")
    @Test
    void givenNewNoticeInfo_whenRequesting_thenSavesNewNotice() throws Exception {
        // Given
        NoticeRequest noticeRequest = NoticeRequest.of("new title", "new content", "#new");
        willDoNothing().given(noticeService).saveNotice(any(NoticeDto.class));

        // When & Then
        mvc.perform(
                post("/notices/form")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(formDataEncoder.encode(noticeRequest))
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/notices"))
                .andExpect(redirectedUrl("/notices"));
        then(noticeService).should().saveNotice(any(NoticeDto.class));
    }

    @DisplayName("[view][GET] 게시글 수정 페이지 - 인증 없을 땐 로그인 페이지로 이동")
    @Test
    void givenNothing_whenRequesting_thenRedirectsToLoginPage() throws Exception {
        // Given
        long noticeId = 1L;

        // When & Then
        mvc.perform(get("/notices/" + noticeId + "/form"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
        then(noticeService).shouldHaveNoInteractions();
    }

    @WithMockUser
    @DisplayName("[view][GET] 게시글 수정 페이지 - 정상 호출, 인증된 사용자")
    @Test
    void givenNothing_whenRequesting_thenReturnsUpdatedNoticePage() throws Exception {
        // Given
        long noticeId = 1L;
        NoticeDto dto = createNoticeDto();
        given(noticeService.getNotice(noticeId)).willReturn(dto);

        // When & Then
        mvc.perform(get("/notices/" + noticeId + "/form"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("notices/form"))
                .andExpect(model().attribute("notice", NoticeResponse.from(dto)))
                .andExpect(model().attribute("formStatus", FormStatus.UPDATE));
        then(noticeService).should().getNotice(noticeId);
    }

    @WithUserDetails(value = "unoTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 게시글 수정 - 정상 호출")
    @Test
    void givenUpdatedNoticeInfo_whenRequesting_thenUpdatesNewNotice() throws Exception {
        // Given
        long noticeId = 1L;
        NoticeRequest noticeRequest = NoticeRequest.of("new title", "new content", "#new");
        willDoNothing().given(noticeService).updateNotice(eq(noticeId), any(NoticeDto.class));

        // When & Then
        mvc.perform(
                post("/notices/" + noticeId + "/form")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(formDataEncoder.encode(noticeRequest))
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/notices/" + noticeId))
                .andExpect(redirectedUrl("/notices/" + noticeId));
        then(noticeService).should().updateNotice(eq(noticeId), any(NoticeDto.class));
    }

    @WithUserDetails(value = "unoTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 게시글 삭제 - 정상 호출")
    @Test
    void givenNoticeIdToDelete_whenRequesting_thenDeletesNotice() throws Exception {
        // Given
        long noticeId = 1L;
        String userId = "unoTest";
        willDoNothing().given(noticeService).deleteNotice(noticeId, userId);

        // When & Then
        mvc.perform(
                post("/notices/" + noticeId + "/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/notices"))
                .andExpect(redirectedUrl("/notices"));
        then(noticeService).should().deleteNotice(noticeId, userId);
    }


    private NoticeDto createNoticeDto() {
        return NoticeDto.of(
                createUserAccountDto(),
                "title",
                "content",
                "#java"
        );
    }

    private NoticeWithCommentsDto createNoticeWithCommentsDto() {
        return NoticeWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "uno",
                "pw",
                "uno@mail.com",
                "Uno",
                "memo",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }

}
