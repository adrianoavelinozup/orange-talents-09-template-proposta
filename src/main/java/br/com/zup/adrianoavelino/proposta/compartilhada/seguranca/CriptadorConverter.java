package br.com.zup.adrianoavelino.proposta.compartilhada.seguranca;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CriptadorConverter implements AttributeConverter<String, String> {
    @Autowired
    private Criptador cripdatador;

    @Override
    public String convertToDatabaseColumn(String documento) {
       return cripdatador.encriptar(documento);
    }

    @Override
    public String convertToEntityAttribute(String documentoCriptado) {
        return cripdatador.decriptar(documentoCriptado);
    }
}
