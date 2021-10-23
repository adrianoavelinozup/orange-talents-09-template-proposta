package br.com.zup.adrianoavelino.proposta.proposta;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "analiseFinanceira", url = "${proposta.sistema-externo-analise-financeira.url}")
public interface AnaliseFinanceiraCliente {
    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<ResultadoAnaliseResponse> solicitar(SolicitacaoAnaliseRequest solicitacaoAnaliseRequest);
}
