package dev.matheuscruz;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import io.dapr.client.domain.State;
import io.quarkiverse.dapr.core.SyncDaprClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/users")
public class UserResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    SyncDaprClient dapr;

    UserResource(SyncDaprClient dapr) {
        this.dapr = dapr;
    }

    @POST
    public Response create(Map<String, String> body) {
        UUID uuid = UUID.randomUUID();
        this.dapr.saveState("kvstore", uuid.toString(), body);
        LOGGER.info("[service:pub] Saving user with ID {}", uuid);
        return Response.created(URI.create("/users/" + uuid)).build();
    }

    @GET
    @Path("/{userId}")
    public Response get(@PathParam("userId") String userId) {
        State<Map> userState = this.dapr.getState("kvstore", userId, Map.class);
        return Response.ok(userState.getValue()).build();
    }

}
