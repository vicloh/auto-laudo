package br.com.autolaudo.restClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import br.com.autolaudo.dto.DadosEmpresaDTO;

@RegisterRestClient(configKey="brasil-api")
public interface BrasilApiClient {

    @GET
    @Path("/cnpj/v1/{cnpj}")
    DadosEmpresaDTO buscarPorCnpj(@PathParam("cnpj") String cnpj);
}