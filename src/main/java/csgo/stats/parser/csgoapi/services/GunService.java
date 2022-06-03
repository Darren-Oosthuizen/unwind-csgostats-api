package csgo.stats.parser.csgoapi.services;

import csgo.stats.parser.csgoapi.repository.IPlayerGunEntityRepository;
import csgo.stats.parser.csgoapi.repository.entities.GunName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GunService {

    private IPlayerGunEntityRepository gunEntityRepository;

    @Autowired
    public GunService(IPlayerGunEntityRepository gunEntityRepository) {
        this.gunEntityRepository = gunEntityRepository;
    }

    public List<GunName> findDistinctGunNames() {
        return this.gunEntityRepository.findDistinctName();
    }
}
