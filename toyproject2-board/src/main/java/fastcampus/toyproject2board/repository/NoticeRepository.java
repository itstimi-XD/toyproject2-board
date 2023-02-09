package fastcampus.toyproject2board.repository;

import fastcampus.toyproject2board.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}