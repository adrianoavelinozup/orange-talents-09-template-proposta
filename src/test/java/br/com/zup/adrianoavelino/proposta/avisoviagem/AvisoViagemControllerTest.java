package br.com.zup.adrianoavelino.proposta.avisoviagem;

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
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class AvisoViagemControllerTest {
    private static final String URI = "/v1/cartoes/{numeroCartao}/avisoViagens";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private AvisoViagemRepository avisoViagemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Cartao cartao;

    @BeforeEach
    void carregarDEadodosIniciais() {
        Proposta proposta = new Proposta("563.734.420-55",
                "email@email.com",
                "João",
                "Rua um",
                new BigDecimal("2000"));
        propostaRepository.save(proposta);

        cartao = new Cartao("Adriano","1462-7510-6296-2081", LocalDateTime.now(),proposta);
        cartaoRepository.save(cartao);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Deve mostrar status BAD_REQUEST quando cadastrar aviso de viagem com destino inválido")
    void testDestinoInvalido(String destino) throws Exception {

        AvisoViagemRequest avisoViagemRequest = new AvisoViagemRequest(destino, LocalDate.now().plusDays(7));
        String content = objectMapper.writeValueAsString(avisoViagemRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI, cartao.getNumeroCartao())
                .contentType(MediaType.APPLICATION_JSON)
                .header("User-Agent", "Mozilla Firefox x.x.x")
                .content(content);

        mockMvc.perform(request)
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @ParameterizedTest
    @NullSource
    @CsvSource(value = {"2000-11-02"})
    @DisplayName("Deve mostrar status BAD_REQUEST quando cadastrar aviso de viagem com data inválida")
    void testDataInvalida(LocalDate data) throws Exception {
        AvisoViagemRequest avisoViagemRequest = new AvisoViagemRequest("Registro", data);
        String content = objectMapper.writeValueAsString(avisoViagemRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI, cartao.getNumeroCartao())
                .contentType(MediaType.APPLICATION_JSON)
                .header("User-Agent", "Mozilla Firefox x.x.x")
                .content(content);

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar status NOT_FOUND quando cadastrar aviso de viagem com um número de cartão não encontrado")
    void testNotFoundCartao() throws Exception {
        AvisoViagemRequest avisoViagemRequest = new AvisoViagemRequest("Registro", LocalDate.now());
        String content = objectMapper.writeValueAsString(avisoViagemRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI, "um número de cartão não encontrado")
                .contentType(MediaType.APPLICATION_JSON)
                .header("User-Agent", "Mozilla Firefox x.x.x")
                .content(content);

        mockMvc.perform(request)
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    @DisplayName("Deve mostrar status BAD_REQUEST quando cadastrar aviso de viagem sem User Agent")
    void testAvisoViagemSemUserAgent() throws Exception {
        AvisoViagemRequest avisoViagemRequest = new AvisoViagemRequest("Registro", LocalDate.now());
        String content = objectMapper.writeValueAsString(avisoViagemRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI, cartao.getNumeroCartao())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Deve cadastrar aviso de viagem com status OK")
    void testCadastrarAvisoViagem() throws Exception {
        Assertions.assertEquals(0, avisoViagemRepository.count());

        AvisoViagemRequest avisoViagemRequest = new AvisoViagemRequest("Registro", LocalDate.now());
        String content = objectMapper.writeValueAsString(avisoViagemRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI, cartao.getNumeroCartao())
                .contentType(MediaType.APPLICATION_JSON)
                .header("User-Agent", "Mozilla Firefox x.x.x")
                .content(content);

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assertions.assertEquals(1, avisoViagemRepository.count());
    }
}
