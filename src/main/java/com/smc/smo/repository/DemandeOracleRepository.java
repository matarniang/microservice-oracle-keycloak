package com.smc.smo.repository;

import com.smc.smo.domain.DemandeOracle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Spring Data SQL repository for the DemandeOracle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DemandeOracleRepository extends JpaRepository<DemandeOracle, Long> {
    
    @Query(value="SELECT * FROM demande_oracle p WHERE user=?1",nativeQuery =true)
    List<DemandeOracle> GetDemande(String login);

    // Optional<User> findOneByLogin(String login);
}
