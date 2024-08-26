package br.insper.aposta.aposta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document

public class Aposta {

    @Id
    private String id;
    private Integer idPartida;
    private LocalDateTime dataAposta;
    private String resultado;  // EMPATE, VITORIA_MANDANTE, VITORIA_VISITANTE
    private Double valor;

    private String status; // GANHOU, PERDEU, REALIZADA

    public String getId() {
        return id;
    }

    public Integer getIdPartida() {
        return idPartida;
    }

    public LocalDateTime getDataAposta() {
        return dataAposta;
    }

    public String getResultado() {
        return resultado;
    }

    public Double getValor() {
        return valor;
    }

    public String getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdPartida(Integer idPartida) {
        this.idPartida = idPartida;
    }

    public void setDataAposta(LocalDateTime dataAposta) {
        this.dataAposta = dataAposta;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
