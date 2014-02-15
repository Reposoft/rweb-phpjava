package se.repos.rweb.php.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(UrlFilter.class);
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		// TODO Auto-generated method stub
		String uri = request.getRequestURI();
		if (false) {
			logger.info("Rewriting URI {}", uri);
			request.getRequestDispatcher(uri).forward(req, res);
		} else {
			logger.info("Passing through URI {}", uri);
			chain.doFilter(req, res);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

}
