package br.com.zup.adrianoavelino.proposta.compartilhada.seguranca;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

@Component
public class CripdatadorTextEncryptor implements Cryptador {
    @Value("${proposta.security.encriptador.chave}")
    private String chave;

    @Value("${proposta.security.encriptador.salt}")
    private String salt;

    @Override
    public String encrypt(String documento) {
        TextEncryptor textEncryptor = Encryptors.queryableText(chave, salt);
        return textEncryptor.encrypt(documento);
    }

    @Override
    public String decrypt(String documento) {
        TextEncryptor textEncryptor = Encryptors.queryableText(chave, salt);
        return textEncryptor.decrypt(documento);
    }
}
