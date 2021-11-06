package br.com.zup.adrianoavelino.proposta.compartilhada.seguranca;

import br.com.zup.adrianoavelino.proposta.compartilhada.excecoes.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class CriptadorAES implements Criptador {
    @Value("${proposta.security.encriptador.chave}")
    private String chave;

    @Value("${proposta.security.encriptador.salt}")
    private String salt;

    private final String ALGORITMO = "AES/CBC/PKCS5Padding";

    @Override
    public String encrypt(String input) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.ENCRYPT_MODE, this.generateKey(), this.generateIv());
            byte[] cipherText = cipher.doFinal(input.getBytes());
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            throw new RegraDeNegocioException("Não foi possível encriptar o texto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String decrypt(String cipherText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, this.generateKey(), this.generateIv());
            byte[] plainText = cipher.doFinal(Base64.getDecoder()
                    .decode(cipherText));
            return new String(plainText);
        } catch (Exception e) {
            throw new RegraDeNegocioException("Não foi possível decriptar o texto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private SecretKey generateKey() {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(chave.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return secret;
        } catch (Exception e) {
            throw new RegraDeNegocioException("Não foi possível gerar chave", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        return new IvParameterSpec(iv);
    }
}
