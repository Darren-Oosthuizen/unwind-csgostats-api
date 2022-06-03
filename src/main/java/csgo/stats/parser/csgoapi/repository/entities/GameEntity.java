package csgo.stats.parser.csgoapi.repository.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "GAME")
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "MAP")
    private String map;

    @Column(name = "CT_SCORE")
    private String CTScore;

    @Column(name = "T_SCORE")
    private String TScore;

    @Column(name = "DATE")
    private Date date;

    @OneToMany(cascade = CascadeType.ALL)
    private List<RoundEntity> rounds;

    @OneToMany(cascade = CascadeType.ALL)
    private List<GameTeamEntity> teams;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnoreProperties("games")
    private List<PlayerEntity> players;
}
