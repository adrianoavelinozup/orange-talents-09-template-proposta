package br.com.zup.adrianoavelino.proposta.compartilhada.excecoes;

import org.springframework.http.HttpStatus;

public class Base64Invalido extends RegraDeNegocioException {
    public Base64Invalido(String mensagem, HttpStatus httpStatus) {
        super(mensagem, httpStatus);
    }
}
