package br.com.zup.adrianoavelino.proposta.compartilhada.excecoes;

import org.springframework.http.HttpStatus;

public class PropostaNaoElegivelException extends RegraDeNegocioException {
    public PropostaNaoElegivelException(String mensagem, HttpStatus httpStatus) {
        super(mensagem, httpStatus);
    }
}
