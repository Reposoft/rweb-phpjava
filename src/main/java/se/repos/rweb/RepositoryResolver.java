package se.repos.rweb;

import java.net.URL;

import com.google.inject.Injector;

/**
 * Manages creation and reuse of repository contexts.
 */
public interface RepositoryResolver {

	Injector getContext(URL repositoryRoot);
	
	Injector getContext(String repositoryId);
	
}
