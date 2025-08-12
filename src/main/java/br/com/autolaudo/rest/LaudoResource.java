package br.com.autolaudo.rest;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.com.autolaudo.dto.DadosEmpresaDTO;
import br.com.autolaudo.restClient.BrasilApiClient;
import br.com.autolaudo.services.PdfService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/laudo")
public class LaudoResource {

    @Inject
    @RestClient
    BrasilApiClient brasilApiClient;

    @Inject
    PdfService pdfService;

    @GET
    @Path("/{cnpj}")
    @Produces(MediaType.APPLICATION_JSON)
    public DadosEmpresaDTO consultarCnpj(@PathParam("cnpj") String cnpj) {
        return brasilApiClient.buscarPorCnpj(cnpj);
    }

    @POST
    @Path("/gerar/dedetizacao/{cnpj}/{crq}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/pdf")
    public Response gerarLaudoDedetizacaoPdf(@PathParam("cnpj") String cnpj, @PathParam("crq") String crq) {

        DadosEmpresaDTO dadosEmpresa = brasilApiClient.buscarPorCnpj(cnpj);
        try {
            byte[] pdfBytes = pdfService.gerarLaudoDedetizacao(
                brasilApiClient.buscarPorCnpj(cnpj),
                crq
            );
            
            String nomeArquivo = "LAUDO "+dadosEmpresa.getRazaoSocial().toUpperCase()+" - WEDEX - 2025 ASSINADO";

            return Response.ok(pdfBytes)
                .header("Content-Disposition", "attachment; filename=\"" + nomeArquivo + "\"")
                .build();

        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao gerar o PDF: " + e.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
        }
    }

    @POST
    @Path("/gerar/limpezaCaixaDagua/{cnpj}/{crq}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/pdf")
    public Response gerarLaudoLimpezaCaixaDaguaPdf(@PathParam("cnpj") String cnpj, @PathParam("crq") String crq) {

        DadosEmpresaDTO dadosEmpresa = brasilApiClient.buscarPorCnpj(cnpj);
        try {
            byte[] pdfBytes = pdfService.gerarLaudoLimpezaCaixaDagua(
                brasilApiClient.buscarPorCnpj(cnpj),
                crq
            );
            
            String nomeArquivo = "LAUDO CAIXA DAGUA "+dadosEmpresa.getRazaoSocial().toUpperCase()+" - WEDEX - 2025 ASSINADO";

            return Response.ok(pdfBytes)
                .header("Content-Disposition", "attachment; filename=\"" + nomeArquivo + "\"")
                .build();

        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao gerar o PDF: " + e.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
        }
    }

    @POST
    @Path("/gerar/dedetizacaoEDesratizacao/{cnpj}/{crq}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/pdf")
    public Response gerarLaudoDedetizacaoEDesratizacaoPdf(@PathParam("cnpj") String cnpj, @PathParam("crq") String crq) {

        DadosEmpresaDTO dadosEmpresa = brasilApiClient.buscarPorCnpj(cnpj);
        try {
            byte[] pdfBytes = pdfService.gerarLaudoDedetizacaoEDesratizacaoPdf(
                brasilApiClient.buscarPorCnpj(cnpj),
                crq

            );
            
            String nomeArquivo = "LAUDO DESRATIZACAO "+dadosEmpresa.getRazaoSocial().toUpperCase()+" - WEDEX - 2025 ASSINADO";

            return Response.ok(pdfBytes)
                .header("Content-Disposition", "attachment; filename=\"" + nomeArquivo + "\"")
                .build();

        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao gerar o PDF: " + e.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
        }
    }
}
