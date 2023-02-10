package fastcampus.toyproject2board.service;

import fastcampus.toyproject2board.dto.MemberDTO;
import fastcampus.toyproject2board.entity.MemberEntity;
import fastcampus.toyproject2board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void signUp(MemberDTO memberDTO) {
        //dto -> entity
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        // repository save 호출
        memberRepository.save(memberEntity);
    }

    public MemberDTO login(MemberDTO memberDTO) {
//        이메일로 db에서 조회
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        //비밀번호 일치 판단?
        if (byMemberEmail.isPresent()) {
            //조회결과 있어(회원정보 있음)
            MemberEntity memberEntity = byMemberEmail.get();

            //비번 같아?
            if (memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                //비번 같아
                //entity -> DTO
                return MemberDTO.toMemberDTO(memberEntity);
            } else {
                return null;
            }

        } else {
            //조회 결과 없어
            return null;
        }
    }

    public List<MemberDTO> findAll() {
        return memberRepository.findAll().stream()
                               .map(MemberDTO::toMemberDTO)
                               .toList();
    }

    public MemberDTO findById(Long id) {
        return memberRepository.findById(id).map(MemberDTO::toMemberDTO ).get();
    }

    public String emailCheck(String memberEmail) {
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberEmail);
        if(byMemberEmail.isPresent()) {
            //이미 있어 -> 불가능
            return "NO";
        }else {
            return "OK";
        }

    }
}
