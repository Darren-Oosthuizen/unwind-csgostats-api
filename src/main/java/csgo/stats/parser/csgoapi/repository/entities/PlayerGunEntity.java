package csgo.stats.parser.csgoapi.repository.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class PlayerGunEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME")
    private String name;
    @Column(name = "KILLS")
    private Integer kills;
    @Column(name = "HEADSHOTS")
    private Integer headshots;
    @Column(name = "DAMAGE_DONE")
    private Long damage;

}
