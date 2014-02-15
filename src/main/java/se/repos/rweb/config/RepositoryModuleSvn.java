package se.repos.rweb.config;

import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNClientManager;

import se.repos.authproxy.ReposCurrentUser;
import se.repos.authproxy.restclient.RestAuthenticationAuthproxy;
import se.repos.authproxy.transfer.AuthproxyTrustedTransfer;
import se.repos.authproxy.transfer.AuthproxyTrustedTransferInstanceShare;
import se.repos.restclient.RestAuthentication;
import se.simonsoft.cms.backend.svnkit.CmsRepositorySvn;
import se.simonsoft.cms.backend.svnkit.commit.CmsCommitSvnkitEditor;
import se.simonsoft.cms.backend.svnkit.config.SvnKitAuthManagerProvider;
import se.simonsoft.cms.backend.svnkit.config.SvnKitClientManagerProvider;
import se.simonsoft.cms.backend.svnkit.config.SvnKitLowLevelProvider;
import se.simonsoft.cms.backend.svnkit.info.CmsItemLookupSvnkit;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.commit.CmsCommit;
import se.simonsoft.cms.item.info.CmsItemLookup;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class RepositoryModuleSvn extends AbstractModule {

	private CmsRepositorySvn repository;

	public RepositoryModuleSvn(CmsRepositorySvn repository) {
		this.repository = repository;
	}
	
	@Override
	protected void configure() {
		bind(CmsRepository.class).toInstance(repository);
		bind(CmsRepositorySvn.class).toInstance(repository);
		bind(CmsItemLookup.class).to(CmsItemLookupSvnkit.class);
		bind(CmsCommit.class).to(CmsCommitSvnkitEditor.class);
		
		// SvnKit setup
		bind(SVNRepository.class).toProvider(SvnKitLowLevelProvider.class);
		bind(ISVNAuthenticationManager.class).toProvider(SvnKitAuthManagerProvider.class);
		bind(SVNClientManager.class).toProvider(SvnKitClientManagerProvider.class);		
		
		// this should be in a generic module
		bind(ReposCurrentUser.class).toInstance(ReposCurrentUser.DEFAULT); // Same as authproxy filter
		bind(RestAuthentication.class).to(RestAuthenticationAuthproxy.class);
		// probably not needed yet, but same as in cms webapp now
		bind(AuthproxyTrustedTransfer.class).annotatedWith(Names.named("instanceshare")).to(AuthproxyTrustedTransferInstanceShare.class);
	}

}
