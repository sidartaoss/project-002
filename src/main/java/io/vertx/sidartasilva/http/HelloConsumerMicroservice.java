package io.vertx.sidartasilva.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.*;
import io.vertx.ext.web.client.*;
import io.vertx.ext.web.codec.BodyCodec;

public class HelloConsumerMicroservice extends AbstractVerticle {

    private WebClient client;

    @Override
    public void start() {
        client = WebClient.create(vertx);

        Router router = Router.router(vertx);

        router.get("/").handler(this::invokeMyFirstMicroservice);

        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(8081);
    }

    private void invokeMyFirstMicroservice(RoutingContext rc) {

        HttpRequest<JsonObject> request = client
            .get(
                80
                , System.getenv("HOST")
                , "/vert.x")
            .as(BodyCodec.jsonObject());

            request.send(ar -> {
                if (ar.failed()) {
                    rc.fail(ar.cause());
                } else {
                    rc.response().end(ar.result().body().encode());
                }
            });

    }

}
