package timeBot.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ALL_ARTS_DB")
@Data
public class AllArtsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "IMAGE", nullable = false)
    private String image;
    @Column(name = "STARS", nullable = false)
    private int stars;
    @Column(name = "ART_CLASS", nullable = false)
    private String artClass;
    @Column(name = "SKILL", nullable = false, length = 65535, columnDefinition="TEXT")
    private String skill;
    @Column(name = "MAX", nullable = false)
    private String max;
    @Column(name = "MIN", nullable = false)
    private String min;

}
