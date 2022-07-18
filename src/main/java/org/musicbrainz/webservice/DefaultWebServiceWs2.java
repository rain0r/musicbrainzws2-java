package org.musicbrainz.webservice;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.musicbrainz.DomainsWs2;
import org.musicbrainz.query.submission.SubmissionException;
import org.musicbrainz.wsxml.MbXMLException;
import org.musicbrainz.wsxml.MbXmlParser;
import org.musicbrainz.wsxml.MbXmlWriter;
import org.musicbrainz.wsxml.element.Metadata;
import org.musicbrainz.wsxml.impl.JDOMParserWs2;
import org.musicbrainz.wsxml.impl.JDOMWriterWs2;

/**
 * A default abstract web service implementation that provides common properties of a web
 * service client that can be extended.
 */
public abstract class DefaultWebServiceWs2 extends DomainsWs2 implements WebService {

	protected static Logger log = Logger.getLogger(DefaultWebServiceWs2.class.getName());

	/**
	 * Encoding used to encode url parameters
	 */
	protected static final String URL_ENCODING = "UTF-8";

	/**
	 * Name of this library that will be used as part of the default user agent string or
	 * as part of a custom user agent string.
	 *
	 * @see DefaultWebServiceWs2#createUserAgent()
	 * @see DefaultWebServiceWs2#createUserAgent(String, String, String)
	 * @see <a href=
	 * "https://musicbrainz.org/doc/XML_Web_Service/Rate_Limiting#How_can_I_be_a_good_citizen_and_be_smart_about_using_the_Web_Service.3F">How
	 * can I be a good citizen and be smart about using the Web Service? </a>
	 */
	protected static final String USER_AGENT_LIB_NAME = "MusicBrainz-Java";

	// TODO Read the constant value from properties file that gets written during maven
	// build
	/**
	 * Version of this library that will be used as part of the default user agent string
	 * or as part of a custom user agent string.
	 *
	 * @see DefaultWebServiceWs2#createUserAgent()
	 * @see DefaultWebServiceWs2#createUserAgent(String, String, String)
	 * @see <a href=
	 * "https://musicbrainz.org/doc/XML_Web_Service/Rate_Limiting#How_can_I_be_a_good_citizen_and_be_smart_about_using_the_Web_Service.3F">How
	 * can I be a good citizen and be smart about using the Web Service? </a>
	 */
	protected static final String USER_AGENT_LIB_VERSION = "2.01beta-nusicEdition";

	/**
	 * Contact information of this library that will be used as part of the default user
	 * agent string.
	 *
	 * @see DefaultWebServiceWs2#createUserAgent()
	 * @see DefaultWebServiceWs2#createUserAgent(String, String, String)
	 * @see <a href=
	 * "https://musicbrainz.org/doc/XML_Web_Service/Rate_Limiting#How_can_I_be_a_good_citizen_and_be_smart_about_using_the_Web_Service.3F">How
	 * can I be a good citizen and be smart about using the Web Service? </a>
	 */
	protected static final String USER_AGENT_LIB_CONTACT = "http://code.google.com/p/musicbrainzws2-java/";

	/**
	 * The authentication scheme, could only be DIGEST.
	 */
	protected static final String SCHEME = "digest";

	/**
	 * A string that is used in the web service url
	 * @see DefaultWebServiceWs2#makeURL(String, String, List, Map)
	 */
	protected static final String PATHPREFIX = "/ws";

	/**
	 * The host, defaults to MAINHOST
	 */
	private String host = MAINHOST;

	/**
	 * The realm for authentication, defaults to AUTHREALM
	 */
	private String realm = AUTHREALM;

	/**
	 * The protocol, defaults to http
	 */
	private String protocol = "https";

	/**
	 * The port, defaults to null
	 */
	private Integer port = null;

	/**
	 * XML parser used to consume the response stream
	 */
	private MbXmlParser parser;

	/**
	 * XML writer used to produce the body of POST requests.
	 */
	private MbXmlWriter writer;

