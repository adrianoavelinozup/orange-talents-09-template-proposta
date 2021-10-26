package br.com.zup.adrianoavelino.proposta.proposta;

import br.com.zup.adrianoavelino.proposta.compartilhada.anotacoes.excecoes.RegraDeNegocioException;
import org.springframework.http.HttpStatus;

public class EntidadeNaoEncontradaException extends RegraDeNegocioException {
    public EntidadeNaoEncontradaException(String mensagem, HttpStatus httpStatus) {
        super(mensagem, httpStatus);
    }
}
