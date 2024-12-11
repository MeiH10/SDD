package Pucknotes.Server.Session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * The SessionConfig class is responsible for configuring the session management
 * settings for the application. It uses Spring's configuration capabilities to
 * define beans that will be used throughout the application.
 */
@Configuration
public class SessionConfig {

  /**
   * This method creates a bean of type CookieSerializer.
   * It initializes the DefaultCookieSerializer with specific settings
   * for session cookie management. The cookie name is set to "SESSIONID",
   * the cookies are marked as HTTP-only for added security, and the SameSite
   * attribute is set to "Strict" to prevent cross-site request forgery.
   *
   * @return A CookieSerializer instance configured with custom settings.
   */
  @Bean
  public CookieSerializer cookieSerializer() {
    DefaultCookieSerializer serializer = new DefaultCookieSerializer();

    // Set the name of the cookie to be used for session management.
    serializer.setCookieName("SESSIONID");

    // Mark the cookie as HTTP-only to prevent client-side scripts from accessing it.
    serializer.setUseHttpOnlyCookie(true);

    // Set the SameSite attribute to "Strict" to mitigate CSRF attacks.
    serializer.setSameSite("Strict");

    // The serializer is configured; now it will be available for dependency injection.
    return serializer;
  }
}
