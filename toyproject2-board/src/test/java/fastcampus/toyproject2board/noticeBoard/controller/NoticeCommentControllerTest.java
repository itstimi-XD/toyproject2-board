package fastcampus.toyproject2board.noticeBoard.controller;

import fastcampus.toyproject2board.noticeBoard.config.TestSecurityConfig;
import fastcampus.toyproject2board.noticeBoard.dto.NoticeCommentDto;
import fastcampus.toyproject2board.noticeBoard.dto.request.NoticeCommentRequest;
import fastcampus.toyproject2board.noticeBoard.service.NoticeCommentService;
import fastcampus.toyproject2board.noticeBoard.util.FormDataEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 댓글")
@Import({TestSecurityConfig.class, FormDataEncoder.class})
@WebMvcTest(NoticeCommentController.class)
class NoticeCommentControllerTest {

    private final MockMvc mvc;

    private final FormDataEncoder formDataEncoder;

    @MockBean private NoticeCommentService noticeCommentService;


    public NoticeCommentControllerTest(
            @Autowired MockMvc mvc,
            @Autowired FormDataEncoder formDataEncoder
    ) {
        this.mvc = mvc;
        this.formDataEncoder = formDataEncoder;
    }


    @WithUserDetails(value = "unoTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 댓글 등록 - 정상 호출")
    @Test
    void givenNoticeCommentInfo_whenRequesting_thenSavesNewNoticeComment() throws Exception {
        // Given
        long noticeId = 1L;
        NoticeCommentRequest request = NoticeCommentRequest.of(noticeId, "test comment");
        willDoNothing().given(noticeCommentService).saveNoticeComment(any(NoticeCommentDto.class));

        // When & Then
        mvc.perform(
                post("/comments/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(formDataEncoder.encode(request))
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/notices/" + noticeId))
                .andExpect(redirectedUrl("/notices/" + noticeId));
        then(noticeCommentService).should().saveNoticeComment(any(NoticeCommentDto.class));
    }

    @WithUserDetails(value = "unoTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][GET] 댓글 삭제 - 정상 호출")
    @Test
    void givenNoticeCommentIdToDelete_whenRequesting_thenDeletesNoticeComment() throws Exception {
        // Given
        long noticeId = 1L;
        long noticeCommentId = 1L;
        String userId = "unoTest";
        willDoNothing().given(noticeCommentService).deleteNoticeComment(noticeCommentId, userId);

        // When & Then
        mvc.perform(
                post("/comments/" + noticeCommentId + "/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(formDataEncoder.encode(Map.of("noticeId", noticeId)))
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/notices/" + noticeId))
                .andExpect(redirectedUrl("/notices/" + noticeId));
        then(noticeCommentService).should().deleteNoticeComment(noticeCommentId, userId);
    }

}
