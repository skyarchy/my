package timeBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import timeBot.entity.AllHeroesEntity;

import java.util.List;

public interface AllHeroDbRepository extends JpaRepository<AllHeroesEntity, Long> {

    AllHeroesEntity getAllByName(String name);

    List<AllHeroesEntity> getAllByStars(int stars);

    @Query(value = "select * from ALL_HERO_DB where NAME LIKE %:name%", nativeQuery = true)
    List<AllHeroesEntity> getAllByNotFullName(@Param("name")String name);


}
