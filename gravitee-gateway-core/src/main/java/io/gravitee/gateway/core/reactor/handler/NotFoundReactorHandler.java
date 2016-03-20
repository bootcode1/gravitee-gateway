/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.gateway.core.reactor.handler;

import io.gravitee.common.http.HttpHeaders;
import io.gravitee.common.http.HttpStatusCode;
import io.gravitee.gateway.api.Request;
import io.gravitee.gateway.api.Response;
import io.gravitee.gateway.api.buffer.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

/**
 * @author David BRASSELY (brasseld at gmail.com)
 */
public class NotFoundReactorHandler extends AbstractReactorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotFoundReactorHandler.class);

    private final String message;

    public NotFoundReactorHandler(Environment environment) {
        this.message =
                environment.getProperty("http.errors[404].message", "No context-path matches the request URI.");
    }

    @Override
    public void handle(Request request, Response response, io.gravitee.gateway.api.handler.Handler<Response> handler) {
        LOGGER.debug("No Gravitee handler can be found for request {}, returns NOT_FOUND(404)", request.path());

        response.status(HttpStatusCode.NOT_FOUND_404);

        response.headers().set(HttpHeaders.CONTENT_LENGTH, Integer.toString(message.length()));
        response.headers().set(HttpHeaders.CONTENT_TYPE, "text/plain");
        response.write(Buffer.buffer(message));

        response.end();
        handler.handle(response);
    }
}
