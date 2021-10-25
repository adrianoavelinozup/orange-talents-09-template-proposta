package br.com.zup.adrianoavelino.proposta.proposta;

import br.com.zup.adrianoavelino.proposta.compartilhada.anotacoes.excecoes.RegraDeNegocioException;

public class PropostaNaoElegivelException extends RegraDeNegocioException {
    public PropostaNaoElegivelException(String mensagem) {
        super(mensagem);
    }
}
