package fastcampus.toyproject2board.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DisplayName("View 컨트롤러 - 게시글")
@WebMvcTest
class NoticeControllerTest {

    private final MockMvc mvc;

    public NoticeControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    public void View_Get_게시글_리스트_페이지_정상호출() throws Exception {

        mvc.perform(get("/notices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("notices"));
    }

    @Test
    public void View_Get_게시글_상세_페이지_정상호출() throws Exception {

        mvc.perform(get("/notices/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("notice"));
    }

    @Test
    public void View_Get_게시글_검색전용_페이지_정상호출() throws Exception {

        mvc.perform(get("/notices/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML));
    }

}