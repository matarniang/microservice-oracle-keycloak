package com.smc.smo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.smc.smo.IntegrationTest;
import com.smc.smo.domain.DemandeOracle;
import com.smc.smo.repository.DemandeOracleRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DemandeOracleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DemandeOracleResourceIT {

    private static final String DEFAULT_NOM_APP = "AAAAAAAAAA";
    private static final String UPDATED_NOM_APP = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEMANDE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEMANDE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_RETOUR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RETOUR = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/demande-oracles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DemandeOracleRepository demandeOracleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDemandeOracleMockMvc;

    private DemandeOracle demandeOracle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeOracle createEntity(EntityManager em) {
        DemandeOracle demandeOracle = new DemandeOracle()
            .nomApp(DEFAULT_NOM_APP)
            .password(DEFAULT_PASSWORD)
            .action(DEFAULT_ACTION)
            .status(DEFAULT_STATUS)
            .message(DEFAULT_MESSAGE)
            .dateDemande(DEFAULT_DATE_DEMANDE)
            .dateRetour(DEFAULT_DATE_RETOUR)
            .user(DEFAULT_USER);
        return demandeOracle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeOracle createUpdatedEntity(EntityManager em) {
        DemandeOracle demandeOracle = new DemandeOracle()
            .nomApp(UPDATED_NOM_APP)
            .password(UPDATED_PASSWORD)
            .action(UPDATED_ACTION)
            .status(UPDATED_STATUS)
            .message(UPDATED_MESSAGE)
            .dateDemande(UPDATED_DATE_DEMANDE)
            .dateRetour(UPDATED_DATE_RETOUR)
            .user(UPDATED_USER);
        return demandeOracle;
    }

    @BeforeEach
    public void initTest() {
        demandeOracle = createEntity(em);
    }

    @Test
    @Transactional
    void createDemandeOracle() throws Exception {
        int databaseSizeBeforeCreate = demandeOracleRepository.findAll().size();
        // Create the DemandeOracle
        restDemandeOracleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeOracle))
            )
            .andExpect(status().isCreated());

        // Validate the DemandeOracle in the database
        List<DemandeOracle> demandeOracleList = demandeOracleRepository.findAll();
        assertThat(demandeOracleList).hasSize(databaseSizeBeforeCreate + 1);
        DemandeOracle testDemandeOracle = demandeOracleList.get(demandeOracleList.size() - 1);
        assertThat(testDemandeOracle.getNomApp()).isEqualTo(DEFAULT_NOM_APP);
        assertThat(testDemandeOracle.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testDemandeOracle.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testDemandeOracle.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDemandeOracle.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testDemandeOracle.getDateDemande()).isEqualTo(DEFAULT_DATE_DEMANDE);
        assertThat(testDemandeOracle.getDateRetour()).isEqualTo(DEFAULT_DATE_RETOUR);
        assertThat(testDemandeOracle.getUser()).isEqualTo(DEFAULT_USER);
    }

    @Test
    @Transactional
    void createDemandeOracleWithExistingId() throws Exception {
        // Create the DemandeOracle with an existing ID
        demandeOracle.setId(1L);

        int databaseSizeBeforeCreate = demandeOracleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemandeOracleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeOracle))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeOracle in the database
        List<DemandeOracle> demandeOracleList = demandeOracleRepository.findAll();
        assertThat(demandeOracleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDemandeOracles() throws Exception {
        // Initialize the database
        demandeOracleRepository.saveAndFlush(demandeOracle);

        // Get all the demandeOracleList
        restDemandeOracleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demandeOracle.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomApp").value(hasItem(DEFAULT_NOM_APP)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].dateDemande").value(hasItem(DEFAULT_DATE_DEMANDE.toString())))
            .andExpect(jsonPath("$.[*].dateRetour").value(hasItem(DEFAULT_DATE_RETOUR.toString())))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER)));
    }

    @Test
    @Transactional
    void getDemandeOracle() throws Exception {
        // Initialize the database
        demandeOracleRepository.saveAndFlush(demandeOracle);

        // Get the demandeOracle
        restDemandeOracleMockMvc
            .perform(get(ENTITY_API_URL_ID, demandeOracle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(demandeOracle.getId().intValue()))
            .andExpect(jsonPath("$.nomApp").value(DEFAULT_NOM_APP))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.dateDemande").value(DEFAULT_DATE_DEMANDE.toString()))
            .andExpect(jsonPath("$.dateRetour").value(DEFAULT_DATE_RETOUR.toString()))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER));
    }

    @Test
    @Transactional
    void getNonExistingDemandeOracle() throws Exception {
        // Get the demandeOracle
        restDemandeOracleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDemandeOracle() throws Exception {
        // Initialize the database
        demandeOracleRepository.saveAndFlush(demandeOracle);

        int databaseSizeBeforeUpdate = demandeOracleRepository.findAll().size();

        // Update the demandeOracle
        DemandeOracle updatedDemandeOracle = demandeOracleRepository.findById(demandeOracle.getId()).get();
        // Disconnect from session so that the updates on updatedDemandeOracle are not directly saved in db
        em.detach(updatedDemandeOracle);
        updatedDemandeOracle
            .nomApp(UPDATED_NOM_APP)
            .password(UPDATED_PASSWORD)
            .action(UPDATED_ACTION)
            .status(UPDATED_STATUS)
            .message(UPDATED_MESSAGE)
            .dateDemande(UPDATED_DATE_DEMANDE)
            .dateRetour(UPDATED_DATE_RETOUR)
            .user(UPDATED_USER);

        restDemandeOracleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDemandeOracle.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDemandeOracle))
            )
            .andExpect(status().isOk());

        // Validate the DemandeOracle in the database
        List<DemandeOracle> demandeOracleList = demandeOracleRepository.findAll();
        assertThat(demandeOracleList).hasSize(databaseSizeBeforeUpdate);
        DemandeOracle testDemandeOracle = demandeOracleList.get(demandeOracleList.size() - 1);
        assertThat(testDemandeOracle.getNomApp()).isEqualTo(UPDATED_NOM_APP);
        assertThat(testDemandeOracle.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testDemandeOracle.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testDemandeOracle.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDemandeOracle.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testDemandeOracle.getDateDemande()).isEqualTo(UPDATED_DATE_DEMANDE);
        assertThat(testDemandeOracle.getDateRetour()).isEqualTo(UPDATED_DATE_RETOUR);
        assertThat(testDemandeOracle.getUser()).isEqualTo(UPDATED_USER);
    }

    @Test
    @Transactional
    void putNonExistingDemandeOracle() throws Exception {
        int databaseSizeBeforeUpdate = demandeOracleRepository.findAll().size();
        demandeOracle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeOracleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demandeOracle.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeOracle))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeOracle in the database
        List<DemandeOracle> demandeOracleList = demandeOracleRepository.findAll();
        assertThat(demandeOracleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDemandeOracle() throws Exception {
        int databaseSizeBeforeUpdate = demandeOracleRepository.findAll().size();
        demandeOracle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeOracleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeOracle))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeOracle in the database
        List<DemandeOracle> demandeOracleList = demandeOracleRepository.findAll();
        assertThat(demandeOracleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDemandeOracle() throws Exception {
        int databaseSizeBeforeUpdate = demandeOracleRepository.findAll().size();
        demandeOracle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeOracleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeOracle))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeOracle in the database
        List<DemandeOracle> demandeOracleList = demandeOracleRepository.findAll();
        assertThat(demandeOracleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDemandeOracleWithPatch() throws Exception {
        // Initialize the database
        demandeOracleRepository.saveAndFlush(demandeOracle);

        int databaseSizeBeforeUpdate = demandeOracleRepository.findAll().size();

        // Update the demandeOracle using partial update
        DemandeOracle partialUpdatedDemandeOracle = new DemandeOracle();
        partialUpdatedDemandeOracle.setId(demandeOracle.getId());

        partialUpdatedDemandeOracle.nomApp(UPDATED_NOM_APP).password(UPDATED_PASSWORD).action(UPDATED_ACTION).message(UPDATED_MESSAGE);

        restDemandeOracleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeOracle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeOracle))
            )
            .andExpect(status().isOk());

        // Validate the DemandeOracle in the database
        List<DemandeOracle> demandeOracleList = demandeOracleRepository.findAll();
        assertThat(demandeOracleList).hasSize(databaseSizeBeforeUpdate);
        DemandeOracle testDemandeOracle = demandeOracleList.get(demandeOracleList.size() - 1);
        assertThat(testDemandeOracle.getNomApp()).isEqualTo(UPDATED_NOM_APP);
        assertThat(testDemandeOracle.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testDemandeOracle.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testDemandeOracle.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDemandeOracle.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testDemandeOracle.getDateDemande()).isEqualTo(DEFAULT_DATE_DEMANDE);
        assertThat(testDemandeOracle.getDateRetour()).isEqualTo(DEFAULT_DATE_RETOUR);
        assertThat(testDemandeOracle.getUser()).isEqualTo(DEFAULT_USER);
    }

    @Test
    @Transactional
    void fullUpdateDemandeOracleWithPatch() throws Exception {
        // Initialize the database
        demandeOracleRepository.saveAndFlush(demandeOracle);

        int databaseSizeBeforeUpdate = demandeOracleRepository.findAll().size();

        // Update the demandeOracle using partial update
        DemandeOracle partialUpdatedDemandeOracle = new DemandeOracle();
        partialUpdatedDemandeOracle.setId(demandeOracle.getId());

        partialUpdatedDemandeOracle
            .nomApp(UPDATED_NOM_APP)
            .password(UPDATED_PASSWORD)
            .action(UPDATED_ACTION)
            .status(UPDATED_STATUS)
            .message(UPDATED_MESSAGE)
            .dateDemande(UPDATED_DATE_DEMANDE)
            .dateRetour(UPDATED_DATE_RETOUR)
            .user(UPDATED_USER);

        restDemandeOracleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeOracle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeOracle))
            )
            .andExpect(status().isOk());

        // Validate the DemandeOracle in the database
        List<DemandeOracle> demandeOracleList = demandeOracleRepository.findAll();
        assertThat(demandeOracleList).hasSize(databaseSizeBeforeUpdate);
        DemandeOracle testDemandeOracle = demandeOracleList.get(demandeOracleList.size() - 1);
        assertThat(testDemandeOracle.getNomApp()).isEqualTo(UPDATED_NOM_APP);
        assertThat(testDemandeOracle.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testDemandeOracle.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testDemandeOracle.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDemandeOracle.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testDemandeOracle.getDateDemande()).isEqualTo(UPDATED_DATE_DEMANDE);
        assertThat(testDemandeOracle.getDateRetour()).isEqualTo(UPDATED_DATE_RETOUR);
        assertThat(testDemandeOracle.getUser()).isEqualTo(UPDATED_USER);
    }

    @Test
    @Transactional
    void patchNonExistingDemandeOracle() throws Exception {
        int databaseSizeBeforeUpdate = demandeOracleRepository.findAll().size();
        demandeOracle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeOracleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, demandeOracle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeOracle))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeOracle in the database
        List<DemandeOracle> demandeOracleList = demandeOracleRepository.findAll();
        assertThat(demandeOracleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDemandeOracle() throws Exception {
        int databaseSizeBeforeUpdate = demandeOracleRepository.findAll().size();
        demandeOracle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeOracleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeOracle))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeOracle in the database
        List<DemandeOracle> demandeOracleList = demandeOracleRepository.findAll();
        assertThat(demandeOracleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDemandeOracle() throws Exception {
        int databaseSizeBeforeUpdate = demandeOracleRepository.findAll().size();
        demandeOracle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeOracleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeOracle))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeOracle in the database
        List<DemandeOracle> demandeOracleList = demandeOracleRepository.findAll();
        assertThat(demandeOracleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDemandeOracle() throws Exception {
        // Initialize the database
        demandeOracleRepository.saveAndFlush(demandeOracle);

        int databaseSizeBeforeDelete = demandeOracleRepository.findAll().size();

        // Delete the demandeOracle
        restDemandeOracleMockMvc
            .perform(delete(ENTITY_API_URL_ID, demandeOracle.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DemandeOracle> demandeOracleList = demandeOracleRepository.findAll();
        assertThat(demandeOracleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
