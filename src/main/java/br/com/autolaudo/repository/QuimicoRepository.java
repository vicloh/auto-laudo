package br.com.autolaudo.repository;

import java.util.Optional;

import br.com.autolaudo.models.Quimico;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QuimicoRepository implements PanacheMongoRepository<Quimico> {

    public Optional<Quimico> findByCrq(String crq) {
        return Optional.ofNullable(Quimico.find("crq", crq).firstResult());
    }
    
}
