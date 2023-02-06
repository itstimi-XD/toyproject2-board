package fastcampus.toyproject2board.FQAboard.repository;

import fastcampus.toyproject2board.FQAboard.domain.FAQboard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FAQBoardRepository extends JpaRepository<FAQboard,Long> {
}
