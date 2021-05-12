/*<ORACLECOPYRIGHT>
 * Copyright (C) 1994, 2018, Oracle and/or its affiliates. All rights reserved.
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.
 * UNIX is a registered trademark of The Open Group.
 *
 * This software and related documentation are provided under a license agreement 
 * containing restrictions on use and disclosure and are protected by intellectual property laws. 
 * Except as expressly permitted in your license agreement or allowed by law, you may not use, copy, 
 * reproduce, translate, broadcast, modify, license, transmit, distribute, exhibit, perform, publish, 
 * or display any part, in any form, or by any means. Reverse engineering, disassembly, 
 * or decompilation of this software, unless required by law for interoperability, is prohibited.
 *
 * The information contained herein is subject to change without notice and is not warranted to be error-free. 
 * If you find any errors, please report them to us in writing.
 *
 * U.S. GOVERNMENT RIGHTS Programs, software, databases, and related documentation and technical data delivered to U.S. 
 * Government customers are "commercial computer software" or "commercial technical data" pursuant to the applicable 
 * Federal Acquisition Regulation and agency-specific supplemental regulations. 
 * As such, the use, duplication, disclosure, modification, and adaptation shall be subject to the restrictions and 
 * license terms set forth in the applicable Government contract, and, to the extent applicable by the terms of the 
 * Government contract, the additional rights set forth in FAR 52.227-19, Commercial Computer Software License 
 * (December 2007). Oracle America, Inc., 500 Oracle Parkway, Redwood City, CA 94065.
 *
 * This software or hardware is developed for general use in a variety of information management applications. 
 * It is not developed or intended for use in any inherently dangerous applications, including applications that 
 * may create a risk of personal injury. If you use this software or hardware in dangerous applications, 
 * then you shall be responsible to take all appropriate fail-safe, backup, redundancy, 
 * and other measures to ensure its safe use. Oracle Corporation and its affiliates disclaim any liability for any 
 * damages caused by use of this software or hardware in dangerous applications.
 *
 * This software or hardware and documentation may provide access to or information on content, 
 * products, and services from third parties. Oracle Corporation and its affiliates are not responsible for and 
 * expressly disclaim all warranties of any kind with respect to third-party content, products, and services. 
 * Oracle Corporation and its affiliates will not be responsible for any loss, costs, 
 * or damages incurred due to your access to or use of third-party content, products, or services.
 </ORACLECOPYRIGHT>*/

package atg.projects.store.mobile.push.apple;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.transaction.TransactionManager;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.core.util.StringUtils;
import atg.json.JSONException;
import atg.json.JSONObject;
import atg.nucleus.GenericService;
import atg.projects.store.mobile.MobileStoreConfiguration;
import atg.projects.store.mobile.device.DeviceManager;
import atg.projects.store.mobile.push.PushNotificationException;
import atg.projects.store.mobile.push.PushNotificationService;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryImpl;
import atg.repository.RepositoryItem;
import atg.security.opss.csf.CredentialMapProperties;
import atg.security.opss.csf.CredentialStoreManager;
import atg.security.opss.csf.GenericCredentialProperties;

/**
 * Nucleus object that handles pushing notifications to apple devices
 *
 * @author gdoneil
 * @version $Change: 1536476 $$DateTime: 2018/04/13 08:11:14 $$Author: releng $
 * @updated $DateTime: 2018/04/13 08:11:14 $$Author: releng $
 */

