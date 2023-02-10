package fastcampus.toyproject2board.FAQboard.service;

import fastcampus.toyproject2board.FQAboard.dto.FAQboardDTO;
import fastcampus.toyproject2board.FQAboard.service.FAQboardService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
public class FAQboardServiceTest {
    @Autowired
    private FAQboardService faQboardService;

    @Test
    public void testRegister(){
        log.info(faQboardService.getClass().getName());
        FAQboardDTO faQboardDTO = FAQboardDTO.builder()
                .FAQtype("결제/취소")
                .title("RegisterTest")
                .content("RegisterTest")
                .id("RegisterTest")
                .build();
        Long tno = faQboardService.register(faQboardDTO);
        log.info("tno: "+tno);
    }
    @Test
    public void testReadAll(){
        List<FAQboardDTO> faQboardDTOList =faQboardService.readAll();
        log.info(faQboardDTOList);
    }
    @Test
    public void testReadOne(){
        FAQboardDTO faQboardDTO =faQboardService.readOne(11L);
        log.info(faQboardDTO);
    }
    @Test
    public void testModify(){
        FAQboardDTO faQboardDTO = FAQboardDTO.builder()
                .tno(9L)
                .FAQtype("이용문의")
                .title("ModifyTest")
                .content("ModifyTest")
                .id("ModifyTest")
                .build();
        faQboardService.modify(faQboardDTO);
    }
    @Test
    public void testRemove(){
        faQboardService.remove(8L);
    }
}
