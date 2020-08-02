package timeBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import timeBot.entity.SystemTable;

public interface SystemTableRepository extends JpaRepository<SystemTable, Long> {

    SystemTable getAllByCode(String code);
}
