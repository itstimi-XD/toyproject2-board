package fastcampus.toyproject2board.FQAboard.service;

import fastcampus.toyproject2board.FQAboard.dto.FAQboardDTO;

import java.util.List;

public interface FAQboardService {
    Long register(FAQboardDTO faQboardDTO);
    List<FAQboardDTO> readAll();
    FAQboardDTO readOne(Long tno);
    void modify(FAQboardDTO faQboardDTO);
    void remove(Long tno);
}
