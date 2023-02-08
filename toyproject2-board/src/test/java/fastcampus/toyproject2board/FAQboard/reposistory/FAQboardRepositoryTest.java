package fastcampus.toyproject2board.FAQboard.reposistory;


import fastcampus.toyproject2board.FQAboard.domain.FAQboard;
import fastcampus.toyproject2board.FQAboard.repository.FAQBoardRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class FAQboardRepositoryTest {
    @Autowired
    private FAQBoardRepository faqBoardRepository;

    @Test
    public void testInsert(){
        IntStream.rangeClosed(1,10).forEach(i ->{
            FAQboard faQboard = FAQboard.builder()
                    .FAQtype("결제/취소")
                    .title("title..."+i)
                    .content("content..."+i)
                    .id("user"+i)
                    .build();

        FAQboard result = faqBoardRepository.save(faQboard);
        log.info("tno: "+result.getTno());
        });

    }

    @Test
    public void testSelect(){
        Long tno = 5L;
        Optional<FAQboard> result = faqBoardRepository.findById(tno);
        FAQboard faQboard = result.orElseThrow();
        log.info(faQboard);
    }

    @Test
    public void testUpdate(){
        Long tno =5L;
        Optional<FAQboard> result = faqBoardRepository.findById(tno);
        FAQboard faQboard = result.orElseThrow();
        faQboard.change("update title 5","update content5");
        faqBoardRepository.save(faQboard);
        log.info(faQboard);
    }

    @Test
    public void testDelete(){
        Long tno =10L;
        faqBoardRepository.deleteById(tno);
    }
}
