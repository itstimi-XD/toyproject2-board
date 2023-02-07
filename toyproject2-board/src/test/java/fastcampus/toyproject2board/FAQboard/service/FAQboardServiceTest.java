package fastcampus.toyproject2board.FAQboard.service;

import fastcampus.toyproject2board.FQAboard.dto.FAQboardDTO;
import fastcampus.toyproject2board.FQAboard.service.FAQboardService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class FAQboardServiceTest {
    @Autowired
    private FAQboardService faQboardService;

    @Test
    public void testRegister(){
        log.info(faQboardService.getClass().getName());
        FAQboardDTO faQboardDTO = FAQboardDTO.builder()
                .FAQtype("cancle")
                .title("RegisterTest")
                .content("RegisterTest")
                .id("RegisterTest")
                .build();
        Long tno = faQboardService.register(faQboardDTO);
        log.info("tno: "+tno);
    }
}
