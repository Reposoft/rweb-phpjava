package se.repos.rweb.php.servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wraps a request with additional and overridden parameters.
 * Does not support multi-value, i.e. repeated, parameters.
 */
public class RequestWrapParameters extends HttpServletRequestWrapper {

    Map<String, String> overridenParameters;

    public RequestWrapParameters(HttpServletRequest httpServletRequest,
                                    Map<String, String> overridenParameters) {
        super(httpServletRequest);
        this.overridenParameters = overridenParameters;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public Enumeration getParameterNames() {
        if (overridenParameters != null) {
            List keys = Collections.list(super.getParameterNames());
            keys.addAll(overridenParameters.keySet());
            return Collections.enumeration(keys);
        }
        return super.getParameterNames();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public Map getParameterMap() {
        if (overridenParameters != null) {
            Map superMap = super.getParameterMap();
            //superMap is an unmodifiable map, hence creating a new one.
            Map overriddenMap = new HashMap(superMap.size() + overridenParameters.size());
            overriddenMap.putAll(superMap);
            overriddenMap.putAll(overridenParameters);
            return overriddenMap;
        }
        return super.getParameterMap();
    }

    @Override
    public String[] getParameterValues(String s) {
        if (overridenParameters.containsKey(s)) {
            return new String[] { overridenParameters.get(s) };
        }
        return super.getParameterValues(s);
    }

    @Override
    public String getParameter(String s) {
        if (overridenParameters.containsKey(s)) {
            return overridenParameters.get(s);
        }
        return super.getParameter(s);
    }

}
