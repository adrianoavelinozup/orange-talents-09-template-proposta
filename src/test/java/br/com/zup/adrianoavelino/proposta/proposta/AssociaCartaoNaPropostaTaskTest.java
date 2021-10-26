package br.com.zup.adrianoavelino.proposta.proposta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class AssociaCartaoNaPropostaTaskTest {

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private AssociaCartaoNaPropostaTask associaCartaoNaPropostaTask;

    @MockBean
    private CartaoCliente cartaoCliente;

    @Test
    @DisplayName("Deve associar cartão na proposta")
    void test1() {
        Proposta propostaElegivel = new Proposta("685.104.060-30",
                "email@email.com",
                "João",
                "Rua um",
                new BigDecimal("2000"));
        propostaElegivel.adicionaStatus(StatusProposta.ELEGIVEL);
        propostaRepository.save(propostaElegivel);

        Proposta propostaNaoElegivel = new Proposta("387.831.210-56",
                "email@email.com",
                "João",
                "Rua um",
                new BigDecimal("2000"));
        propostaNaoElegivel.adicionaStatus(StatusProposta.NAO_ELEGIVEL);
        propostaRepository.save(propostaNaoElegivel);

        CartaoResponse cartaoResponse = new CartaoResponse("Adriano",
                "5812-4804-7265-6806",
                LocalDateTime.now(),
                propostaElegivel.getId());
        ResponseEntity<CartaoResponse> response = ResponseEntity.status(HttpStatus.OK).body(cartaoResponse);
        Mockito.when(cartaoCliente.consultarCartao(Mockito.any())).thenReturn(response);

        associaCartaoNaPropostaTask.associarCartao();

        List<Proposta> propostasElegiveisSemCartao = propostaRepository.findByPropostaElegivelSemCartao();
        Assertions.assertTrue(propostasElegiveisSemCartao.isEmpty());
    }
}
