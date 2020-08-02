package timeBot.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ONLINE_CHECK")
@Data
public class SystemTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Column(name = "CODE", nullable = false)
    private String code;
    @Column(name = "ONOFF", nullable = false)
    private int onoff;
}
