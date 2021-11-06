package br.com.zup.adrianoavelino.proposta.compartilhada.seguranca;

public interface Cryptador {
    String encrypt(String documento);
    String decrypt(String documento);
}
