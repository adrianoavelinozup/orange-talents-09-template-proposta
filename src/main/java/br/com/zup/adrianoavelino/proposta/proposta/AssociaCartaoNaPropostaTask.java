package br.com.zup.adrianoavelino.proposta.proposta;


import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AssociaCartaoNaPropostaTask {

    private final Logger logger = LoggerFactory.getLogger(AssociaCartaoNaPropostaTask.class);

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private CartaoCliente cartaoCliente;

    @Value("${proposta.sistema-externo.cartoes.quantidade-itens-por-consulta}")
    private Long quantidadeItensPorConsulta;

    @Scheduled(fixedDelayString = "${proposta.sistema-externo.cartoes.intervalo-tempo-execucao-tarefa}")
    public void associarCartao() {
        List<Proposta> propostasElegiveis = propostaRepository.findByPropostaElegivelSemCartao(quantidadeItensPorConsulta);
        if (propostasElegiveis.isEmpty()) return;

        logger.info("Realiza consulta de cartão para propostas elegíveis {}", LocalDateTime.now());
        List<Proposta> propostaElegiveisComCartaoAprovado = new ArrayList<>();
        propostasElegiveis.forEach( proposta -> {
            try {
                ResponseEntity<CartaoResponse> response = cartaoCliente.consultarCartao(proposta.getId());
                propostaElegiveisComCartaoAprovado.add(proposta);
                Cartao cartao = response.getBody().toModel(propostaRepository);
                proposta.associarCartao(cartao);
                logger.info("Proposta id={} associada ao cartão {}", proposta.getId(), cartao.getNumeroCartao());
            } catch (FeignException.FeignServerException ex) {
                logger.warn("Proposta id={} não possui cartão disponível", ex.getMessage());
            } catch (FeignException ex) {
                logger.warn("Erro não esperado: {}", ex.getMessage());
            }
        });

        propostaRepository.saveAll(propostaElegiveisComCartaoAprovado);
    }
}
