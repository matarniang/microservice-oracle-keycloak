package com.smc.smo.web.rest;

import com.smc.smo.domain.DemandeOracle;
import com.smc.smo.repository.DemandeOracleRepository;
import com.smc.smo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import java.time.LocalDate; 
import org.json.JSONObject;


/**
 * REST controller for managing {@link com.smc.smo.domain.DemandeOracle}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DemandeOracleResource {

    private final Logger log = LoggerFactory.getLogger(DemandeOracleResource.class);

    private static final String ENTITY_NAME = "oracleDemandeOracle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DemandeOracleRepository demandeOracleRepository;
    LocalDate date = LocalDate.now();


    public DemandeOracleResource(DemandeOracleRepository demandeOracleRepository) {
        this.demandeOracleRepository = demandeOracleRepository;
    }

    /**
     * {@code POST  /demande-oracles} : Create a new demandeOracle.
     *
     * @param demandeOracle the demandeOracle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new demandeOracle, or with status {@code 400 (Bad Request)} if the demandeOracle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/demande-oracles")
    public ResponseEntity<DemandeOracle> createDemandeOracle(@RequestBody DemandeOracle demandeOracle) throws URISyntaxException {
        demandeOracle.setDateDemande(date);
        log.debug("REST request to save DemandeOracle : {}", demandeOracle);
        if (demandeOracle.getId() != null) {
            throw new BadRequestAlertException("A new demandeOracle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DemandeOracle result = demandeOracleRepository.save(demandeOracle);
        return ResponseEntity
            .created(new URI("/api/demande-oracles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /demande-oracles/:id} : Updates an existing demandeOracle.
     *
     * @param id the id of the demandeOracle to save.
     * @param demandeOracle the demandeOracle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeOracle,
     * or with status {@code 400 (Bad Request)} if the demandeOracle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the demandeOracle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/demande-oracles/{id}")
    public ResponseEntity<DemandeOracle> updateDemandeOracle(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DemandeOracle demandeOracle
    ) throws URISyntaxException {
        log.debug("REST request to update DemandeOracle : {}, {}", id, demandeOracle);
        if (demandeOracle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeOracle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeOracleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DemandeOracle result = demandeOracleRepository.save(demandeOracle);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demandeOracle.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /demande-oracles/:id} : Partial updates given fields of an existing demandeOracle, field will ignore if it is null
     *
     * @param id the id of the demandeOracle to save.
     * @param demandeOracle the demandeOracle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeOracle,
     * or with status {@code 400 (Bad Request)} if the demandeOracle is not valid,
     * or with status {@code 404 (Not Found)} if the demandeOracle is not found,
     * or with status {@code 500 (Internal Server Error)} if the demandeOracle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/demande-oracles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DemandeOracle> partialUpdateDemandeOracle(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DemandeOracle demandeOracle
    ) throws URISyntaxException {
        log.debug("REST request to partial update DemandeOracle partially : {}, {}", id, demandeOracle);
        if (demandeOracle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeOracle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeOracleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DemandeOracle> result = demandeOracleRepository
            .findById(demandeOracle.getId())
            .map(existingDemandeOracle -> {
                if (demandeOracle.getNomApp() != null) {
                    existingDemandeOracle.setNomApp(demandeOracle.getNomApp());
                }
                if (demandeOracle.getPassword() != null) {
                    existingDemandeOracle.setPassword(demandeOracle.getPassword());
                }
                if (demandeOracle.getAction() != null) {
                    existingDemandeOracle.setAction(demandeOracle.getAction());
                }
                if (demandeOracle.getStatus() != null) {
                    existingDemandeOracle.setStatus(demandeOracle.getStatus());
                }
                if (demandeOracle.getMessage() != null) {
                    existingDemandeOracle.setMessage(demandeOracle.getMessage());
                }
                if (demandeOracle.getDateDemande() != null) {
                    existingDemandeOracle.setDateDemande(demandeOracle.getDateDemande());
                }
                if (demandeOracle.getDateRetour() != null) {
                    existingDemandeOracle.setDateRetour(demandeOracle.getDateRetour());
                }
                if (demandeOracle.getUser() != null) {
                    existingDemandeOracle.setUser(demandeOracle.getUser());
                }

                return existingDemandeOracle;
            })
            .map(demandeOracleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demandeOracle.getId().toString())
        );
    }

    /**
     * {@code GET  /demande-oracles} : get all the demandeOracles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demandeOracles in body.
     */
    @GetMapping("/demande-oracles")
    public List<DemandeOracle> getAllDemandeOracles() {
        log.debug("REST request to get all DemandeOracles");
        return demandeOracleRepository.findAll();
    }

    /**
     * {@code POST  /demande-oracles-login} : get all the demande-oracles-login.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demandes in body.
     */
    @PostMapping("/demande-oracles-login")
    public List<DemandeOracle> getDemande(@RequestBody String request) {
        log.debug("REST request to get all Demandes");
        JSONObject object=new JSONObject(request);
        String username=object.getString("user");
        return demandeOracleRepository.GetDemande(username);
    }
    
    /**
     * {@code POST  /demande-oracles-delete} : get the demande delete.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demandes in body.
     */
    @PostMapping("/demande-oracles-delete")
    public ResponseEntity<Void> delete(@RequestBody String request) {
        JSONObject object=new JSONObject(request);
        Long id=object.getLong("id");
        log.debug("REST request to delete DemandeOracle : {}", id);
        demandeOracleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /demande-oracles/:id} : get the "id" demandeOracle.
     *
     * @param id the id of the demandeOracle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the demandeOracle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/demande-oracles/{id}")
    public ResponseEntity<DemandeOracle> getDemandeOracle(@PathVariable Long id) {
        log.debug("REST request to get DemandeOracle : {}", id);
        Optional<DemandeOracle> demandeOracle = demandeOracleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(demandeOracle);
    }

    /**
     * {@code DELETE  /demande-oracles/:id} : delete the "id" demandeOracle.
     *
     * @param id the id of the demandeOracle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/demande-oracles/{id}")
    public ResponseEntity<Void> deleteDemandeOracle(@PathVariable Long id) {
        log.debug("REST request to delete DemandeOracle : {}", id);
        demandeOracleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
