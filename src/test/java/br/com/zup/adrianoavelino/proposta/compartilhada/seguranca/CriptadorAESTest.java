package br.com.zup.adrianoavelino.proposta.compartilhada.seguranca;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CriptadorAESTest {
    @Autowired
    private CriptadorAES criptadorAES;

    @Test
    @DisplayName("Deve gerar criptografia simétrica")
    void deverGerarCriptografiaSimetrica ( ) {
        String documento = "563.734.420-55";
        String documentoCriptado = criptadorAES.encriptar(documento);
        String documentoDecriptado = criptadorAES.decriptar(documentoCriptado);
        Assertions.assertEquals(documento, documentoDecriptado);
    }
}