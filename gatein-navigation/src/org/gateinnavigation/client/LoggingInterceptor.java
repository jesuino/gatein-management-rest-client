package org.gateinnavigation.client;

import java.util.logging.Logger;

import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.annotations.interception.ClientInterceptor;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.spi.interception.ClientExecutionContext;
import org.jboss.resteasy.spi.interception.ClientExecutionInterceptor;

@ClientInterceptor
@Provider
public class LoggingInterceptor implements ClientExecutionInterceptor {

	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public ClientResponse<?> execute(ClientExecutionContext ctx) throws Exception {
	//	logger.info("Request to " + ctx.getRequest().getUri().toString()
	//			+ " started...");
		ClientResponse<?> cr = ctx.proceed();
	//	logger.info("Request to " + ctx.getRequest().getUri().toString()
	//			+ " finished...");
		return cr;
	}

}
