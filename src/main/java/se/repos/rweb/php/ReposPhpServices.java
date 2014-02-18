package se.repos.rweb.php;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

import se.repos.rweb.RepositoryResolver;
import se.simonsoft.cms.item.CmsItem;
import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.impl.CmsItemIdUrl;
import se.simonsoft.cms.item.info.CmsItemLookup;
import se.simonsoft.cms.item.info.CmsItemNotFoundException;

public class ReposPhpServices {

	private final Logger logger = LoggerFactory.getLogger(ReposPhpServices.class);
	
	private RepositoryResolver repositoryResolver;

	public ReposPhpServices(RepositoryResolver repositoryResolver) {
		this.repositoryResolver = repositoryResolver;
	}
	
	/**
	 * 
	 * @param base repository URL, encoded
	 * @param target path starting with slash, not encoded
	 * @return
	 */
	CmsItem getItem(URL repositoryUrl, String target) throws CmsItemNotFoundException {
		logger.debug("Read info for {} {}", repositoryUrl, target);
		CmsItemPath path = new CmsItemPath(target);
		Injector context = repositoryResolver.getContext(repositoryUrl);
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
	Map<String, Object> getInfo(URL repositoryUrl, String path) {
		Map<String, Object> info = new LinkedHashMap<String, Object>();
		CmsItem item;
		try {
			item = getItem(repositoryUrl, path);
		} catch (CmsItemNotFoundException e) {
			info.put("path", path);
			return info; // How SvnOpenFile returned non-existent items
		}
		info.put("lastChangedRevision", item.getRevisionChanged().getNumber());
		info.put("kind", item.getKind().getKind());
		info.put("url", item.getId().getUrl());
		info.put("author", item.getRevisionChangedAuthor());
		info.put("date", item.getRevisionChanged().getDateIso() + "Z");
		// TODO lock info
		return info;
	}
	
	boolean isWritable(URL fileUrl) {
		// the worst that can happen with false positives, given repos authproxy concept, is that an operation will fail
		// but repos-web needs this info for usability also
		return true; // TODO implement access control check
		// we could use repos-web style conditional requests, or ask svn access file
	}
	
	boolean isWritableFolder(URL fileUrl) {
		// the worst that can happen with false positives, given repos authproxy concept, is that an operation will fail
		// but repos-web needs this info for usability also
		return true; // TODO implement access control check
	}	
	
}
