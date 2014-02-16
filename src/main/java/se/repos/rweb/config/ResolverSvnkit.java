package se.repos.rweb.config;

import java.io.File;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import se.repos.rweb.RepositoryResolver;
import se.simonsoft.cms.backend.svnkit.CmsRepositorySvn;
import se.simonsoft.cms.item.CmsRepository;

/**
 * Assumes that every repository's type is svn, because that's what repos-web PHP supports.
 */
public class ResolverSvnkit implements RepositoryResolver {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Injector global = null;
	
	@Override
	public Injector getContext(URL repositoryRoot) {
		return getContextNew(repositoryRoot);
	}
	
	@Override
	public Injector getContext(String repositoryId) {
		throw new UnsupportedOperationException("not implemented");
	}
	
	protected Module getSvnkit(CmsRepositorySvn repository) {
		return new RepositoryModuleBackendSvnkit(repository);
	}
	
	protected Injector getContextParent() {
		if (global == null) {
			logger.info("Creating new global context");
			global = getContextParentNew();
		}
		return global;
	}
	
	protected Injector getContextParentNew() {
		return Guice.createInjector(
				new GlobalModuleAuthnAuthproxy()
			);
	}
	
	protected Injector getContextNew(URL repositoryUrl) {
		logger.info("Creating new repository context for {}", repositoryUrl);
		CmsRepository repo = new CmsRepository(repositoryUrl.toString());
		File parentadmin = new File("/tmp/not-expected-to-use-admin-access-here");
		File repoadmin = new File(parentadmin, repo.getName());
		CmsRepositorySvn repository = new CmsRepositorySvn(repo.getUrl(), repoadmin);
		return getContextNew(getContextParent(), repository);
	}
	
	protected Injector getContextNew(Injector parent, CmsRepositorySvn repository) {
		return parent.createChildInjector(
				new RepositoryModuleBackendSvnkit(repository),
				new RepositoryModuleBackendSvnkitInternals()
			);
	}

}
