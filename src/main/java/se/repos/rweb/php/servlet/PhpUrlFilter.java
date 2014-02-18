package se.repos.rweb.php.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhpUrlFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(PhpUrlFilter.class);
	
	private static final Pattern PATTERN_V1 = Pattern.compile("/([^/]+)/v1/([^/]+)/([^/]+)?(/.*)?");
	
	private static final Pattern PATTERN_ERR = Pattern.compile("/([^/]+)/errors/([0-9]+)/?");
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		String uri = request.getRequestURI();
		if (uri.indexOf('+') >= 0) {
			// if we get decoded uris here in some containers we can easily pass around parameters decoded, but we seem to get UTF-8 and # etc encoded here
			logger.debug("Encoding + to %2B as paths are expected to be urlencoded at this stage");
			uri = uri.replace("+", "%2B");
		}
		Matcher vm = PATTERN_V1.matcher(uri);
		if (vm.matches()) {
			logger.info("Rewriting URI {}", uri);
			rewriteV1(request, res, request.getParameter("rweb"), vm.group(1), vm.group(2), vm.group(3), vm.group(4));
		} else {
			Matcher em = PATTERN_ERR.matcher(uri);
			if (em.matches()) { // error pages should be handled as subrequest, or Quercus won't find them
				redirect(request, res, "/errors/" + em.group(2) + "/", Collections.<String, String> emptyMap());
			} else {
				logger.debug("Passing through URI {} (target={})", uri, request.getParameterValues("target"));
				chain.doFilter(req, res);
			}
		}
	}
	
	public void rewriteV1(HttpServletRequest request, ServletResponse res, String rweb, String webapp, String parent, String repoName, String path) throws ServletException, IOException {
		logger.info("Rewriting URI {} {} {} {} {}", new Object[]{ rweb, webapp, parent, repoName, path });
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("base", repoName);
		params.put("target", path);
		String to;
		if ("details".equals(rweb)) {
			to = "/open/";
		} else if ("history".equals(rweb)) {
			to = "/open/log/";
		} else if (rweb.startsWith("e.")) {
			to = "/edit/" + rweb.substring(2) + "/";
		} else {
			throw new RuntimeException("Service '" + rweb + "' not recognized"); // TODO status 422 and better looking error message
		}
		redirect(request, res, to, params);
	}

	protected void redirect(HttpServletRequest request, ServletResponse res,
			String to, Map<String, String> params) throws ServletException, IOException {
		// HttpServletRequest req = new RequestWrapParameters(request, params);
		//logger.info("Redirecting to {} {}", to, params);
		// Standard servlet rewrite strategy seems to fail
		//request.getRequestDispatcher(to).forward(req, res);
		StringBuffer tophp = new StringBuffer(to);
		if (tophp.charAt(tophp.length() - 1) == '/') {
			tophp.append("index.php"); // needed in filter redirect and apache subrequests like error pages, otherwise Quercus doesn't find the "welcome file" and tomcat returns 404 page
		}
		for (String p : params.keySet()) {
			tophp.append(tophp.indexOf("?") < 0 ? '?' : '&');
			tophp.append(p).append('=');
			//tophp.append(urlencode(params.get(p)));
			tophp.append(params.get(p)); // values picked from the original URI look encoded already
		}
		Enumeration<?> existing = request.getParameterNames();
		while (existing.hasMoreElements()) {
			String p = (String) existing.nextElement();
			if (!params.containsKey(p)) {
				tophp.append(tophp.indexOf("?") < 0 ? '?' : '&');
				for (String v : request.getParameterValues(p)) {
					tophp.append(p).append('=');
					tophp.append(urlencode(v));
				}
			}
		}
		logger.info("Redirecting (for {}) to {}", to, tophp);
		request.getRequestDispatcher(tophp.toString()).forward(request, res);
	}

	private String urlencode(String queryvalue) {
		try {
			return URLEncoder.encode(queryvalue, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Required encoding failed");
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

}
