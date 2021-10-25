package br.com.zup.adrianoavelino.proposta.proposta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AcompanharPropostaControllerTest {

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private MockMvc mockMvc;
    private String URI = "/v1/propostas/{id}";

    @Test
    @DisplayName("Deve pesquisar uma proposta por id com sucesso")
    void test1() throws Exception {
        Proposta proposta = new Proposta("685.104.060-30",
                "email@email.com",
                "João",
                "Rua um",
                new BigDecimal("2000"));
        propostaRepository.save(proposta);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URI, proposta.getId());

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Optional<Proposta> possivelProposta = propostaRepository.findByDocumento(proposta.getDocumento());
        Assertions.assertEquals(1L, possivelProposta.get().getId());
        propostaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve exibir erro quando pesquisar por proposta não existente")
    void test2() throws Exception {
        Proposta proposta = new Proposta("685.104.060-30",
                "email@email.com",
                "João",
                "Rua um",
                new BigDecimal("2000"));
        propostaRepository.save(proposta);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URI, 999);

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Assertions.assertEquals(1L, propostaRepository.count());
        propostaRepository.deleteAll();
    }
}
