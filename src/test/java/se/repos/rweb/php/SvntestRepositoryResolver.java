package se.repos.rweb.php;

import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNClientManager;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provider;

import se.repos.rweb.config.RepositoryModuleBackendSvnkit;
import se.repos.rweb.config.ResolverSvnkit;
import se.simonsoft.cms.backend.svnkit.CmsRepositorySvn;
import se.simonsoft.cms.backend.svnkit.config.SvnKitAuthManagerProvider;
import se.simonsoft.cms.backend.svnkit.config.SvnKitClientManagerProvider;
import se.simonsoft.cms.testing.svn.CmsTestRepository;

public class SvntestRepositoryResolver extends ResolverSvnkit {

	private CmsTestRepository testrepo;

	public SvntestRepositoryResolver(CmsTestRepository repository) {
		this.testrepo = repository;
	}
	
	@Override
	protected Injector getContextNew(Injector parent,
			CmsRepositorySvn repository) {
		return parent.createChildInjector(
				new RepositoryModuleBackendSvnkit(repository),
				new CmsTestingSvnkitModule()
			);
	}

	class CmsTestingSvnkitModule extends AbstractModule {

		@Override
		protected void configure() {
			bind(SVNRepository.class).toInstance(testrepo.getSvnkit());
			bind(ISVNAuthenticationManager.class).toProvider(SvnKitAuthManagerProvider.class);
			bind(SVNClientManager.class).toProvider(SvnKitClientManagerProvider.class);			
		}
		
	}
	
}
