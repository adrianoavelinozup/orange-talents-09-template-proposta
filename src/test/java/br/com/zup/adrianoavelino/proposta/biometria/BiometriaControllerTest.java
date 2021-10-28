package br.com.zup.adrianoavelino.proposta.biometria;

import br.com.zup.adrianoavelino.proposta.proposta.Cartao;
import br.com.zup.adrianoavelino.proposta.proposta.CartaoRepository;
import br.com.zup.adrianoavelino.proposta.proposta.Proposta;
import br.com.zup.adrianoavelino.proposta.proposta.PropostaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BiometriaControllerTest {
    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Cartao cartao;

    private static final String URI = "/v1/cartoes/{cartaoId}/biometrias";

    @BeforeEach
    void carregarRegistrosIniciais() {
        Proposta proposta = new Proposta("387.831.210-56",
                "email@email.com",
                "João",
                "Rua um",
                new BigDecimal("2000"));
        propostaRepository.save(proposta);
        cartao = new Cartao("João", "3098-6654-7706-8653", LocalDateTime.now(),proposta);

        cartaoRepository.save(cartao);
    }

    @Test
    @DisplayName("Deve adicionar uma biometria com status created (201)")
    void test1() throws Exception {
        BiometriaRequest biometriaRequest = new BiometriaRequest("b2xhIQ==");

        String content = objectMapper.writeValueAsString(biometriaRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI, cartao.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Deve adicionar uma biometria com location")
    void test2() throws Exception {
        BiometriaRequest biometriaRequest = new BiometriaRequest("b2xhIQ==");

        String content = objectMapper.writeValueAsString(biometriaRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI, cartao.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        String url = "http://**/v1/cartoes/" + cartao.getId() + "/biometrias/";
        mockMvc.perform(request)
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.redirectedUrlPattern(url + "{spring:[0-9]+}"));
    }

    @DisplayName("Deve retornar erro quando adicionar uma biometria com impressão digital inválida")
    @ParameterizedTest
    @NullAndEmptySource
    @CsvSource(value = {"a", "}"})
    void test3(String impressaoDigital) throws Exception {
        BiometriaRequest biometriaRequest = new BiometriaRequest(impressaoDigital);

        String content = objectMapper.writeValueAsString(biometriaRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI, cartao.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro quando adicionar biometria num cartão não existente")
    void test4() throws Exception {
        BiometriaRequest biometriaRequest = new BiometriaRequest("b2xhIQ==");

        String content = objectMapper.writeValueAsString(biometriaRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI, 9999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

        Assertions.assertEquals(propostaRepository.count(), 1);
    }

}
