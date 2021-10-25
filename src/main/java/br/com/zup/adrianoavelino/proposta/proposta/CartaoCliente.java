package br.com.zup.adrianoavelino.proposta.proposta;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cartaoCliente", url = "${proposta.sistema-externo.cartoes.url}")
public interface CartaoCliente {
    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<CartaoResponse> consultarCartao(@RequestParam Long idProposta);
}
