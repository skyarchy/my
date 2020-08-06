package timeBot.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "GW_INFO_TABLE")
@Data
public class GWInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "CHAT_ID", nullable = false)
    private String chatId;
    @Column(name = "MSG_ID", length = 65535, columnDefinition = "TEXT")
    private String msgId;
    @Column(name = "BASE_NUMBER", nullable = false)
    private int bNumber;
    @Column(name = "DATE", nullable = false)
    private Timestamp date;
    @Column(name = "INFO", length = 65535, columnDefinition = "TEXT")
    private String info;
}
