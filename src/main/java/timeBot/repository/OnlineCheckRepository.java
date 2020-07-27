package timeBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import timeBot.entity.OnlineEntity;

public interface OnlineCheckRepository extends JpaRepository<OnlineEntity, Long> {


    @Query(value = "select ONOFF from ONLINE_CHECK where CODE=?1", nativeQuery = true)
    int getFlag(String code);

    @Query(value = "select * from ONLINE_CHECK where CODE=?1", nativeQuery = true)
    OnlineEntity getRow(String code);


}