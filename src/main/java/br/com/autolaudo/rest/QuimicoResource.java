package br.com.autolaudo.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import br.com.autolaudo.dto.CriarQuimicoDTO;
import br.com.autolaudo.dto.ImagemFormDTO;
import br.com.autolaudo.models.Quimico;
import br.com.autolaudo.services.QuimicoService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.nio.file.Paths;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import io.quarkus.mongodb.MongoClientName;
import org.bson.types.ObjectId;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;


@Path("/quimicos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuimicoResource {

    @Inject
    QuimicoService quimicoService;

    @Inject
    MongoClient mongoClient;

    @GET
    @Path("/teste")
    public String teste() {
        return mongoClient.getDatabase("autolaudo_db").getName();
    }

    @POST
    @Path("/Criar/Quimico")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@MultipartForm ImagemFormDTO form) {
        try {
            MongoDatabase database = mongoClient.getDatabase("autolaudo_db");
            GridFSBucket gridFSBucket = GridFSBuckets.create(database, "imagens");

            GridFSUploadOptions options = new GridFSUploadOptions()
                    .chunkSizeBytes(358400)
                    .metadata(new org.bson.Document("nome", form.nome));

            ObjectId fileId = gridFSBucket.uploadFromStream(form.nome, form.arquivo, options);

            Quimico quimico = new Quimico();

            quimico.setNome(form.getNomeQuimico());
            quimico.setCrq(form.getCrq());
            quimico.setRegiao(form.getRegiao());
            quimico.setCaminhoAssinatura(fileId.toHexString());

            quimicoService.salvarQuimico(quimico);

            return Response.ok("Imagem salva com ID: " + fileId.toHexString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Erro ao fazer upload: " + e.getMessage()).build();
        }
        
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "LISTAR QUIMICOS")
    public List<Quimico> listarTodos() {
        return Quimico.listAll();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") String id) {
        try {
            Quimico quimico = Quimico.findById(new ObjectId(id));
            return quimico != null ? Response.ok(quimico).build() : Response.status(Status.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Status.BAD_REQUEST).entity("Formato de ID inválido.").build();
        }
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "criar quimico")
    public Response criar(CriarQuimicoDTO quimicoDTO) {

        Quimico quimico = new Quimico();

        quimico.setNome(quimicoDTO.getNome());
        quimico.setCrq(quimicoDTO.getCrq());
        quimico.setRegiao(quimicoDTO.getRegiao());
        quimico.setCaminhoAssinatura(quimicoDTO.getCaminhoAssinatura());

        quimicoService.salvarQuimico(quimico);
        return Response.status(Status.CREATED).entity(quimico).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response atualizar(@PathParam("id") String id, Quimico quimicoAtualizado) {
        try {
            Quimico quimico = Quimico.findById(new ObjectId(id));
            if (quimico == null) {
                return Response.status(Status.NOT_FOUND).build();
            }

            // Atualiza os campos do objeto encontrado com os dados recebidos
            quimico.nome = quimicoAtualizado.nome;
            quimico.crq = quimicoAtualizado.crq;
            quimico.regiao = quimicoAtualizado.regiao;
            quimico.caminhoAssinatura = quimicoAtualizado.caminhoAssinatura;
            
            // Com Panache e MongoDB, usamos o método update().
            quimico.update();
            
            return Response.ok(quimico).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Status.BAD_REQUEST).entity("Formato de ID inválido.").build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{crq}")
    public Response deletar(@PathParam("crq") String crq) {
        try {
            quimicoService.excluirQuimico(crq);
            return Response.status(Status.ACCEPTED).entity("Químico deletado com sucesso.").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Status.BAD_REQUEST).entity("Formato de CRQ inválido.").build();
        }
    }
}