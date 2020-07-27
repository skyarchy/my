package timeBot.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "POLL_ROLL")
@Data
public class PollEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Column(name = "USER_ID", nullable = false)
    private String userId;
    @Column(name = "DATE", nullable = false)
    private Timestamp date;
    @Column(name = "COUNT_NORMAL")
    private int countNormal;
    @Column(name = "COUNT_ML")
    private int countMl;
    @Column(name = "FIVE_STARS_HERO", length = 65535, columnDefinition = "TEXT")
    private String fiveStars;
    @Column(name = "STAT_ML")
    private String statMl;
    @Column(name = "STAT_NORMAL")
    private String statNormal;
    @Column(name = "STAT_ARTS")
    private String statArts;

}
