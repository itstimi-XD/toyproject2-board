package fastcampus.toyproject2board.FQAboard.service;

import fastcampus.toyproject2board.FQAboard.domain.FAQboard;
import fastcampus.toyproject2board.FQAboard.dto.FAQboardDTO;
import fastcampus.toyproject2board.FQAboard.repository.FAQBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class FAQboardServiceImpl implements FAQboardService{
    private final ModelMapper modelMapper;
    private final FAQBoardRepository faqBoardRepository;

    @Override
    public Long register(FAQboardDTO faQboardDTO) {
        FAQboard faQboard = modelMapper.map(faQboardDTO,FAQboard.class);
        Long tno = faqBoardRepository.save(faQboard).getTno();
        return tno;
    }

    @Override
    public FAQboardDTO readOne(Long tno) {
        Optional<FAQboard> result = faqBoardRepository.findById(tno);
        FAQboard faQboard = result.orElseThrow();
        FAQboardDTO faQboardDTO = modelMapper.map(faQboard,FAQboardDTO.class);
        return faQboardDTO;
    }
}
