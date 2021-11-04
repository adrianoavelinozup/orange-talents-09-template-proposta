package br.com.zup.adrianoavelino.proposta.proposta;

import br.com.zup.adrianoavelino.proposta.avisoviagem.ResultadoAvisoViagemResponse;
import br.com.zup.adrianoavelino.proposta.avisoviagem.SolicitacaoAvisoViagemRequest;
import br.com.zup.adrianoavelino.proposta.bloqueio.ResultadoBloqueioResponse;
import br.com.zup.adrianoavelino.proposta.bloqueio.SolicitacaoBloqueioRequest;
import br.com.zup.adrianoavelino.proposta.carteiradigital.ApiCartaoCarteiraDigitalRequest;
import br.com.zup.adrianoavelino.proposta.carteiradigital.ApiCartaoCarteiraDigitalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@FeignClient(name = "cartaoCliente", url = "${proposta.sistema-externo.cartoes.url}")
public interface CartaoCliente {
    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<CartaoResponse> consultarCartao(@RequestParam Long idProposta);

    @PostMapping("${proposta.sistema-externo.cartoes.url.bloqueio}")
    ResponseEntity<ResultadoBloqueioResponse> bloquearCartao(@PathVariable @NotBlank String numeroCartao,
                                                             @RequestBody @Valid SolicitacaoBloqueioRequest solicitacaoBloqueio);

    @PostMapping("${proposta.sistema-externo.cartoes.url.aviso}")
    ResponseEntity<ResultadoAvisoViagemResponse> avisarViagem(@PathVariable @NotBlank String numeroCartao,
                                                              @RequestBody @Valid SolicitacaoAvisoViagemRequest solicitacaoAvisoViagemRequest);

    @PostMapping("${proposta.sistema-externo.cartoes.url.carteira}")
    ResponseEntity<ApiCartaoCarteiraDigitalResponse> associarCarteiraDigital(@PathVariable String numeroCartao,
                                                                             @RequestBody @Valid ApiCartaoCarteiraDigitalRequest apiCartaoCarteiraDigitalRequest);
}
