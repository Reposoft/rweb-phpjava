package se.repos.rweb.php;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.junit.After;
import org.junit.Test;

import se.simonsoft.cms.testing.svn.CmsTestRepository;
import se.simonsoft.cms.testing.svn.SvnTestSetup;

public class ReposPhpIntegrationTest {

	// can't mock context with current static setup so we need actual svnkit
	private SvnTestSetup setup = SvnTestSetup.getInstance();
	
	@After
	public void tearDown() {
		setup.tearDown();
	}
	
	@Test
	public void testGetInfo() throws MalformedURLException {
		InputStream dumpfile = this.getClass().getClassLoader().getResourceAsStream(
				"se/repos/rweb/php/svntest/testrepo1.svndump");
		assertNotNull(dumpfile);
		CmsTestRepository repository = setup.getRepository().load(dumpfile);
		//repository.keep();
		
		ReposPhpServices service = new ReposPhpServices(new SvntestRepositoryResolver(repository));
		
		// test file
		Map<String, Object> info = service.getInfo(new URL(repository.getUrl()), "/t1.txt");
		assertEquals(1L, info.get("revision"));
		
		// test folder
		
		// test non existent file
		Map<String, Object> infoNot = service.getInfo(new URL(repository.getUrl()), "/non/existent");
		assertEquals("info for non existent folder should contain path, as legacy convention", "/non/existent", infoNot.get("path"));
		assertEquals("should have no other fields", 1, infoNot.size());
	}

}
