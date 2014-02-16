package se.repos.rweb.config;

import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNClientManager;

import se.simonsoft.cms.backend.svnkit.config.SvnKitAuthManagerProvider;
import se.simonsoft.cms.backend.svnkit.config.SvnKitClientManagerProvider;
import se.simonsoft.cms.backend.svnkit.config.SvnKitLowLevelProvider;

import com.google.inject.AbstractModule;

public class RepositoryModuleBackendSvnkitInternals extends AbstractModule {

	@Override
	protected void configure() {
		bind(SVNRepository.class).toProvider(SvnKitLowLevelProvider.class);
		bind(ISVNAuthenticationManager.class).toProvider(SvnKitAuthManagerProvider.class);
		bind(SVNClientManager.class).toProvider(SvnKitClientManagerProvider.class);
	}

}
