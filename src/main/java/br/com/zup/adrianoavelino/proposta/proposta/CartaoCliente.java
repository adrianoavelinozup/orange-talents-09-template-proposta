package br.com.zup.adrianoavelino.proposta.proposta;

import br.com.zup.adrianoavelino.proposta.bloqueio.ResultadoBloqueioResponse;
import br.com.zup.adrianoavelino.proposta.bloqueio.SolicitacaoBloqueioRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@FeignClient(name = "cartaoCliente", url = "${proposta.sistema-externo.cartoes.url}")
public interface CartaoCliente {
    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<CartaoResponse> consultarCartao(@RequestParam Long idProposta);

    @PostMapping("/{numeroCartao}/bloqueios")
    ResponseEntity<ResultadoBloqueioResponse> bloquearCartao(@PathVariable @NotBlank String numeroCartao,
                                                             @RequestBody @Valid SolicitacaoBloqueioRequest solicitacaoBloqueio);
}
