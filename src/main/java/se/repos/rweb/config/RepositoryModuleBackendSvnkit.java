package se.repos.rweb.config;

import se.simonsoft.cms.backend.svnkit.CmsRepositorySvn;
import se.simonsoft.cms.backend.svnkit.commit.CmsCommitSvnkitEditor;
import se.simonsoft.cms.backend.svnkit.info.CmsItemLookupSvnkit;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.commit.CmsCommit;
import se.simonsoft.cms.item.info.CmsItemLookup;

import com.google.inject.AbstractModule;

public class RepositoryModuleBackendSvnkit extends AbstractModule {

	private CmsRepositorySvn repository;

	public RepositoryModuleBackendSvnkit(CmsRepositorySvn repository) {
		this.repository = repository;
	}
	
	@Override
	protected void configure() {
		bind(CmsRepository.class).toInstance(repository);
		bind(CmsRepositorySvn.class).toInstance(repository);
		bind(CmsItemLookup.class).to(CmsItemLookupSvnkit.class);
		bind(CmsCommit.class).to(CmsCommitSvnkitEditor.class);
	}

}
