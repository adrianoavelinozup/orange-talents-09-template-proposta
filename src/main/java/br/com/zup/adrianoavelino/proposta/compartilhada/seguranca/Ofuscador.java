package br.com.zup.adrianoavelino.proposta.compartilhada.seguranca;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 * Ofuscador de dados sensíveis
 */
public class Ofuscador {
    private Ofuscador() {
    }

    /**
     * Ofusca uma palavra com base na posição inicial e final
     * @param palavra @NotBlank é a palavra que será ofuscada
     * @param posicaoInicial @Positive é a posição inicial da palavra que iniciará a ofuscação dos dados
     * @param posicaoFinal @Positive é a posição final da palavra que terminará a ofuscação dos dados
     * @return a palavra ofuscada
     */
    private static String ofuscar(@NotBlank String palavra, @Positive int posicaoInicial, @Positive int posicaoFinal) {
        int totalLetras = palavra.length();
        String[] letras = palavra.split("");
        StringBuilder palavraOfuscada = new StringBuilder();
        for (int i=0; i < totalLetras; i++) {
            if(i >= posicaoInicial && i <= posicaoFinal && !letras[i].contains("@")) {
                palavraOfuscada.append("*");
            } else {
                palavraOfuscada.append(letras[i]);
            }
        }
        return palavraOfuscada.toString();
    }

    /**
     * Ofusca caracteres centrais do documento
     * @param documento String documento
     * @return documento ofuscado. Ex: 123.*******-00
     */
    public static String documento(@NotBlank @Length(min = 5) String documento) {
        return ofuscar(documento, 4, documento.length() -4);
    }

    /**
     * Ofusca os números do cartão antes dos 4 últimos caracteres
     * @param numeroCartao
     * @return número do cartão ofuscado
     */
    public static String numeroCartao(@NotBlank @Length(min = 5) String numeroCartao) {
        return ofuscar(numeroCartao, 0, numeroCartao.length() -5);
    }
}
