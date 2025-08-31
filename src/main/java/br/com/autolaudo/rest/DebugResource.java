package br.com.autolaudo.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

@Path("/debug")
public class DebugResource {

    @GET
    @Path("/env")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEnvironmentVariables() {
        // Pega todas as variáveis de ambiente que a aplicação está recebendo
        Map<String, String> env = System.getenv();
        
        // Retorna o mapa de variáveis como uma resposta JSON
        return Response.ok(env).build();
    }
}