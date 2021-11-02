package br.com.zup.adrianoavelino.proposta.avisoviagem;

import br.com.zup.adrianoavelino.proposta.compartilhada.excecoes.RegraDeNegocioException;
import br.com.zup.adrianoavelino.proposta.compartilhada.seguranca.Ofuscador;
import br.com.zup.adrianoavelino.proposta.proposta.Cartao;
import br.com.zup.adrianoavelino.proposta.proposta.CartaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/v1/cartoes")
public class AvisoViagemController {
    private final Logger logger = LoggerFactory.getLogger(AvisoViagemController.class);

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private AvisoViagemRepository avisoViagemRepository;

    @PostMapping("/{numeroCartao}/avisoViagens")
    public ResponseEntity<Object> cadastrar(@PathVariable String numeroCartao,
                                            @RequestBody @Valid AvisoViagemRequest avisoViagemRequest,
                                            @RequestHeader("User-Agent") String userAgent,
                                            HttpServletRequest request) {
        Cartao cartao = cartaoRepository.findByNumeroCartao(numeroCartao)
                                        .orElseThrow(() -> {
                                            logger.warn("Cartão {} não encontrado", Ofuscador.numeroCartao(numeroCartao));
                                            return new RegraDeNegocioException("Cartão não encontrado", HttpStatus.NOT_FOUND);
                                        });

        AvisoViagem avisoViagem = avisoViagemRequest.toModel(userAgent, request.getRemoteAddr(), cartao);
        avisoViagemRepository.save(avisoViagem);
        logger.info("AvisoViagem do cartão {} criado com sucesso!", Ofuscador.numeroCartao(numeroCartao));
        return ResponseEntity.ok().build();
    }
}
