package com.smc.smo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.smc.smo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemandeOracleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemandeOracle.class);
        DemandeOracle demandeOracle1 = new DemandeOracle();
        demandeOracle1.setId(1L);
        DemandeOracle demandeOracle2 = new DemandeOracle();
        demandeOracle2.setId(demandeOracle1.getId());
        assertThat(demandeOracle1).isEqualTo(demandeOracle2);
        demandeOracle2.setId(2L);
        assertThat(demandeOracle1).isNotEqualTo(demandeOracle2);
        demandeOracle1.setId(null);
        assertThat(demandeOracle1).isNotEqualTo(demandeOracle2);
    }
}