public class ApplePushNotificationService extends GenericService implements 
    PushNotificationService
{
  public static String CLASS_VERSION =
      "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/src/atg/projects/store/mobile/push/apple/ApplePushNotificationService.java#3 $$Change: 1536476 $";

  // ---------------------------------------------------------------------------
  // Constants 
  // ---------------------------------------------------------------------------  
  
  // Used for creating notification JSONs
  private static final String APS_KEY   = "aps";
  private static final String ALERT_KEY = "alert";
  private static final String LOC_KEY   = "loc-key";
  private static final String BODY_KEY  = "body";
  private static final String LINK_KEY  = "link";
  private static final String CONTENT_AVAILABLE_KEY = "content-available";

  // Used for retrieving certificate password from the credential store
  private static final String CREDENTIAL_MAP_NAME = "PushNotificationServiceCredentialMap";
  private static final String CERTIFICATE_PASSWORD_KEY_NAME = "appleCertificatePassword";

  // ---------------------------------------------------------------------------
  // Properties
  // ---------------------------------------------------------------------------  

  // ---------------------------------------------------------------------------
  // property:certificatePath
  private String mCertificatePath;

  /**
   * Gets the path to the private key (.p12) file used when communicating with the 
   * Apple Push Server
   * @return The path to the private key file
   */
  public String getCertificatePath() {
    return mCertificatePath;
  }

  /**
   * Sets the path to the private key (.p12) file used when communicating with the 
   * Apple Push Server
   * @param pCertificatePath The path to the p12 file
   */
  public void setCertificatePath(String pCertificatePath) {
    mCertificatePath = pCertificatePath;
  }

  // ---------------------------------------------------------------------------
  // property: appleHost and applePort
  private String mAppleHost;
  private int mApplePort;

  /**
   * Gets the Apple Push Server hostname e.g. "gateway.push.apple.com" or 
   * "gateway.sandbox.push.apple.com"
   * @return The hostname of the Apple Push Server
   */
  public String getAppleHost() {
    return mAppleHost;
  }

  /**
   * Sets the Apple Push Server hostname
   * @param pAppleHost The hostname of the Apple Push Server
   */
  public void setAppleHost(String pAppleHost) {
    mAppleHost = pAppleHost;
  }

  /**
   * Gets the port used when connecting to appleHost
   * @return The port used when connecting to the appleHost
   */
  public int getApplePort() {
    return mApplePort;
  }

  /**
   * Sets the port used when connecting to appleHost
   * @param pApplePort The port used when connecting to the appleHost
   */
  public void setApplePort(int pApplePort) {
    mApplePort = pApplePort;
  }
  
  // ---------------------------------------------------------------------------
  // property: appleFeedbackHost, appleFeedbackPort
  private String mAppleFeedbackHost;
  private int mAppleFeedbackPort;
  
  /**
   * Gets the host used for getting feedback about which devices fail to receive
   * push notifications
   * @return the host used for getting feedback about which devices fail to
   * receive push notifications
   */
  public String getAppleFeedbackHost() {
    return mAppleFeedbackHost;
  }

  /**
   * Sets the host used for getting feedback about which devices fail to receive
   * push notifications
   * @param mAppleFeedbackHost the mAppleFeedbackHost to set
   */
  public void setAppleFeedbackHost(String pAppleFeedbackHost) {
    mAppleFeedbackHost = pAppleFeedbackHost;
  }

  /**
   * @return the port used for getting feedback about which devices fail to receive
   * push notifications
   */
  public int getAppleFeedbackPort() {
    return mAppleFeedbackPort;
  }

  /**
   * Sets the port used for getting feedback about which devices fail to receive push
   * notifications
   * @param mAppleFeedbackPort the port used for getting feedback about which devices
   * fail to receive push notifications
   */
  public void setAppleFeedbackPort(int pAppleFeedbackPort) {
    mAppleFeedbackPort = pAppleFeedbackPort;
  }

  // ---------------------------------------------------------------------------
  // property: proxyHost, proxyPort and proxyAuthentication 
  private String mProxyHost;
  private int mProxyPort;
  private String mProxyAuthentication;

  /**
   * Gets the host name of the proxy server
   * @return The host name of the proxy server
   */
  public String getProxyHost() {
    return mProxyHost;
  }

  /**
   * Sets the host name of the proxy server
   * @param pProxyHost The new hostname for the proxy server
   */
  public void setProxyHost(String pProxyHost) {
    mProxyHost = pProxyHost;
  }

  /**
   * Gets the port used when connecting to the proxy server
   * @return The port used when connecting to the proxy server
   */
  public int getProxyPort() {
    return mProxyPort;
  }

  /**
   * Sets the proxy authentication used when connecting to the proxy server (can
   * be generated using the command: "echo <username>:<password> | openssl -base64")
   */
  public void setProxyPort(int pProxyPort) {
    mProxyPort = pProxyPort;
  }

  /**
   * Gets the proxy authentication used when connecting to the proxy server (can
   * be generated using the command: "echo <username>:<password> | openssl -base64")
   * @return The proxy authentication used when connecting to the proxy server
   */ 
  public String getProxyAuthentication() {
    return mProxyAuthentication;
  }

  /**
   * Sets the proxy authentication used when connecting to the proxy server (can
   * be generated using the command: "echo <username>:<password> | openssl -base64")
   */
  public void setProxyAuthentication(String pProxyAuthentication) {
    mProxyAuthentication = pProxyAuthentication;
  }

  // ---------------------------------------------------------------------------
  // property: deviceManager
  private DeviceManager mDeviceManager;
  
  /**
   * Sets device manager property
   * @param pDeviceManager
   */
  public void setDeviceManager(DeviceManager pDeviceManager) {
    mDeviceManager = pDeviceManager;
  }

  /**
   * Gets the device manager
   * @return the device manager
   */
  public DeviceManager getDeviceManager() {
    return mDeviceManager;
  }
  
  // ---------------------------------------------------------------------------
  // property: deviceManager
  private CredentialStoreManager mCredentialStoreManager;
  
  /**
   * @return the mCredentialStoreManager
   */
  public CredentialStoreManager getCredentialStoreManager() {
    return mCredentialStoreManager;
  }

  /**
   * @param mCredentialStoreManager the mCredentialStoreManager to set
   */
  public void setCredentialStoreManager(CredentialStoreManager pCredentialStoreManager) {
    mCredentialStoreManager = pCredentialStoreManager;
  }

  // ---------------------------------------------------------------------------
  // Member variables
  // ---------------------------------------------------------------------------

  private SSLSocketFactory mSocketFactory;

  /**
   * Lazily instantiate mSocketFactory
   * @return 
   * @throws PushNotificationException
   */
  private SSLSocketFactory getSocketFactory() throws PushNotificationException {
    if (mSocketFactory == null) {
      
      // Check for blank certificate path or null password
      String errorMessage = "";
      if (StringUtils.isBlank(getCertificatePath())) {
        vlogError(MobileStoreConfiguration.sResourceBundle, "apnsNullCertificatePath");
        errorMessage += MobileStoreConfiguration.sResourceBundle.getString("apnsNullCertificatePath");
      }
      if (getCertificatePassword() == null) {
        vlogWarning(MobileStoreConfiguration.sResourceBundle, "apnsCertificatePasswordNotSet", CERTIFICATE_PASSWORD_KEY_NAME,
            CREDENTIAL_MAP_NAME);
      }
      
      if (!StringUtils.isBlank(errorMessage)) {
        throw new PushNotificationException(errorMessage);
      }
      
      FileInputStream fileInputStream = null;
      try {
        // Create SSLSocketFactory using certificate and password
        KeyStore keystore = KeyStore.getInstance("PKCS12"); // Specify p12 encryption
        File certificate = new File(getCertificatePath());
        // Check if certificate exists
        if (!certificate.exists()) {
          vlogError(MobileStoreConfiguration.sResourceBundle,
              "apnsCannotFindCertificateAtPath", getCertificatePath());
        }
        fileInputStream = new FileInputStream(getCertificatePath());
        keystore.load(fileInputStream, 
                      getCertificatePassword().toCharArray()); 
        KeyManagerFactory keystoreFactory = KeyManagerFactory
                                            .getInstance("SunX509");
        keystoreFactory.init(keystore, getCertificatePassword().toCharArray());
  
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keystoreFactory.getKeyManagers(), null, null);
        mSocketFactory = sslContext.getSocketFactory();
      } catch (IOException | KeyStoreException | NoSuchAlgorithmException 
               | CertificateException | UnrecoverableKeyException 
               | KeyManagementException e) 
      {
        throw new PushNotificationException(MobileStoreConfiguration.sResourceBundle.getString("apnsSocketFactoryCreationError"), e);
      } finally {
        try {
          if (fileInputStream != null) {
            fileInputStream.close();
          }
        } catch (IOException e) {
          // Do nothing if stream fails to close
        }
      }
      
    }
    return mSocketFactory;
  }
  
  // ---------------------------------------------------------------------------
  // Public methods
  // ---------------------------------------------------------------------------

  /* (non-Javadoc)
   * @see atg.projects.store.mobile.push.PushNotificationService#sendNotification(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Map)
   */
  public void sendNotification(String pDeviceUniqueId, String pBody, String pLocKey,
                               String pLink, Map<String, Object>pExtra) 
                                   throws PushNotificationException
  {

    // Get device token for provided uniqaue id
    String deviceToken = getDeviceManager().getPushTokenForUniqueId(pDeviceUniqueId);
    if (deviceToken == null) {
      throw new PushNotificationException(
          MobileStoreConfiguration.sResourceBundle.getString("nullPushTokenError")
          + " " + pDeviceUniqueId);
    }
    SSLSocket socket = null;

    String notificationString = createNotification(pBody, pLocKey, pLink, pExtra);

    // Initiate connection to apple push notification server
    socket = createSocket(getAppleHost(), getApplePort());
    try {
      // If no proxy, do handshake.  Otherwise handshake has already been done 
      // in order to create the proxy socket
      if (getProxyHost() == null) {
        socket.startHandshake();
      }
  
      // Encode token
      byte[] tokenBytes = Hex.decodeHex(deviceToken.toCharArray());
  
      // Write to socket
      OutputStream outputstream = socket.getOutputStream();
      
      // https://developer.apple.com/library/ios/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/Chapters/CommunicatingWIthAPS.html
      // for format of outputstream bytes
      int frameSize = 1 + 2 + tokenBytes.length + 1 + 2 + notificationString.length();
      outputstream.write(2); // Write command to socket
      // Write the length of the data we'll be sending to the socket
      outputstream.write(intToBytes(frameSize));
  
      // Send the device token
      outputstream.write(1); // the number 1 encodes signifies that we'll be sending the token next
      // Then send the length of the token (must be just 2 bytes)
      outputstream.write(Arrays.copyOfRange(intToBytes(tokenBytes.length), 2, 4)); 
      outputstream.write(tokenBytes); // send the token data
      
      // Next, send the notification payload
      // the number 2 signifies that the notification payload will be sent next
      outputstream.write(2); 
      // Then, send the length of the notification payload (just 2 bytes)
      outputstream.write(Arrays.copyOfRange(intToBytes(notificationString.length()),
                                            2, 4)); 
      outputstream.write(notificationString.getBytes()); // send the payload data
      
      outputstream.flush();
    } catch(IOException | DecoderException e) {
      throw new PushNotificationException (
          MobileStoreConfiguration.sResourceBundle.getString("genericPushError"), 
          e);
    } finally {
    // Closing a socket also closes its input and output streams
      try {
        socket.close();
      } catch (IOException e) {
        vlogError(MobileStoreConfiguration.sResourceBundle, "socketCloseError");
      }
    }
  }
  
  public void retireInvalidDevices() throws PushNotificationException {
    Map<String, Timestamp> pushTokens = getDeviceTokensFromFeedbackService(); 
    if (pushTokens == null || pushTokens.isEmpty()) {
      // There are no invalid devices
      return;
    }
    
    TransactionManager transactionManager = getTransactionManager();
    TransactionDemarcation td = new TransactionDemarcation ();
    boolean shouldRollback = false;
    try {
      td.begin(transactionManager, TransactionDemarcation.REQUIRED);
 
      try {
        for (String pushToken : pushTokens.keySet()) {
          RepositoryItem device = getDeviceManager().getDeviceByPushToken(pushToken);
          
          // The shopper might have re-installed the app since it was registered
          // in the feedback service. We need to check that the device was not
          // seen again since it was entered in the feedback service. 
          Timestamp deviceLastSeen = (Timestamp) device.getPropertyValue(getDeviceManager()
              .getPropertyManager().getSeenOnPropertyName());
          Timestamp deviceRetiredOn = pushTokens.get(pushToken);
          if (deviceLastSeen.before(deviceRetiredOn)) {
            String deviceId = (String) device.getPropertyValue(getDeviceManager()
                .getPropertyManager().getUniqueIdPropertyName());
            getDeviceManager().deleteDevice(deviceId);
          }
        }
      } catch (RepositoryException ex) {
        throw new PushNotificationException(ex);
      }
    } catch (Exception ex) {
      shouldRollback = true;
      throw new PushNotificationException(ex);
    } finally {
      try {
        td.end(shouldRollback);
      } catch (TransactionDemarcationException ex) {
        throw new PushNotificationException(ex);
      }
    }
  }
  
  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------
  
  /**
   * @param pCredentialMapName the name of the credentialMapProperties to return
   * @return the <code>CredentialMapProperties</code> with name pCredentialMapName
   */
  private CredentialMapProperties getCredentialMapProperties(String pCredentialMapName) {
    // Get the proxy authentication from the credential store
    CredentialMapProperties credentialMap = null;
    try {
      credentialMap = getCredentialStoreManager()
                      .retrieveCredentialMapProperties(pCredentialMapName);
    } catch (Exception e) {
      vlogError(MobileStoreConfiguration.sResourceBundle, "apnsErrorRetreivingCredentialMap",
          pCredentialMapName);
      vlogError(e, "");
      return null;
    }
    return credentialMap;
  }
  
  /**
   * Gets the password for the private key (.p12) file used when communicating
   * with the Apple Push Server.
   * @return the password for the private key (.p12) file used when communicating
   * with the Apple Push Server.  If no password set, returns empty string 
   */
  private String getCertificatePassword() {
    // Get the certificate password from the credential store
    CredentialMapProperties credentialMap = getCredentialMapProperties(CREDENTIAL_MAP_NAME);
    if (credentialMap != null
        && credentialMap.containsKey(CERTIFICATE_PASSWORD_KEY_NAME))
    {
      return (String)((GenericCredentialProperties)credentialMap
             .get(CERTIFICATE_PASSWORD_KEY_NAME)).getSingleCredential();
    }

    vlogWarning(MobileStoreConfiguration.sResourceBundle, "apnsCertificatePasswordNotSet", 
        CERTIFICATE_PASSWORD_KEY_NAME, CREDENTIAL_MAP_NAME);
    return "";
  }
  
  /**
   * Creates an returns an <code>SSLSocket</code> used for sending push notification
   * data to the Apple Push Notification Server
   * @param pHost the host that the socket will connect to
   * @param pPort the port used to connect to the provided host
   * @return <code>SSLSocket</code> used for sending push notification data to
   * the Apple Push Notification Server
   * @throws PushNotificationException
   */
  private SSLSocket createSocket(String pHost, int pPort)
      throws PushNotificationException
  {
    if (pHost == null) {
      throw new IllegalArgumentException(MobileStoreConfiguration.sResourceBundle.getString("apnsNullHost"));
    }
    if (getProxyHost() != null) {
      return createProxySocket(pHost, pPort);
    }
    try {
      return (SSLSocket) getSocketFactory().createSocket(pHost, pPort);
    } catch (IOException e) {
      throw new PushNotificationException(
          MobileStoreConfiguration.sResourceBundle.getString("apnsErrorCreatingSocket")
          + pHost + ":" + pPort, e);
    } 
  }
  
  /**
   * Creates an <code>SSLSocket</code> connected to the pHost:pPort through a
   * <code>Socket</code> connected to the proxy host/port
   * @param pHost the host to connect the SSLSocket to
   * @param pPort the port to connect the SSLSocket to
   * @return <code>SSLSocket</code> connected to the pHost:pPort through a
   * <code>Socket</code> connected to the proxy host/port
   * @throws PushNotificationException
   */
  private SSLSocket createProxySocket(String pHost, int pPort)
      throws PushNotificationException
  {
    try {
      Socket tunnel = new Socket(getProxyHost(), getProxyPort());
      startProxyHandshake(tunnel, pHost, pPort);
      try {
        SSLSocket socket = (SSLSocket) getSocketFactory().createSocket(tunnel, pHost, pPort, true);
        return socket;  
      } catch (IOException e) {
        // Close first socket then allow the outer try block to catch the IOException
        tunnel.close();
        throw new IOException(e);
      }
    } catch (IOException e) {
      throw new PushNotificationException(
          MobileStoreConfiguration.sResourceBundle.getString("apnsErrorCreatingSocket")
          + pHost + ":" + pPort + " " 
          + MobileStoreConfiguration.sResourceBundle.getString("apnsTunnelingThrough")
          + " " + getProxyHost() + ":" + getProxyPort(), e);
    }
  }
  
  /**
   * Sends connect request through the tunnel <code>Socket</code> to connect to
   * the Apple Push Server host/port. Equivalent of <code>socket.startHandshake()</code>
   * through a tunnel
   * @param pTunnel Tunnel used to connect to the proxy
   * @param pHost Apple host
   * @param pPort Apple port
   * @throws PushNotificationException
   */
  private void startProxyHandshake(Socket pTunnel, String pHost, int pPort)
      throws PushNotificationException
  {
    // Create HTTP header with connect request

    // Header example: 
    // CONNECT gateway.push.apple.com:2195 HTTP/1.0
    // Proxy-Authorization: Z2RvbmQpbDppaWxTVN5HMzAwMAo=
    StringBuilder header = new StringBuilder();
    header.append("CONNECT " + pHost + ":" + pPort + " HTTP/1.0");

    // If proxy authentication provided, include in the header
    if (StringUtils.isBlank(getProxyAuthentication()))  {
      header.append("\nProxy-Authorization: " + getProxyAuthentication());
    }

    // End header
    final String newLine = "\r\n";
    header.append(newLine + newLine);

    String msg = header.toString();

    try {
    // Write the connect request to the tunnel's output stream
      OutputStream out = pTunnel.getOutputStream();
      out.write(msg.getBytes());
      out.flush();
  
      // Read first line of response
      InputStreamReader inReader = new InputStreamReader(pTunnel.getInputStream());
      BufferedReader responseReader = new BufferedReader(inReader);
      String status = responseReader.readLine();

      // Log error on unsuccessful connect
      if (!status.contains("200")) {
        vlogError(MobileStoreConfiguration.sResourceBundle, "apnsUnsuccessfulTunnelResponse",
            pHost, pPort, getProxyHost(), getProxyPort());
        throw new PushNotificationException(
            MobileStoreConfiguration.sResourceBundle.getString("apnsConnectStatusError")
            + status);
      }
    } catch (IOException e) {
      throw new PushNotificationException(
          MobileStoreConfiguration.sResourceBundle.getString("apnsErrorStartingProxyHandshake"),
          e);
    }
  }  
  

  /**
   * Returns json string encoding the message, link and extra map
   * Ex: {"aps":{"alert":pMessage},"link":"pLink", pExtra.key1:pExtra.get(pExtra.key1),
   * pExtra.key2:pExtra.get(pExtra.key2), ...}
   * @param pBody the message that the notification should contain
   * @param pLocKey the localized key the notification should contain if no body
   * @param pLink the notification link
   * @param pExtra any extra data that should be sent in the notification
   * @return json string encoding the message, link and extra map
   * @throws PushNotificationException
   */
  private String createNotification(String pBodyValue, String pLocKeyValue,
                                    String pLink,      Map<String, Object> pExtra) 
      throws PushNotificationException
  {
    JSONObject notification = new JSONObject();
    JSONObject aps = new JSONObject();
    JSONObject body = new JSONObject();
    JSONObject locKey = new JSONObject();
    
    // Add push message to push notification
    try {
      if (!StringUtils.isBlank(pBodyValue)) {
        body.put(BODY_KEY, pBodyValue);
        aps.put(ALERT_KEY, body);
      }
      if (!StringUtils.isBlank(pLocKeyValue)) {
        locKey.put(LOC_KEY, pLocKeyValue);
        aps.put(ALERT_KEY, locKey);
      }

      // Always set content available to true so that an iOS app will temporarily
      // wake from the background upon receiving a notification
      aps.put(CONTENT_AVAILABLE_KEY, 1);
      notification.put(APS_KEY, aps);
      
      // If link is defined, add that to the notification
      if (!StringUtils.isBlank(pLink)) {
        notification.put(LINK_KEY, pLink);
      }
      
      // If the extra dictionary is defined, loop through the keys adding the
      // key-value pairs to the notification
      if (pExtra != null) {
        for (String key : pExtra.keySet()) {
          notification.put(key, pExtra.get(key));
        }
      }
    } catch (JSONException e) {
      throw new PushNotificationException(
          MobileStoreConfiguration.sResourceBundle.getString("apnsErrorCreatingNotification"),
          e);
    }
    return notification.toString();
  }
  
  /**
   * Converts an int to a byte array with length 4
   * @param pInt the int to convert to the byte array
   * @return byte array of length 4 that encodes the int provided
   */
  private static byte[] intToBytes(int pInt) {
    // Break int up into its 4 bytes by bit shifting then taking the small 8 bits
    // (1 byte) end of the shifted bits
    return new byte[]{(byte)(pInt >>> 24), (byte)(pInt >>> 16),
                      (byte)(pInt >>> 8),  (byte)pInt};
  }
  
  private static byte[] streamToBytes(InputStream pStream) throws IOException {
    if (pStream == null) {
      throw new IllegalArgumentException(
          MobileStoreConfiguration.sResourceBundle.getString("apnsInvalidInputStream"));
    }
    byte[] section = new byte[512]; 
    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    int bytesReadLength = 0;
    // Read from input stream in sections of 512 bytes
    while ((bytesReadLength = pStream.read(section)) != -1) {
      byteStream.write(section, 0, bytesReadLength);
    }
    return byteStream.toByteArray();
  }
  
  /**
   * Queries the apple feedback service to find which devices have had the app
   * uninstalled and, therefore, are failing to receive notifications
   * 
   * These devices should no longer be pushed to in order to increase performance
   * of apples push notification services.  Additionally, apple monitors providers
   * for their diligence in checking the feedback service and refraining from
   * sending push notifications to nonexistent applications on devices
   * 
   * Returns a map of device token to time that apple failed to push to the device
   * 
   * @return a map of device token to time that apple failed to push to the device
   * @throws PushNotificationException
   */
  private Map<String, Timestamp> getDeviceTokensFromFeedbackService() 
      throws PushNotificationException
  {
    // Get the feedback socket
    SSLSocket feedbackSocket = createSocket(getAppleFeedbackHost(), 
                                            getAppleFeedbackPort());
    
    // Start the handshake if there is no proxy. Otherwise, the handshake has
    // already been done
    if (getProxyHost() == null) {
      try {
        feedbackSocket.startHandshake();
      } catch (IOException e) {
        // Close feedback socket on error
        try {
          feedbackSocket.close();
        } catch (IOException io) {
          // Do nothing if socket fails to close
        } 
        // Throw push notification exception
        throw new PushNotificationException(
          MobileStoreConfiguration.sResourceBundle.getString("apnsErrorStartingHandshake"),
          e);
      }
    }
    
    // Create a map for mapping device token to the time that apple failed to
    // push to the device
    HashMap<String, Timestamp> tokenTimeMap = new HashMap<>();
    
    // Get the response from the feedback socket
    byte[] response = new byte[0];
    try {
      response = streamToBytes(feedbackSocket.getInputStream());
    } catch (IOException e) {
      throw new PushNotificationException(
          MobileStoreConfiguration.sResourceBundle.getString("apnsErrorConvertingStreamToBytes"),
          e);
    } finally {
      try {
        feedbackSocket.close();
      } catch (IOException e) {
        // We don't need to do anything if closing the socket failed here.
      }
    }

    // The response will have tuples of <time, token length, token>. The time is
    // 4 bytes. Token length is 2 bytes. The token is token length bytes
    int index = 0;
    // Make sure there is at least a time and a token length left in the response
    while (index + 6 <= response.length) {
      // Get the time and increment index
      byte[] timeBytes = Arrays.copyOfRange(response, index, index += 4);
      Timestamp timestamp = new Timestamp((new BigInteger(timeBytes)).longValue());
      
      // Get the token length and increment the index
      byte[] tokenLength = Arrays.copyOfRange(response, index, index += 2);
      int tokenLengthInt = (new BigInteger(tokenLength)).intValue();
      
      // If there are not enough bytes left for the token, log a warning and 
      // break the loop
      if (index + response.length < tokenLengthInt) {
        vlogWarning(
            MobileStoreConfiguration.sResourceBundle, "apnsIncompleteFeedbackServiceResponse");
        break;
      }
      
      // Get the device token and increment the index
      byte[] deviceToken = Arrays.copyOfRange(response, index, index += tokenLengthInt);
      
      // Parse the device token into a string
      StringBuilder deviceTokenBuilder = new StringBuilder();
      for (int i = 0; i < deviceToken.length; i ++) {
        deviceTokenBuilder.append(String.format("%02x",deviceToken[i]));
      }
      String deviceTokenString = deviceTokenBuilder.toString();
      
      // Add device token and time to the tokenTimeMap
      tokenTimeMap.put(deviceTokenString, timestamp);
    }
    return tokenTimeMap;
  }
  
  /**
   * Returns the transaction manager for the repository that we are using
   */
  private TransactionManager getTransactionManager() {
    Repository repository = getDeviceManager().getProfileRepository();
    if (repository instanceof RepositoryImpl) {
      return ((RepositoryImpl) repository).getTransactionManager();
    }
    return null;
  }
}

