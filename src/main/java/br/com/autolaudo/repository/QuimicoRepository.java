package br.com.autolaudo.repository;

import br.com.autolaudo.models.Quimico;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QuimicoRepository implements PanacheMongoRepository<Quimico> {

    
    
}
