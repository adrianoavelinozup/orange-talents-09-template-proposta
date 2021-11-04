package br.com.zup.adrianoavelino.proposta.carteiradigital;

import br.com.zup.adrianoavelino.proposta.compartilhada.excecoes.RegraDeNegocioException;
import br.com.zup.adrianoavelino.proposta.compartilhada.seguranca.Ofuscador;
import br.com.zup.adrianoavelino.proposta.proposta.Cartao;
import br.com.zup.adrianoavelino.proposta.proposta.CartaoCliente;
import br.com.zup.adrianoavelino.proposta.proposta.CartaoRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/v1/cartoes")
public class CarteiraDigitalPaypalController {
    private final Logger logger = LoggerFactory.getLogger(CarteiraDigitalPaypalController.class);

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private CarteiraDigitalDoCartaoRepository carteiraDigitalDoCartaoRepository;

    @Autowired
    private CartaoCliente cartaoCliente;

    @PostMapping("/{numeroCartao}/carteirasDigitais")
    public ResponseEntity<Object> cadastrar(@PathVariable String numeroCartao,
                                    @RequestBody @Valid CarteiraDigitalDoCartaoRequest request,
                                    UriComponentsBuilder uriBuilder) {
        Cartao cartao = cartaoRepository.findByNumeroCartao(numeroCartao)
                .orElseThrow(() -> {
                    logger.warn("Cartão {} não encontrado", Ofuscador.numeroCartao(numeroCartao));
                    throw new RegraDeNegocioException("Cartão não encontrado", HttpStatus.NOT_FOUND);
                });

        CarteiraDigitalDoCartao carteiraDigitalDoCartao = request.toModel(cartao);

        if (carteiraDigitalDoCartao.jaEstaCadastrada(carteiraDigitalDoCartaoRepository)) {
            throw new RegraDeNegocioException("Carteira digital " + carteiraDigitalDoCartao.getTipoCarteiraDigital()
                    + " já está cadastrada", HttpStatus.BAD_REQUEST);
        }

        ApiCartaoCarteiraDigitalRequest apiCartaoCarteiraDigitalRequest =
                new ApiCartaoCarteiraDigitalRequest(carteiraDigitalDoCartao);

        associarCarteiraDigital(numeroCartao,carteiraDigitalDoCartao,apiCartaoCarteiraDigitalRequest);
        URI uri = uriBuilder.path("/v1/cartoes/{numeroCartao}/carteirasDigitais/{id}")
                .buildAndExpand(cartao.getNumeroCartao(), carteiraDigitalDoCartao.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    private void associarCarteiraDigital(String numeroCartao, CarteiraDigitalDoCartao carteiraDigital,ApiCartaoCarteiraDigitalRequest apiCartaoCarteiraDigitalRequest) {
        try {
            ResponseEntity<ApiCartaoCarteiraDigitalResponse> responseEntity = cartaoCliente.associarCarteiraDigital(numeroCartao, apiCartaoCarteiraDigitalRequest);
            carteiraDigitalDoCartaoRepository.save(carteiraDigital);
            logger.info("Carteira Digital cartão {} associada com sucesso!", Ofuscador.numeroCartao(numeroCartao));
        } catch (FeignException e) {
            logger.error("Não foi possível adicionar a carteira digital para o cartão {} .Erro não esperado",
                    Ofuscador.numeroCartao(numeroCartao));
            throw new RegraDeNegocioException("Não foi possível sincronizar a carteira digital no sistema externo", HttpStatus.BAD_REQUEST);
        }
    }
}
