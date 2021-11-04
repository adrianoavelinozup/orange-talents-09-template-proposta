package br.com.zup.adrianoavelino.proposta.bloqueio;

import br.com.zup.adrianoavelino.proposta.proposta.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
class BloqueioCartaoControllerTest {
    private static final String URI = "/v1/cartoes/{numeroCartao}/bloqueios";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private BloqueioCartaoRepository bloqueioCartaoRepository;
    
    @Autowired
    private ObjectMapper objectMapper;

    private Cartao cartao;

    @MockBean
    private CartaoCliente cartaoCliente;

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

    @Test
    @DisplayName("Deve bloquear um cartão com sucesso")
    void test1() throws Exception {
        ResultadoBloqueioResponse resultadoBloqueioResponse = new ResultadoBloqueioResponse(StatusCartaoResponse.BLOQUEADO);
        ResponseEntity<ResultadoBloqueioResponse> response = ResponseEntity.status(HttpStatus.CREATED).body(resultadoBloqueioResponse);
        SolicitacaoBloqueioRequest solicitacaoBloqueioRequest = new SolicitacaoBloqueioRequest("Sistema de Propostas");
        Mockito.when(cartaoCliente.bloquearCartao(cartao.getNumeroCartao(),solicitacaoBloqueioRequest)).thenReturn(response);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI, cartao.getNumeroCartao())
                .header("User-Agent", "PostmanRuntime/7.28.4");

        mockMvc.perform(request)
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Deve exibir erro quando bloquear um cartão sem enviar o User Agent")
    void test2() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(URI, cartao.getNumeroCartao());

        mockMvc.perform(request)
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Deve exibir erro quando bloquear um cartão não encontrado")
    void test3() throws Exception {
        String numeroCartaoNaoEncontrado = "111-111-111";
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI, numeroCartaoNaoEncontrado)
                .header("User-Agent", "PostmanRuntime/7.28.4");

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Deve exibir erro quando bloquear um cartão já bloqueado")
    void test4() throws Exception {
        BloqueioCartao cartaoBloqueado = new BloqueioCartao("Firefox", "192.168.1.1", cartao);
        bloqueioCartaoRepository.save(cartaoBloqueado);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI, cartao.getNumeroCartao())
                .header("User-Agent", "PostmanRuntime/7.28.4");

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }
}
