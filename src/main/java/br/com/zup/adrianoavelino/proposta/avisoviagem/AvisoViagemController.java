package br.com.zup.adrianoavelino.proposta.avisoviagem;

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
import javax.validation.Valid;

@RestController
@RequestMapping("/v1/cartoes")
public class AvisoViagemController {
    private final Logger logger = LoggerFactory.getLogger(AvisoViagemController.class);

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private AvisoViagemRepository avisoViagemRepository;

    @Autowired
    private CartaoCliente cartaoCliente;

    @PostMapping("/{numeroCartao}/avisosViagens")
    @Transactional
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
        adicionarAvisoViagem(cartao, avisoViagem);
        return ResponseEntity.ok().build();
    }

    private void adicionarAvisoViagem(Cartao cartao, AvisoViagem avisoViagem) {
        try {
            SolicitacaoAvisoViagemRequest solicitacaoAvisoViagemRequest = new SolicitacaoAvisoViagemRequest(avisoViagem.getDestino(), avisoViagem.getDataTermino());
            cartaoCliente.avisarViagem(cartao.getNumeroCartao(), solicitacaoAvisoViagemRequest);
            avisoViagemRepository.save(avisoViagem);
            logger.info("AvisoViagem do cartão {} criado com sucesso!", Ofuscador.numeroCartao(cartao.getNumeroCartao()));
        } catch (FeignException.UnprocessableEntity exception) {
            logger.warn("Não foi possível adicionar AvisoViagem do cartão {} no sistema externo. Status do Erro: {}",
                    Ofuscador.numeroCartao(cartao.getNumeroCartao()), HttpStatus.UNPROCESSABLE_ENTITY);
            throw new RegraDeNegocioException("Não foi possível adicionar Aviso de viagem no sistema externo", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        catch (FeignException exception) {
            logger.error("Não foi possível adicionar AvisoViagem do cartão {} no sistema externo. Erro: não esperado, status {}",
                    Ofuscador.numeroCartao(cartao.getNumeroCartao()), HttpStatus.INTERNAL_SERVER_ERROR);
            throw new RegraDeNegocioException("Não foi possível adicionar Aviso de viagem no sistema externo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
