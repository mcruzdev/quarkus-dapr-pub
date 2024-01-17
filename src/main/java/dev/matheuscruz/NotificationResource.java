package dev.matheuscruz;

import java.util.UUID;

import io.quarkiverse.dapr.core.SyncDaprClient;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/notifications")
public class NotificationResource {

    SyncDaprClient dapr;

    NotificationResource(SyncDaprClient dapr) {
        this.dapr = dapr;
    }

    @POST
    public String create() {
        UUID uuid = UUID.randomUUID();
        this.dapr.publishEvent("notification-created", uuid);
        return String.format("Event sent to notification-created: ID %s", uuid);
    }

}
