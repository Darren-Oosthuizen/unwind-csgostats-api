package csgo.stats.parser.csgoapi.repository.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "GAME_TEAM")
public class GameTeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CLAN")
    private String clan;

    @Column(name = "SCORE")
    private Integer score;

    @Column(name = "RESULT")
    private Integer result;

    @Column(name = "FIRST_HALF_SCORE")
    private Integer firstHalfScore;

    @Column(name = "SECOND_HALF_SCORE")
    private Integer secondHalfScore;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("kills desc")
    private List<TeamPlayerEntity> players;
}

