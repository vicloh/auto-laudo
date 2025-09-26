package br.com.autolaudo.rest;

import br.com.autolaudo.dto.request.LoginRequest;
import br.com.autolaudo.dto.request.RefreshRequest;
import br.com.autolaudo.dto.response.LoginResponse;
import br.com.autolaudo.models.User;
import br.com.autolaudo.services.TokenService;
import br.com.autolaudo.services.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    UserService userService;

    @Inject
    TokenService tokenService;

    // Endpoint de login
    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        User user = userService.findByEmail(request.getEmail());
        if (user == null || !userService.checkPassword(request.getPassword(), user.getPasswordHash())) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciais inválidas").build();
        }
        String accessToken = tokenService.generateAccessToken(user.getEmail());
        String refreshToken = tokenService.generateRefreshToken();
        userService.updateRefreshToken(user, refreshToken);
        return Response.ok(new LoginResponse(accessToken, refreshToken)).build();
    }

    // Endpoint de refresh
    @POST
    @Path("/refresh")
    public Response refresh(RefreshRequest request) {
        User user = userService.findByRefreshToken(request.getRefreshToken());
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Refresh token inválido").build();
        }
        String accessToken = tokenService.generateAccessToken(user.getEmail());
        String newRefreshToken = tokenService.generateRefreshToken();
        userService.updateRefreshToken(user, newRefreshToken);
        return Response.ok(new LoginResponse(accessToken, newRefreshToken)).build();
    }
}