	/*
	 * All POST requests require authentication. You should authenticate using HTTP
	 * Digest, use the same username and password you use to access the main
	 * http://musicbrainz.org website. The realm is "musicbrainz.org".
	 *
	 * POST requests should always include a 'client' parameter in the URL (not the body).
	 * The value of 'client' should be the ID of the client software submitting data. This
	 * has to be the application's name and version number, not that of a client library
	 * (client libraries should use HTTP's User-Agent header). The recommended format is
	 * "application-version", where version does not contain a - character.
	 */
	/**
	 * The client credential of your application example.app-0.4.7
	 */
	private String client = "";

	/**
	 * The username for your Musicbrainz account
	 */
	private String username;

	/**
	 * The password for your Musicbrainz account
	 */
	private String password;

	/**
	 * Default Constructor that uses {@link JDOMParserWs2}
	 */
	public DefaultWebServiceWs2() {
		this.parser = new JDOMParserWs2();
		this.writer = new JDOMWriterWs2();
	}

	/**
	 * Constructor to inject a custom parser and writer
	 * @param parser XML parser used to consume the response stream
	 * @param writer XML used to produce the body of POST requests
	 */
	public DefaultWebServiceWs2(MbXmlParser parser, MbXmlWriter writer) {
		this.parser = parser;
		this.writer = writer;
	}

	/**
	 * Sends a GET request to the specified url
	 * @param url The URL
	 * @return A populated {@link Metadata} object
	 * @throws WebServiceException
	 */
	protected abstract Metadata doGet(String url) throws WebServiceException, MbXMLException;

	/**
	 * Sends a POST request to the specified url.
	 * @param url
	 * @param md
	 * @return {@link Metadata}
	 * @throws WebServiceException
	 * @throws MbXMLException
	 */
	protected abstract Metadata doPost(String url, Metadata md) throws WebServiceException, MbXMLException;

	/**
	 * Sends a PUT request to the specified url.
	 * @param url
	 * @return {@link Metadata}
	 * @throws WebServiceException
	 * @throws MbXMLException
	 */
	protected abstract Metadata doPut(String url) throws WebServiceException, MbXMLException;

	/**
	 * Sends a DELETE request to the specified url.
	 * @param url
	 * @return {@link Metadata}
	 * @throws WebServiceException
	 * @throws MbXMLException
	 */
	protected abstract Metadata doDelete(String url) throws WebServiceException, MbXMLException;

	public Metadata get(String entity, String id, List<String> includeParams, Map<String, String> filterParams)
			throws WebServiceException, MbXMLException {
		String url = this.makeURL(entity, id, includeParams, filterParams);

		// log.debug("GET " + url);

		return doGet(url);
	}

	public Metadata post(Metadata metadata) throws WebServiceException, MbXMLException {
		if (metadata == null || metadata.getSubmissionWs2() == null)
			throw new SubmissionException("Empty Submission not allowed");

		String url = this.makeURLforPost(metadata.getSubmissionWs2().getSubmissionType(), getClient());

		// log.debug("POST " + url);

		return doPost(url, metadata);
	}

	public Metadata put(String entity, String id, List<String> data) throws WebServiceException, MbXMLException {

		return doPut(buildRequest(entity, id, data));
	};

	public Metadata delete(String entity, String id, List<String> data) throws WebServiceException, MbXMLException {

		return doDelete(buildRequest(entity, id, data));
	}

	/**
	 * Constructs a URL that can be used to query the web service. The url is made up of
	 * the protocol, host, port, version, type, path and parameters.
	 * @param submissionType
	 * @param client
	 * @return An URL as String
	 */
	protected String makeURLforPost(String submissionType, String client) {
		StringBuffer url = new StringBuffer();

		// append protocol, host and port
		url.append(this.protocol).append("://").append(this.getHost());
		if (this.port != null)
			url.append(":").append(this.port);

		// append path
		url.append(PATHPREFIX).append("/").append(WS_VERSION).append("/").append(submissionType).append("?client=")
				.append(client);

		return url.toString();
	}

