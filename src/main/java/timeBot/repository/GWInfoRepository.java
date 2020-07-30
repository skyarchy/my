package timeBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import timeBot.entity.GWInfoEntity;

import java.util.List;

public interface GWInfoRepository extends JpaRepository<GWInfoEntity, Long> {

    @Query(value = "select * from GW_INFO_TABLE where BASE_NUMBER=:bNumber and CHAT_ID=:chatId", nativeQuery = true)
    GWInfoEntity getAllByBNumberAndChatId(@Param("bNumber") int bNumber, @Param("chatId") String chatId);

    @Query(value = "select * from GW_INFO_TABLE", nativeQuery = true)
    List<GWInfoEntity> getAll();

}
