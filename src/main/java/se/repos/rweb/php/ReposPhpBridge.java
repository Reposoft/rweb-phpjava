package se.repos.rweb.php;

import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.repos.rweb.RepositoryResolver;
import se.repos.rweb.config.ResolverSvnkit;
import se.simonsoft.cms.item.CmsItem;

/**
 * Interface towards arbitrary positions in the PHP code where we're migrating towards java serviecs.
 * Static so we can handle instance management here, because Repos PHP can keep state only per request.
 * 
 * Only trivial services that need no testing may be implemented here.
 * For actual service javadoc see {@link ReposPhpServices}.
 */
public abstract class ReposPhpBridge {

	private static final Logger logger = LoggerFactory.getLogger(ReposPhpBridge.class);
	
	private static ReposPhpServices services = null;
	
	static {
		init();
	}
	
	private static void init() {
		RepositoryResolver resolver = new ResolverSvnkit();
		services = new ReposPhpServices(resolver);
		logger.info("Initialized ReposPhpBridge with repository resolver {}", resolver);
	}
	
	public static CmsItem getItem(URL repositoryUrl, String target) {
		return services.getItem(repositoryUrl, target);
	}
	
	public static Map<String, Object> getInfo(URL repositoryUrl, String path) {
		return services.getInfo(repositoryUrl, path);
	}
	
	public static boolean isWritable(URL fileUrl) {
		return services.isWritable(fileUrl);
	}
	
	public static boolean isWritableFolder(URL fileUrl) {
		return services.isWritable(fileUrl);
	}	
	
}
