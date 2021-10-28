package br.com.zup.adrianoavelino.proposta.compartilhada.excecoes;

import org.springframework.http.HttpStatus;

public class EntidadeNaoEncontradaException extends RegraDeNegocioException {
    public EntidadeNaoEncontradaException(String mensagem, HttpStatus httpStatus) {
        super(mensagem, httpStatus);
    }
}
