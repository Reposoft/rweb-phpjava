package se.repos.rweb.config;

import se.repos.authproxy.ReposCurrentUser;
import se.repos.authproxy.restclient.RestAuthenticationAuthproxy;
import se.repos.authproxy.transfer.AuthproxyTrustedTransfer;
import se.repos.authproxy.transfer.AuthproxyTrustedTransferInstanceShare;
import se.repos.restclient.RestAuthentication;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class GlobalModuleAuthnAuthproxy  extends AbstractModule {

	@Override
	protected void configure() {
		bind(ReposCurrentUser.class).toInstance(ReposCurrentUser.DEFAULT); // Same as authproxy filter
		bind(RestAuthentication.class).to(RestAuthenticationAuthproxy.class);
		// probably not needed yet, but same as in cms webapp now
		bind(AuthproxyTrustedTransfer.class).annotatedWith(Names.named("instanceshare")).to(AuthproxyTrustedTransferInstanceShare.class);
	}

}
