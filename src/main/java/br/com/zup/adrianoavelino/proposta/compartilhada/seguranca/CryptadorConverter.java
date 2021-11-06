package br.com.zup.adrianoavelino.proposta.compartilhada.seguranca;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CryptadorConverter implements AttributeConverter<String, String> {
    @Autowired
    private Cryptador cripdatador;

    @Override
    public String convertToDatabaseColumn(String documento) {
       return cripdatador.encrypt(documento);
    }

    @Override
    public String convertToEntityAttribute(String documentoCriptado) {
        return cripdatador.decrypt(documentoCriptado);
    }
}
