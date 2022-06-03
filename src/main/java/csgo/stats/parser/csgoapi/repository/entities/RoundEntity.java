package csgo.stats.parser.csgoapi.repository.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ROUND")
public class RoundEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("time asc")
    private List<KillEntity> kills;

    @Column(name = "ROUND_NUMBER")
    private Integer roundNumber;

    @Column(name = "WINNER")
    private String winner;

    @Column(name = "REASON")
    private String reason;

    @Column(name = "CLUTCH")
    private String clutch;

    @OneToMany(cascade = CascadeType.ALL)
    private List<RoundDamageEntity> damage;

    @OneToMany(cascade = CascadeType.ALL)
    private List<RoundFlashbangEntity> flashbangs;

}
