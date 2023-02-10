package fastcampus.toyproject2board.noticeBoard.repository;

import fastcampus.toyproject2board.noticeBoard.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
}
