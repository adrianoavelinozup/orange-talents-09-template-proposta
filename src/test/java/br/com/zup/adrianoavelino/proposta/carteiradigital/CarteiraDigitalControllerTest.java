package br.com.zup.adrianoavelino.proposta.carteiradigital;

import br.com.zup.adrianoavelino.proposta.proposta.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class CarteiraDigitalControllerTest {
    private static final String URI = "/v1/cartoes/{numeroCartao}/carteirasDigitais";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartaoCliente cartaoCliente;

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private CarteiraDigitalDoCartaoRepository carteiraDigitalDoCartaoRepository;

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
    @EnumSource(TipoCarteiraDigital.class)
    @DisplayName("Deve cadastrar carteira digital com status CREATED")
    void deveCadastrarCarteiraComStatusCreated(TipoCarteiraDigital tipoCarteiraDigital) throws Exception {
        CarteiraDigitalDoCartaoRequest carteiraDigital = new CarteiraDigitalDoCartaoRequest("email@email.com", tipoCarteiraDigital);
        String content = objectMapper.writeValueAsString(carteiraDigital);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI, cartao.getNumeroCartao())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @ParameterizedTest
    @EnumSource(TipoCarteiraDigital.class)
    @DisplayName("Deve cadastrar carteira digital com Location")
    void deveCadastrarCarteiraComLocation(TipoCarteiraDigital tipoCarteiraDigital) throws Exception {
        CarteiraDigitalDoCartaoRequest carteiraDigital = new CarteiraDigitalDoCartaoRequest("email@email.com", tipoCarteiraDigital);
        String content = objectMapper.writeValueAsString(carteiraDigital);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI, cartao.getNumeroCartao())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        String url = "http://**/v1/cartoes/" + cartao.getNumeroCartao()+ "/carteirasDigitais/";
        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern(url + "{spring:[0-9]+}"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @CsvSource(value = {"email.email.com", "email@", "@email"})
    @DisplayName("Deve mostrar status BAD_REQUEST quando cadastrar carteira digital Email inválido")
    void deveMostrarStatusBadRequestQuandoCadastrarCarteiraComEmailInvalido(String email) throws Exception {
        CarteiraDigitalDoCartaoRequest carteiraDigital = new CarteiraDigitalDoCartaoRequest(email, TipoCarteiraDigital.SAMSUNG);
        String content = objectMapper.writeValueAsString(carteiraDigital);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI, cartao.getNumeroCartao())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar status BAD_REQUEST quando api retornar falha")
    void deveMostrarStatusBadRequestQuandoApiNaoEstiverAcessivel() throws Exception {
        CarteiraDigitalDoCartaoRequest carteiraDigital = new CarteiraDigitalDoCartaoRequest("email@email.com", TipoCarteiraDigital.PAYPAL);
        String content = objectMapper.writeValueAsString(carteiraDigital);

        ApiCartaoCarteiraDigitalRequest requestApi =
            new ApiCartaoCarteiraDigitalRequest("email@email.com", TipoCarteiraDigital.PAYPAL);

        FeignException feigException = Mockito.mock(FeignException.class);
        Mockito.when(cartaoCliente.associarCarteiraDigital(Mockito.eq(cartao.getNumeroCartao()), Mockito.any(ApiCartaoCarteiraDigitalRequest.class)))
                .thenThrow(feigException);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI, cartao.getNumeroCartao())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar status BAD_REQUEST quando cadastrar carteira digital repetida")
    void deveMostrarStatusBadRequestQuandoCadastrarCarteiraRepetida() throws Exception {
        CarteiraDigitalDoCartao carteiraDigitalDoCartao = new CarteiraDigitalDoCartao("email@email.com", TipoCarteiraDigital.PAYPAL, cartao);
        carteiraDigitalDoCartaoRepository.save(carteiraDigitalDoCartao);
        Assertions.assertEquals(1, carteiraDigitalDoCartaoRepository.count());

        CarteiraDigitalDoCartaoRequest carteiraDigital = new CarteiraDigitalDoCartaoRequest("email@email.com", TipoCarteiraDigital.PAYPAL);
        String content = objectMapper.writeValueAsString(carteiraDigital);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI, cartao.getNumeroCartao())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

        Assertions.assertEquals(1, carteiraDigitalDoCartaoRepository.count());
    }
}

