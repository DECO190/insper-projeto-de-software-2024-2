package br.insper.aposta.aposta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApostaService {

    @Autowired
    private ApostaRepository apostaRepository;

    public void salvar(Aposta aposta) {
        aposta.setId(UUID.randomUUID().toString());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RetornarPartidaDTO> partida = restTemplate.getForEntity(
                "http://localhost:8080/partida/" + aposta.getIdPartida(),
                RetornarPartidaDTO.class);

        if (partida.getStatusCode().is2xxSuccessful())  {
            apostaRepository.save(aposta);
        }

    }

    public List<Aposta> listar(String filtroStatus) {
        if (!filtroStatus.isEmpty()) {
            return apostaRepository.findByResultado(filtroStatus);
        }

        return apostaRepository.findAll();
    }

    public String pegarStatus(String apostaId) {
        Optional<Aposta> apostaQuery = apostaRepository.findById(apostaId);

        if (!apostaQuery.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi possível achar a aposta!");
        }

        Aposta aposta = apostaQuery.get();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RetornarPartidaDTO> partidaReq = restTemplate.getForEntity("http://localhost:8080/partida/" + aposta.getIdPartida(), RetornarPartidaDTO.class);

        if (partidaReq.getStatusCode().is4xxClientError() || partidaReq.getStatusCode().is5xxServerError()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi possível achar a partida!");
        }

        RetornarPartidaDTO partida = partidaReq.getBody();

        if (partida.getStatus().equals("REALIZADA")) {
            if (
                (aposta.getResultado().equals("EMPATE") && partida.getPlacarMandante().equals(partida.getPlacarVisitante()))
                ||
                (aposta.getResultado().equals("VITORIA_MANDANTE") && partida.getPlacarMandante() > partida.getPlacarVisitante())
                ||
                (aposta.getResultado().equals("VITORIA_VISITANTE") && partida.getPlacarMandante() < partida.getPlacarVisitante())
            ) {
                aposta.setStatus("GANHOU");
            } else {
                aposta.setStatus("PERDIDA");
            }

            apostaRepository.save(aposta);
        }

        return aposta.getStatus();
    }

}