	/**
	 * @param entity
	 * @param id
	 * @param data
	 * @return String
	 */
	private String buildRequest(String entity, String id, List<String> data) {

		String url = this.makeURL(entity, id, null, null);
		StringBuilder buf = new StringBuilder();

		buf.append(url);
		buf.append("/");
		boolean begin = true;
		for (String s : data) {
			if (!begin)
				buf.append(";");
			begin = false;
			buf.append(s);
		}
		buf.append("?client=").append(getClient());
		return buf.toString();
	}

	/**
	 * Constructs a URL that can be used to query the web service. The url is made up of
	 * the protocol, host, port, version, type, path and parameters.
	 * @param entity The entity (i.e. type, e.g. 'artist') the request is targeting
	 * @param id The id of the entity
	 * @param includeParams A list containing values for the 'inc' parameter (can be null)
	 * @param filterParams Additional parameters depending on the entity (can be null)
	 * @return An URL as String
	 */
	protected String makeURL(String entity, String id, List<String> includeParams, Map<String, String> filterParams) {
		StringBuilder url = new StringBuilder();
		Map<String, String> urlParams = new HashMap<String, String>();

		// Type is not requested/allowed anymore in ws2.
		// urlParams.put("type", this.type);

		// append filter params

		if (filterParams != null)
			urlParams.putAll(filterParams);

		// append protocol, host and port
		url.append(this.protocol).append("://").append(this.getHost());
		if (this.port != null)
			url.append(":").append(this.port);

		// append path
		url.append(PATHPREFIX).append("/").append(WS_VERSION).append("/").append(entity).append("/").append(id);

		// Handle COLLECTION sintax exception.

		if (entity.equals(COLLECTION) && !id.isEmpty()) {
			url.append("/" + RELEASES_INC);
		}

		// build space separated include param
		if (includeParams != null) {
			urlParams.put("inc", StringUtils.join(includeParams, "+"));
		}

		// append params
		url.append("?");
		Iterator<Entry<String, String>> it = urlParams.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> e = it.next();
			try {
				url.append(e.getKey()).append("=").append(URLEncoder.encode(e.getValue(), URL_ENCODING)).append("&");
			}
			catch (UnsupportedEncodingException ex) {
				log.severe("Internal Error: Could not encode url parameter " + e.getKey());
			}
		}

		return url.substring(0, url.length() - 1);
	}

	protected String createUserAgent() {
		return USER_AGENT_LIB_NAME + "/" + USER_AGENT_LIB_VERSION + " ( " + USER_AGENT_LIB_CONTACT + " )";
	}

	/**
	 * @return the parser
	 *
	 * <code>&lt;name&gt;</code>/<code>&lt;version&gt;</code>
	 * {@link #USER_AGENT_LIB_NAME}/{@link #USER_AGENT_LIB_VERSION} (
	 * <code>&lt;contact&gt;</code> )
	 * @param name custom application name
	 * @param version custom application version
	 * @param contact application contact URL or author email
	 * @return {@link String}
	 */
	protected String createUserAgent(String name, String version, String contact) {
		return name + "/" + version + " " + USER_AGENT_LIB_NAME + "/" + USER_AGENT_LIB_VERSION + " ( " + contact + " )";
	}

	/**
	 * @return the parser
	 */
	public MbXmlParser getParser() {
		return parser;
	}

	/**
	 * @param parser the parser to set
	 */
	public void setParser(MbXmlParser parser) {
		this.parser = parser;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the realm
	 */
	public String getRealm() {
		return realm;
	}

	/**
	 * @param realm the realm to set
	 */
	public void setRealm(String realm) {
		this.realm = realm;
	}

	/**
	 * @return the client
	 */
	public String getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(String client) {
		this.client = client;
	}

	/**
	 * @return the writer
	 */
	public MbXmlWriter getWriter() {
		return writer;
	}

	/**
	 * @param writer the writer to set
	 */
	public void setWriter(MbXmlWriter writer) {
		this.writer = writer;
	}

}
