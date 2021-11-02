package br.com.zup.adrianoavelino.proposta.bloqueio;

import br.com.zup.adrianoavelino.proposta.compartilhada.excecoes.EntidadeNaoEncontradaException;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/v1/cartoes")
public class BloqueioCartaoController {
    private final Logger logger = LoggerFactory.getLogger(BloqueioCartaoController.class);

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private BloqueioCartaoRepository bloqueioCartaoRepository;

    @Autowired
    private CartaoCliente cartaoCliente;

    @PostMapping("/{numeroCartao}/bloqueios")
    @Transactional
    public ResponseEntity<Object> cadastrar(@PathVariable String numeroCartao,
                                       @NotBlank @RequestHeader("User-Agent") String userAgent,
                                       HttpServletRequest request) {
        Cartao cartao = cartaoRepository
                .findByNumeroCartao(numeroCartao)
                .orElseThrow(() -> {
                    logger.info("Cartão número {} não encontrado", Ofuscador.numeroCartao(numeroCartao));
                    return new EntidadeNaoEncontradaException("Cartão não encontrado", HttpStatus.NOT_FOUND);
                });

        if (bloqueioCartaoRepository.existsByCartaoNumeroCartao(numeroCartao)) {
            logger.info("Cartão {} já está bloqueado", Ofuscador.numeroCartao(numeroCartao));
            throw new RegraDeNegocioException("Cartão já está bloqueado", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        BloqueioCartao bloqueioCartao = new BloqueioCartao(userAgent, request.getRemoteAddr(), cartao);

        bloquearCartao(cartao, bloqueioCartao);
        return ResponseEntity.ok().build();
    }

    private void bloquearCartao(Cartao cartao, BloqueioCartao bloqueioCartao) {
        try {
            cartaoCliente.bloquearCartao(cartao.getNumeroCartao(), new SolicitacaoBloqueioRequest("Sistema de Propostas"));
            cartao.bloquear();
            cartaoRepository.save(cartao);
            bloqueioCartaoRepository.save(bloqueioCartao);
            logger.info("Cartão {} bloqueado com sucesso!", Ofuscador.numeroCartao(cartao.getNumeroCartao()));
        } catch (FeignException.UnprocessableEntity exception) {
            logger.warn("Cartão {}, não foi possível bloquear cartão no sistema Externo. Erro: {}",
                    Ofuscador.numeroCartao(cartao.getNumeroCartao()), exception.getMessage());
            throw new RegraDeNegocioException("Cartão já está bloqueado no sistema externo", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        catch (FeignException exception) {
            logger.error("Cartão {}, não foi possível bloquear cartão no sistema Externo. Erro: não esperado",
                    Ofuscador.numeroCartao(cartao.getNumeroCartao()));
        }
    }
}
