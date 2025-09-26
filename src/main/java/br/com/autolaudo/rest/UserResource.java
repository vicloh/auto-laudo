package br.com.autolaudo.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.MultipartForm;

import br.com.autolaudo.dto.request.CriarUserQuimicoRequestDTO;
import br.com.autolaudo.dto.request.criarUserAdmRequestDTO;
import br.com.autolaudo.dto.response.QuimicoResponseDTO;
import br.com.autolaudo.models.Quimico;
import br.com.autolaudo.services.QuimicoService;
import br.com.autolaudo.services.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    QuimicoService quimicoService;

    @Inject
    UserService userService;

    @POST
    @Path("/criar/quimico")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(summary = "Cria um novo químico com upload de imagem")
    @APIResponse(responseCode = "200", description = "Imagem salva com sucesso")
    public Response criarQuimico(@SuppressWarnings("removal") @MultipartForm CriarUserQuimicoRequestDTO request) {
        if (userService.emailExists(request.getEmail())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Email já cadastrado").build();
        }
        if (userService.crqExists(request.getCrq()) || quimicoService.crqExists(request.getCrq())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("CRQ já cadastrado").build();
        }

        try {
            quimicoService.salvarQuimico(request);
            userService.registerUserQuimico(
                    request.getEmail(),
                    request.getPassword(),
                    request.getCrq());

            return Response.status(Response.Status.CREATED)
                    .entity("Usuário Quimico cadastrado com sucesso").build();
        } catch (Exception e) {
            return Response.serverError().entity("Erro ao fazer upload: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("/register/adm")
    @Operation(summary = "criar usuário ADM")
    @APIResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public Response registerAdm(@Valid criarUserAdmRequestDTO request) {
        if (userService.emailExists(request.getEmail())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Email já cadastrado").build();
        }

        userService.registerUserAdm(request.getEmail(), request.getPassword());
        
        return Response.status(Response.Status.CREATED)
                .entity("Usuário ADM cadastrado com sucesso").build();
    }

    // Listar todos os químicos (apenas ADM)
    @GET
    @RolesAllowed("ADM")
    public Response listarQuimicos() {
        List<Quimico> quimicos = quimicoService.listarQuimicos();
        List<QuimicoResponseDTO> dtos = quimicos.stream()
            .map(QuimicoResponseDTO::fromEntity)
            .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    // Excluir químico (apenas ADM)
    @DELETE
    @Path("/{crq}")
    @RolesAllowed("ADM")
    @Transactional
    public Response excluirQuimico(@PathParam("crq") String crq) {
        Quimico quimico = quimicoService.buscarPorCrq(crq);
        if (quimico == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Químico não encontrado").build();
        }
        quimicoService.excluirQuimico(crq);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
