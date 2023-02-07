package fastcampus.toyproject2board.FQAboard.service;

import fastcampus.toyproject2board.FQAboard.dto.FAQboardDTO;

public interface FAQboardService {
    Long register(FAQboardDTO faQboardDTO);
    FAQboardDTO readOne(Long tno);
    void modify(FAQboardDTO faQboardDTO);
    void remove(Long tno);
}
