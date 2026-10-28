package tn.esprit.tpautoloc.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ContratControllerTest {

    @Autowired
    private ContratController contratController;

    @Autowired
    private PaiementController paiementController;

    @Autowired
    private AgenceController agenceController;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(contratController, paiementController, agenceController)
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    @Test
    void testFindAllContrats() throws Exception {
        mockMvc.perform(get("/contrats"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateContratSuccess() throws Exception {
        String json = """
            {
                \"dateSignature\": \"2026-11-02\",
                \"montantTotal\": 480.00,
                \"valide\": true
            }
            """;

        mockMvc.perform(post("/contrats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idContrat").exists())
                .andExpect(jsonPath("$.montantTotal").value(480.00));
    }

    @Test
    void testCreateContratWithIdThrowsBadRequest() throws Exception {
        String json = """
            {
                \"idContrat\": 999,
                \"dateSignature\": \"2026-11-02\",
                \"montantTotal\": 480.00,
                \"valide\": true
            }
            """;

        mockMvc.perform(post("/contrats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statut").value(400))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        mockMvc.perform(get("/contrats/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statut").value(404))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testCreateContratWithPaiementsCascade() throws Exception {
        String json = """
            {
                \"dateSignature\": \"2026-11-02\",
                \"montantTotal\": 480.00,
                \"valide\": true,
                \"paiements\": [
                    { \"montant\": 150.00, \"datePaiement\": \"2026-11-02\", \"modePaiement\": \"CARTE\" },
                    { \"montant\": 330.00, \"datePaiement\": \"2026-11-06\", \"modePaiement\": \"VIREMENT\" }
                ]
            }
            """;

        mockMvc.perform(post("/contrats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paiements").isArray());
    }

    @Test
    void testCreateAgenceEmptyNameBadRequest() throws Exception {
        String json = """
            {
                \"nom\": \"\",
                \"ville\": \"Tunis\"
            }
            """;

        mockMvc.perform(post("/agences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statut").value(400));
    }
}
