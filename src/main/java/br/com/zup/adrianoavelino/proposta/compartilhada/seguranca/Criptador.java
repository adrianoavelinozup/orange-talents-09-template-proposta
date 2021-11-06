package br.com.zup.adrianoavelino.proposta.compartilhada.seguranca;

public interface Criptador {
    String encriptar(String documento);
    String decriptar(String documento);
}
