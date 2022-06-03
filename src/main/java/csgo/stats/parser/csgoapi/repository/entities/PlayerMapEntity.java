package csgo.stats.parser.csgoapi.repository.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "PLAYER_MAP")
public class PlayerMapEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME")
    private String name;
    @Column(name = "TOTAL_GAMES")
    private Integer totalGames;
    @Column(name = "WINS")
    private Integer wins;
    @Column(name = "LOSSES")
    private Integer losses;
    @Column(name = "DRAWS")
    private Integer draws;
}
