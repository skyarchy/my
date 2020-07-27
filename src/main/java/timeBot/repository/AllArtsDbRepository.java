package timeBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import timeBot.entity.AllArtsEntity;

import java.util.List;

public interface AllArtsDbRepository extends JpaRepository<AllArtsEntity, Long> {

    AllArtsEntity getAllByName(String name);

    List<AllArtsEntity> getAllByStars(int stars);
}
