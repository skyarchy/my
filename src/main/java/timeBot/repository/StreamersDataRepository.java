package timeBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import timeBot.entity.StreamersDataEntity;

import java.util.List;

public interface StreamersDataRepository extends JpaRepository<StreamersDataEntity, Long> {


    @Query(value = "select * from STREAMERS_DATA", nativeQuery = true)
    List<StreamersDataEntity> getAll();

    @Query(value = "select * from STREAMERS_DATA where NAME=:name and CODE=:code", nativeQuery = true)
    StreamersDataEntity getStreamer(@Param("name") String name, @Param("code") String code);

    StreamersDataEntity getAllByName(String name);

}
