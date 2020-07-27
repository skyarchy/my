package timeBot.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ALL_HERO_DB")
@Data
public class AllHeroesEntity {
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
    @Column(name = "COLOR", nullable = false)
    private String color;
    @Column(name = "HERO_CLASS", nullable = false)
    private String heroClass;
    @Column(name = "SKILL_ONE", nullable = false, length = 65535, columnDefinition="TEXT")
    @Type(type="text")
    private String skillOne;
    @Column(name = "SKILL_TWO", nullable = false, length = 65535, columnDefinition="TEXT")
    @Type(type="text")
    private String skillTwo;
    @Column(name = "SKILL_THREE", nullable = false, length = 65535, columnDefinition="TEXT")
    @Type(type="text")
    private String skillThree;
    @Column(name = "AWAKEYES", nullable = false, length = 65535, columnDefinition="TEXT")
    @Type(type="text")
    private String awakeYes;
    @Column(name = "AWAKENO", nullable = false, length = 65535, columnDefinition="TEXT")
    @Type(type="text")
    private String awakeNo;

}
