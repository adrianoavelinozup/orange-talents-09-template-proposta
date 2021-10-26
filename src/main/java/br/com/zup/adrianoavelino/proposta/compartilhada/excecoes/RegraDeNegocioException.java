package br.com.zup.adrianoavelino.proposta.compartilhada.excecoes;

import org.springframework.http.HttpStatus;

public class RegraDeNegocioException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String mensagem;

    public RegraDeNegocioException(String mensagem, HttpStatus httpStatus) {
        super(mensagem);
        this.mensagem = mensagem;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMensagem() {
        return mensagem;
    }
}
