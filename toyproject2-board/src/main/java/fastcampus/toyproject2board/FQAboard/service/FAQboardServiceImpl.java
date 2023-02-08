package fastcampus.toyproject2board.FQAboard.service;

import fastcampus.toyproject2board.FQAboard.domain.FAQboard;
import fastcampus.toyproject2board.FQAboard.dto.FAQboardDTO;
import fastcampus.toyproject2board.FQAboard.repository.FAQBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
    public List<FAQboardDTO> readAll() {
        List<FAQboard> faQboardList = faqBoardRepository.findAll();
        List<FAQboardDTO> faQboardDTOList = new ArrayList<>();
        for (int i = 0; i<faQboardList.size();i++){
            faQboardDTOList.add(modelMapper.map(faQboardList.get(i),FAQboardDTO.class));
        }
        return faQboardDTOList;
    }

    @Override
    public FAQboardDTO readOne(Long tno) {
        Optional<FAQboard> result = faqBoardRepository.findById(tno);
        FAQboard faQboard = result.orElseThrow();
        FAQboardDTO faQboardDTO = modelMapper.map(faQboard,FAQboardDTO.class);
        return faQboardDTO;
    }

    @Override
    public void modify(FAQboardDTO faQboardDTO) {
        Optional<FAQboard> result = faqBoardRepository.findById(faQboardDTO.getTno());
        FAQboard faQboard = result.orElseThrow();
        faQboard.change(faQboardDTO.getTitle(),faQboardDTO.getContent());
        faqBoardRepository.save(faQboard);
    }

    @Override
    public void remove(Long tno) {
        faqBoardRepository.deleteById(tno);
    }

}
