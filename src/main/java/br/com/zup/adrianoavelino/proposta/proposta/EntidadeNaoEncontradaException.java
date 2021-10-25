package br.com.zup.adrianoavelino.proposta.proposta;

import br.com.zup.adrianoavelino.proposta.compartilhada.anotacoes.excecoes.RegraDeNegocioException;

public class EntidadeNaoEncontradaException extends RegraDeNegocioException {
    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
