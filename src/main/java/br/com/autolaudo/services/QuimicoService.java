package br.com.autolaudo.services;

import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;

import br.com.autolaudo.dto.request.CriarUserQuimicoRequestDTO;
import br.com.autolaudo.models.Quimico;
import br.com.autolaudo.repository.QuimicoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class QuimicoService {

    @Inject
    QuimicoRepository quimicoRepository;

    @Inject
    MongoClient mongoClient;

    public void salvarQuimico(CriarUserQuimicoRequestDTO request) {
        MongoDatabase database = mongoClient.getDatabase("autolaudo_db");
        GridFSBucket gridFSBucket = GridFSBuckets.create(database, "imagens");

        GridFSUploadOptions options = new GridFSUploadOptions()
                .chunkSizeBytes(358400)
                .metadata(new org.bson.Document("nome", request.getNomeQuimico()));

        ObjectId fileId = gridFSBucket.uploadFromStream(request.getNomeQuimico(), request.getArquivo(), options);

        Quimico quimico = new Quimico();
        quimico.setNome(request.getNomeQuimico());
        quimico.setCrq(request.getCrq());
        quimico.setRegiao(request.getRegiao());
        quimico.setCaminhoAssinatura(fileId.toHexString());
        quimicoRepository.persist(quimico);
    }

    public Quimico buscarPorCrq(String crq) {
        return quimicoRepository.find("crq", crq).firstResult();
    }

    public boolean crqExists(String crq) {
        return quimicoRepository.findByCrq(crq).orElse(null) != null;
    }

    public void excluirQuimico(String crq) {
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

    public List<Quimico> listarQuimicos() {
        return quimicoRepository.listAll();
    }
}
