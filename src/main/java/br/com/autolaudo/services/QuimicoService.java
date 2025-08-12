package br.com.autolaudo.services;

import br.com.autolaudo.models.Quimico;
import br.com.autolaudo.repository.QuimicoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class QuimicoService {
    
    @Inject
    QuimicoRepository quimicoRepository;
    
    public Quimico salvarQuimico(Quimico quimico) {
        System.out.println("Salvando Químico: " + quimico.getNome());
        quimicoRepository.persist(quimico);
        
        return quimico; 
    }
    
    public Quimico buscarPorCrq(String crq) {
        return quimicoRepository.find("crq", crq).firstResult();
    }

    public void excluirQuimico(String crq){
        Quimico quimico = quimicoRepository.find("crq", crq).firstResult();
        try {
            if (quimico != null) {
                quimicoRepository.delete(quimico);
            } else {
                throw new Exception("Químico não encontrado com CRQ: " + crq);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir químico: " + e.getMessage(), e);
        }
    }
}
