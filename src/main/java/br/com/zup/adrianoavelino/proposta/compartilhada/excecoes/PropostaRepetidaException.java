package br.com.zup.adrianoavelino.proposta.compartilhada.excecoes;

import org.springframework.http.HttpStatus;

public class PropostaRepetidaException extends RegraDeNegocioException {
    public PropostaRepetidaException(String mensagem, HttpStatus httpStatus) {
        super(mensagem, httpStatus);
    }
}
