package br.com.zup.adrianoavelino.proposta.proposta;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PropostaControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PropostaRepository propostaRepository;

    private final String URI = "/v1/propostas";

    @Test
    @DisplayName("Deve cadastrar uma nova proposta com status created (201)")
    void test1() throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest("685.104.060-30",
                "email@email.com",
                "João",
                "Rua um",
                new BigDecimal("2000"));

        String content = objectMapper.writeValueAsString(propostaRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());

        propostaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve cadastrar uma nova proposta com location")
    void test2() throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest("685.104.060-30",
                "email@email.com",
                "João",
                "Rua um",
                new BigDecimal("2000"));

        String content = objectMapper.writeValueAsString(propostaRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("http://**/v1/propostas/{spring:[0-9]+}"));

        propostaRepository.deleteAll();
    }

    @DisplayName("Deve retornar erro quando cadastrar proposta com documento inválido")
    @ParameterizedTest
    @NullAndEmptySource
    @CsvSource(value = {"81004839099", "810048390-99", "810048.390-65"})
    void test3(String documento) throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest(documento,
                "email@email.com",
                "João",
                "Rua um",
                new BigDecimal("2000"));

        String content = objectMapper.writeValueAsString(propostaRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("Deve retornar erro quando cadastrar proposta com email inválido")
    @ParameterizedTest
    @NullAndEmptySource
    @CsvSource(value = {"email.email.com", "email@", "@email"})
    void test4(String email) throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest("685.104.060-30",
                email,
                "João",
                "Rua um",
                new BigDecimal("2000"));

        String content = objectMapper.writeValueAsString(propostaRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("Deve retornar erro quando cadastrar proposta com nome nulo ou em branco")
    @ParameterizedTest
    @NullAndEmptySource
    void test5(String nome) throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest("685.104.060-30",
                "email@email.com",
                nome,
                "Rua um",
                new BigDecimal("2000"));

        String content = objectMapper.writeValueAsString(propostaRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @DisplayName("Deve retornar erro quando cadastrar proposta com endereço nulo ou em branco")
    @ParameterizedTest
    @NullAndEmptySource
    void test6(String endereco) throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest("685.104.060-30",
                "email@email.com",
                "João",
                endereco,
                new BigDecimal("2000"));

        String content = objectMapper.writeValueAsString(propostaRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("Deve retornar erro quando cadastrar proposta com salário inválido")
    @ParameterizedTest
    @NullSource
    @CsvSource({"-1", "-999.99", "0"})
    void test7(BigDecimal salario) throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest("685.104.060-30",
                "email@email.com",
                "João",
                "Rua um",
                salario);

        String content = objectMapper.writeValueAsString(propostaRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro com status 422 quando cadastrar proposta com documento repetido")
    void test8() throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest("685.104.060-30",
                "email@email.com",
                "João",
                "Rua um",
                new BigDecimal("2000"));

        propostaRepository.save(propostaRequest.toModel());

        PropostaRequest propostaRequestRepetida = new PropostaRequest("685.104.060-30",
                "email@email.com",
                "João",
                "Rua um",
                new BigDecimal("2000"));

        String content = objectMapper.writeValueAsString(propostaRequestRepetida);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

        propostaRepository.deleteAll();
    }
}
