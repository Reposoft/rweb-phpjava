package se.repos.rweb.php;

import java.io.File;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import se.repos.rweb.config.RepositoryModuleSvn;
import se.simonsoft.cms.backend.svnkit.CmsRepositorySvn;
import se.simonsoft.cms.item.CmsItem;
import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.impl.CmsItemIdUrl;
import se.simonsoft.cms.item.info.CmsItemLookup;

public abstract class ReposPhpBridge {

	public static final Logger logger = LoggerFactory.getLogger(ReposPhpBridge.class);
	
	static Injector getContext(URL repositoryUrl) {
		return getContextNew(repositoryUrl);
	}
	
	static Injector getContextNew(URL repositoryUrl) {
		// assume that type is svn, because that's what repos-web PHP supports
		CmsRepository repo = new CmsRepository(repositoryUrl.toString());
		File parentadmin = new File("/tmp/not-expected-to-use-admin-access-here");
		File repoadmin = new File(parentadmin, repo.getName());
		CmsRepositorySvn repository = new CmsRepositorySvn(repo.getUrl(), repoadmin);
		Injector context = Guice.createInjector(new RepositoryModuleSvn(repository));
		return context;
	}
	
	/**
	 * 
	 * @param base repository URL, encoded
	 * @param target path starting with slash, not encoded
	 * @return
	 */
	public static CmsItem getItem(URL repositoryUrl, String target) {
		logger.debug("Read info for {} {}", repositoryUrl, target);
		CmsItemPath path = new CmsItemPath(target);
		Injector context = getContext(repositoryUrl);
		CmsItemId id = new CmsItemIdUrl(context.getInstance(CmsRepository.class), path);
		CmsItemLookup lookup = context.getInstance(CmsItemLookup.class);
		CmsItem item = lookup.getItem(id);
		logger.info("Got {}", item);
		
		return item;
	}
	
	/**
What _readInfoSvn returned: Array
(
    [path] => lock-oo.png
    [revision] => 92
    [kind] => file
    [url] => http://localdev:8530/svn/repo1/qa/Images/lock-oo.png
    [relative-url] => ^/qa/Images/lock-oo.png
    [root] => http://localdev:8530/svn/repo1
    [uuid] => 50217dbe-7a01-4030-aaaa-f1baa9200a11
    [lastChangedRevision] => 58
    [author] => solsson
    [date] => 2010-08-18T09:11:12.899021Z
    [lockcreated] => 1970-01-01T00:00:00.000000Z
    [name] => lock-oo.png
)
	 */	
	public static Map<String, String> getInfo(URL repositoryUrl, String path) {
		Map<String, String> info = new LinkedHashMap<String, String>();
		CmsItem item = getItem(repositoryUrl, path);
		info.put("revision", Long.toString(item.getRevisionChanged().getNumber()));
		info.put("kind", item.getKind().getKind());
		info.put("url", item.getId().getUrl());
		info.put("author", item.getRevisionChangedAuthor());
		info.put("date", item.getRevisionChanged().getDateIso());
		// TODO lock info
		return info;
	}
	
	public static boolean isWritable(URL fileUrl) {
		// the worst that can happen with false positives, given repos authproxy concept, is that an operation will fail
		// but repos-web needs this info for usability also
		return true; // TODO implement access control check
		// we could use repos-web style conditional requests, or ask svn access file
	}
	
	public static boolean isWritableFolder(URL fileUrl) {
		// the worst that can happen with false positives, given repos authproxy concept, is that an operation will fail
		// but repos-web needs this info for usability also
		return true; // TODO implement access control check
	}	
	
}