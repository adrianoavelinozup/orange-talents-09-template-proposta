package br.com.zup.adrianoavelino.proposta.compartilhada.seguranca;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OfuscadorTest {

    @Test
    @DisplayName("Deve ofuscar documento a partir do quarto caractere ")
    void test1(){
        String documento = "123.456.789-11";
        String documentoOfuscado = Ofuscador.documento(documento);
        Assertions.assertTrue(documentoOfuscado.startsWith("123.*"));
    }

    @Test
    @DisplayName("Deve finalizar a ofuscação do documento antes dos 3 últimos caracteres")
    void test2(){
        String documento = "123.456.789-11";
        String documentoOfuscado = Ofuscador.documento(documento);
        Assertions.assertTrue(documentoOfuscado.endsWith("*-11"));
    }

    @Test
    @DisplayName("Deve ofuscar documento")
    void test3(){
        String documento = "123.456.789-11";
        String documentoOfuscado = Ofuscador.documento(documento);
        String esperado = "123.*******-11";
        Assertions.assertEquals(esperado, documentoOfuscado);
    }

    @Test
    @DisplayName("Deve ofuscar os números iniciais do cartão, antes dos 4 últimos carcateres")
    void test4(){
        String numeroCartao =  "4716750056121368";
        String cartaoOfucado = Ofuscador.numeroCartao(numeroCartao);
        Assertions.assertTrue(cartaoOfucado.startsWith("************1"));
    }

    @Test
    @DisplayName("Não deve ofuscar os 4 últimos carcateres do cartão")
    void test5(){
        String numeroCartao =  "4716750056121368";
        String cartaoOfucado = Ofuscador.numeroCartao(numeroCartao);
        Assertions.assertTrue(cartaoOfucado.endsWith("*1368"));
    }

    @Test
    @DisplayName("Deve ofuscar os carcateres do cartão")
    void test6(){
        String numeroCartao =  "4716750056121368";
        String cartaoOfucado = Ofuscador.numeroCartao(numeroCartao);
        String esperado = "************1368";
        Assertions.assertEquals(esperado, cartaoOfucado);
    }

}
