package br.com.zup.adrianoavelino.proposta.proposta;

import br.com.zup.adrianoavelino.proposta.compartilhada.anotacoes.excecoes.PropostaRepetidaException;
import feign.Feign;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/v1/propostas")
public class PropostaController {
    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private AnaliseFinanceiraCliente analiseFinanceiraCliente;

    private final Logger logger = LoggerFactory.getLogger(PropostaController.class);

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid PropostaRequest request, UriComponentsBuilder uriBuilder) {
        Proposta proposta = request.toModel();
        if(proposta.ehRepetida(propostaRepository)) {
            logger.warn("Proposta com o documento {} já está cadastrada!", proposta.getDocumento());
            throw  new PropostaRepetidaException("Proposta repetida. Não pode haver mais de uma proposta por documento");
        }

        propostaRepository.save(proposta);
        verificarSituacaoFinanceira(proposta);
        propostaRepository.save(proposta);

        URI uri = uriBuilder.path("/v1/propostas/{id}").buildAndExpand(proposta.getId()).toUri();
        logger.info("Proposta documento={}, salário={}, status={} criada com sucesso!", proposta.getDocumento(), proposta.getSalario(), proposta.getStatusProposta());
        return ResponseEntity.created(uri).build();
    }

    private void verificarSituacaoFinanceira(Proposta proposta) {
        try {
            SolicitacaoAnaliseRequest solicitacaoAnaliseRequest = new SolicitacaoAnaliseRequest(proposta);
            analiseFinanceiraCliente.solicitar(solicitacaoAnaliseRequest);
            proposta.adicionaStatus(StatusProposta.ELEGIVEL);
            logger.info("Proposta documento={} atualiza status para {}", proposta.getDocumento(), proposta.getStatusProposta());
        } catch (FeignException.UnprocessableEntity feignException) {
            proposta.adicionaStatus(StatusProposta.NAO_ELEGIVEL);
            logger.info("Proposta documento={} atualiza status para {}", proposta.getDocumento(), proposta.getStatusProposta());
        } catch (FeignException e) {
            logger.error("Proposta  documento={}, não foi possível acessar o serviço de analise financeira. Erro: {}", proposta.getDocumento(), e.getLocalizedMessage());
        }
    }
}
