package timeBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import timeBot.entity.PollEntity;

public interface PollRollRepository extends JpaRepository<PollEntity, Long> {

    PollEntity getAllByUserId (String userId);
}
