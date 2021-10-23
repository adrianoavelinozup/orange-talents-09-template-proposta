package br.com.zup.adrianoavelino.proposta.proposta;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "analiseFinanceira", url = "localhost:9999/api")
public interface AnaliseFinanceiraCliente {
    @RequestMapping(method = RequestMethod.POST, value = "/solicitacao")
    ResponseEntity<ResultadoAnaliseResponse> solicitar(SolicitacaoAnaliseRequest solicitacaoAnaliseRequest);
}
