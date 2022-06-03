package csgo.stats.parser.csgoapi.repository.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class RoundDamageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;

    @Column(name = "ATTACKER_STEAM_ID")
    private String attackerSteamId;
    @Column(name = "WEAPON")
    private String weapon;
    @Column(name = "DAMAGE")
    private Integer damage;
    @Column(name = "FRIENDLY")
    private Boolean friendly;
    @Column(name = "VICTIM_STEAM_ID")
    private String victimSteamId;
    @Column(name = "HITGROUP")
    private Integer hitgroup;
}
