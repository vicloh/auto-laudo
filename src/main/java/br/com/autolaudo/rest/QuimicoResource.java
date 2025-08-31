package br.com.autolaudo.rest;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import br.com.autolaudo.dto.ImagemFormDTO;
import br.com.autolaudo.dto.QuimicoResponseDTO;
import br.com.autolaudo.models.Quimico;
import br.com.autolaudo.services.QuimicoService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;

import org.jboss.resteasy.reactive.MultipartForm;

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

    @POST
    @Path("/criar/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(summary = "Cria químico com upload de assinatura")
    @APIResponse(responseCode = "200", description = "Imagem salva com sucesso")
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

            return Response.ok(quimico).build();
        } catch (Exception e) {
            return Response.serverError().entity("Erro ao fazer upload: " + e.getMessage()).build();
        }
    }

    @GET
    @Operation(summary = "Lista todos os químicos")
    @APIResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response listarTodos() {
        List<QuimicoResponseDTO> lista = Quimico.<Quimico>listAll()
                .stream()
                .map(q -> new QuimicoResponseDTO(
                        q.getNome(),
                        q.getCrq(),
                        q.getRegiao(),
                        q.getCaminhoAssinatura()))
                .toList();

        return Response.ok(lista, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Busca químico por ID")
    @APIResponse(responseCode = "200", description = "Químico encontrado")
    @APIResponse(responseCode = "404", description = "Químico não encontrado")
    public Response buscarPorId(@PathParam("id") String id) {
        try {
            Quimico quimico = Quimico.findById(new ObjectId(id));
            if (quimico == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Químico não encontrado").build();
            }
            return Response.ok(quimico).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Formato de ID inválido: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Atualiza um químico")
    @APIResponse(responseCode = "200", description = "Químico atualizado com sucesso")
    @APIResponse(responseCode = "404", description = "Químico não encontrado")
    public Response atualizar(@PathParam("id") String id, Quimico quimicoAtualizado) {
        try {
            Quimico quimico = Quimico.findById(new ObjectId(id));
            if (quimico == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Químico não encontrado").build();
            }

            quimico.nome = quimicoAtualizado.nome;
            quimico.crq = quimicoAtualizado.crq;
            quimico.regiao = quimicoAtualizado.regiao;
            quimico.caminhoAssinatura = quimicoAtualizado.caminhoAssinatura;

            quimico.update();

            return Response.ok(quimico).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Formato de ID inválido" + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{crq}")
    @Operation(summary = "Deleta um químico pelo CRQ")
    @APIResponse(responseCode = "202", description = "Químico deletado com sucesso")
    @APIResponse(responseCode = "400", description = "Formato de CRQ inválido")
    public Response deletar(@PathParam("crq") String crq) {
        try {
            quimicoService.excluirQuimico(crq);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(crq)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Formato de CRQ inválido: " + e.getMessage())
                    .build();
        }
    }
}
