package se.repos.rweb.php;

import java.net.URL;
import java.nio.charset.Charset;
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
 * 
 * For arguments that may contain UTF-8 characters: we have a situation where mb_detect_encoding
 * in Repos says that the string is UTF-8, but it arrives in java with double chars.
 * http://www.caucho.com/resin-3.1/doc/quercus.xtp#MarshallingPHPtoJavaconversions hints of non-unicode
 * but nothing works with unicode.semantics on.
 * The theory is that Repos' runs UTF-8 internally which is unexpected for PHP5. We don't cange this,
 * so our java bridge should take byte[] instead of String and convert explicitly.
 * 
 */
public abstract class ReposPhpBridge {

	private static final Logger logger = LoggerFactory.getLogger(ReposPhpBridge.class);
	
	private static ReposPhpServices services = null;
	
	private static final Charset ARG_CHARSET = Charset.forName("UTF-8");
	
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
	
	public static Map<String, Object> getInfo(URL repositoryUrl, byte[] path) {
		return services.getInfo(repositoryUrl, new String(path, ARG_CHARSET));
	}
	
	public static boolean isWritable(URL fileUrl) {
		return services.isWritable(fileUrl);
	}
	
	public static boolean isWritableFolder(URL fileUrl) {
		return services.isWritable(fileUrl);
	}	
	
}
