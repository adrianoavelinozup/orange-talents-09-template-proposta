package br.com.zup.adrianoavelino.proposta.compartilhada.seguranca;

public interface Criptador {
    String encrypt(String documento);
    String decrypt(String documento);
}
