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

package atg.projects.store.mobile.push;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import atg.core.util.StringUtils;
import atg.nucleus.Nucleus;
import atg.nucleus.ServiceAdminServlet;
import atg.repository.RepositoryItem;

/**
 * Admin Servlet used to push notifications from dynamo admin
 *
 * @author gdoneil
 * @date Sep 15, 2014
 */

public class PushNotificationManagerAdminServlet extends ServiceAdminServlet<PushNotificationManager>{
  private static final long serialVersionUID = 4479532014262719111L;

  public static String CLASS_VERSION =
      "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/src/atg/projects/store/mobile/push/PushNotificationManagerAdminServlet.java#2 $$Change: 1503965 $";

  private static final String PUSH_TO_USER = "user_push";
  
  // message types
  private static final String INFO                      = "info";
  private static final String ERROR                     = "error";
  
  // font colors
  private static final String BLACK                     = "black";
  private static final String RED                       = "red";
  
  // operations
  private static final String ACTION                    = "action";
  private static final String PROCESS                   = "process";

  private static final String MESSAGE                   = "message";
  private static final String PROFILE_ID                = "profileId";
  private static final String LINK                      = "link";
  
  
  private Map<String, Set<String>> mMessages = new HashMap<String, Set<String>>();
  private boolean mShowAction = true;

  public PushNotificationManagerAdminServlet(PushNotificationManager pService, Nucleus pNucleus) {
    super(pService, pNucleus);
  }
  
  /* (non-Javadoc)
   * @see atg.nucleus.ServiceAdminServlet#printAdmin(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.ServletOutputStream)
   */
  protected void printAdmin(HttpServletRequest pRequest, HttpServletResponse pResponse, 
      ServletOutputStream pOut ) throws ServletException, IOException 
  {
      

    // Print title
    pOut.println("<H3>Push Notification Manager Services</H3>");

    // Display page in order
    displayProcess(pRequest, pResponse, pOut);
    displayMessages(pRequest, pResponse, pOut);
    displayPushOptions(pRequest, pResponse, pOut);
    
    if(mShowAction) {
      displayAction(pRequest, pResponse, pOut);
    }
    
    // reset the value after processing
    mShowAction = true;
  }
  
  /**
   * Displays different options available for pushing notifications
   * @param pRequest
   * @param pResponse
   * @param pOut
   * @throws ServletException
   * @throws IOException
   */
  protected void displayPushOptions(HttpServletRequest pRequest, HttpServletResponse pResponse, 
    ServletOutputStream pOut ) throws ServletException, IOException  
  {
    pOut.println("<form method=\"post\" >");
    pOut.println("<table width=\"500\">");
    pOut.println("<tr><td colspan=\"3\"><h3>Select PNM action:</h3></td></tr>");
    pOut.println("<tr><td colspan=\"3\"><hr /></td></tr>");
    pOut.println("<tr>");
    pOut.println("<td><b>Action:</b></td>");
    pOut.println("<td>");
    pOut.println("<select name=\"action\">");
    pOut.println("<option value=\"" + PUSH_TO_USER + "\" >Send Notification to User ID</option>");
    pOut.println("</select>");
    pOut.println("</td>");
    pOut.println("<td><input type=\"submit\" value=\"Select\" /></td>");
    pOut.println("</tr>");
    pOut.println("<tr><td colspan=\"3\"><hr/></td></tr>");
    pOut.println("</table>");
    pOut.println("</form>");
  }

  /**
   * Displays INFO and ERROR messages as list elements
   * @param pRequest
   * @param pResponse
   * @param pOut
   * @throws ServletException
   * @throws IOException
   */
  protected void displayMessages(HttpServletRequest pRequest, HttpServletResponse pResponse, 
      ServletOutputStream pOut) throws ServletException, IOException  {
    
    if(mMessages != null && ! mMessages.isEmpty()) {
      pOut.println("<table width=\"100%\">");
      pOut.println("<tr>");
      pOut.println("<td width=\"75%\">");
      for(String messageType : mMessages.keySet()) {
        String color = getColorFromMessageType(messageType);
        pOut.println("<font color=\"" + color  + "\">");
        pOut.println("<ul>");
        
        Set<String> messages = (Set<String>) mMessages.get(messageType);
        for(String message : messages) { 
          pOut.println("<li>" + message);
        }
        
        pOut.println("</ol>");
        pOut.println("</font>");
      }
      pOut.println("</td>");
      pOut.println("<td width=\"25%\">&nbsp;</td>");
      pOut.println("</tr>");
      pOut.println("</table>");
      mMessages.clear();
    }
  }
  
  /**
   * Displays action that the user selected (i.e. Push to User form)
   * @param pRequest
   * @param pResponse
   * @param pOut
   * @throws ServletException
   * @throws IOException
   */
  protected void displayAction(HttpServletRequest pRequest, HttpServletResponse pResponse, 
      ServletOutputStream pOut ) throws ServletException, IOException  {
    
    String action = pRequest.getParameter(ACTION);
    if(StringUtils.isNotBlank(action)) {
      
      if(action.equalsIgnoreCase(PUSH_TO_USER)) {
        displayPushToUser(pRequest, pResponse, pOut);
      }
    }
  }
  
  /**
   * Displays the push to user form
   * @param pRequest
   * @param pResponse
   * @param pOut
   * @throws IOException
   */
  private void displayPushToUser(HttpServletRequest pRequest,
      HttpServletResponse pResponse, ServletOutputStream pOut) throws IOException {
    pOut.println("<form method=\"post\">");
    pOut.println("<input type=\"hidden\" name=\"" + PROCESS + "\" value=\"" + PUSH_TO_USER + "\" />");
    pOut.println("<input type=\"hidden\" name=\"" + ACTION + "\" value=\"" + PUSH_TO_USER + "\" />");
    pOut.println("<table width=\"100%\">");
    pOut.println("<tr><td colspan=\"4\"><h3>Send Notification to User's Devices</h3></td></tr>");
    pOut.println("<tr><td colspan=\"4\"><hr></hr></td></tr>");
    pOut.println("<tr>");
    pOut.println("<td width=\"150\"><b>Profile ID (required):</b></td>");
    pOut.println("<td width=\"250\"><input text=\"text\" name=\"" + PROFILE_ID + "\" size=\"35\" /></td>");
    pOut.println("<td width=\"150\"><b>Link URL (optional):</b></td>");
    pOut.println("<td><input type=\"text\" name=\"" + LINK + "\" size=\"35\" /></td>");
    pOut.println("</tr>");
    pOut.println("<tr><td colspan=\"4\">&nbsp;</td></tr>");
    pOut.println("<tr>");
    pOut.println("<td><b>Enter Message (optional):</b></td>");
    pOut.println("<td colspan=\"3\"><textarea name=\"" + MESSAGE + "\" rows=\"5\" cols=\"100\" /></textarea></td>");
    pOut.println("</tr>");
    pOut.println("<tr><td colspan=\"4\">&nbsp;</td></tr>");
    pOut.println("<tr>");
    pOut.println("<td colspan=\"4\"><input type=\"submit\" value=\"Send Notification\" /></td>");
    pOut.println("</tr>");
    pOut.println("<tr><td colspan=\"4\"><hr></hr></td></tr>");
    pOut.println("</table>");
    pOut.println("</form>");
  }

  /**
   * Displays the process that was chosen
   * @param pRequest
   * @param pResponse
   * @param pOut
   * @throws ServletException
   * @throws IOException
   */
  protected void displayProcess(HttpServletRequest pRequest, HttpServletResponse pResponse, 
      ServletOutputStream pOut) {
    
    String process = pRequest.getParameter(PROCESS);
    
    if(StringUtils.isNotBlank(process)) {
      if(process.equalsIgnoreCase(PUSH_TO_USER)) {
        processPushToUser(pRequest, pResponse, pOut);
      }
    }
  }
  
  /**
   * Processes request to push notifications to a single user.  Pushes that notification to all
   * of the user's devices that have a non-null push token
   * @param pRequest
   * @param pResponse
   * @param pOut
   */
  private void processPushToUser(HttpServletRequest pRequest,
      HttpServletResponse pResponse, ServletOutputStream pOut) {

    // Get request parameters
    String message   = pRequest.getParameter(MESSAGE);
    String profileId = pRequest.getParameter(PROFILE_ID);
    String link      = pRequest.getParameter(LINK);

    
    if(StringUtils.isBlank(profileId)) {
      addToMessages(ERROR, "No Profile ID specified. Cannot send push notification to null profile");
      return; // if no profile id, we cannot push a message. just log error and return
    }
    
    // Trim spaces from parameters
    profileId = profileId.trim();
    message   = message.trim();
    link      = link.trim();
    
    PushNotificationManager pushManager = (PushNotificationManager)this.getService();
    RepositoryItem[] devices;

    // Try fetching devices for the profileId provided. Catch errors and log them
    try {
      devices = pushManager.getDeviceManager().getDevicesByUser(profileId);
    } catch (Exception e) {
      addToMessages(ERROR, e.getMessage());
      return;
    }

    // Loop through devices and push to any that have a push token set
    for (int i = 0; i < devices.length; i ++) {
      // Fetch the push token
      String pushTokenKey = pushManager.getDeviceManager().getPropertyManager().getPushTokenPropertyName();
      String pushToken = (String)devices[i].getPropertyValue(pushTokenKey);

      // If push token exists, try pushing to the device
      if (!StringUtils.isEmpty(pushToken)) {
        String uniqueIdKey = pushManager.getDeviceManager().getPropertyManager().getUniqueIdPropertyName();
        String uniqueId = (String)devices[i].getPropertyValue(uniqueIdKey);

        // Try pushing to devices[i].  Log success or failure
        try {
          pushManager.sendNotification(uniqueId, message, null, link, null);
          addToMessages(INFO, "Successfully pushed to device with unique ID: " + uniqueId);
        } catch (Exception e) {
          String errorMessage = "Failed to push to device with unique ID: " + uniqueId + 
                                "Error: " + e.getMessage();
          addToMessages(ERROR, errorMessage);
        } 
      }
    }
  }

  /**
   * Retrieves the color for the message. RED for ERROR, BLACK for INFO
   * 
   * @param pMessageType
   * @return
   */
  private String getColorFromMessageType(String pMessageType) {
    
    String color = null;
    if(pMessageType != null) {
      if(pMessageType.equalsIgnoreCase(INFO)) {
        color = BLACK;
      }
      else if(pMessageType.equalsIgnoreCase(ERROR)) {
        color = RED;
      }
    }
    else {
      color = BLACK;
    }
    return color;
  }
  
  /**
   * Adds a message to the internal message map. 
   * 
   * @param pMessageType
   * @param pMessage
   */
  private void addToMessages(String pMessageType, String pMessage) {
    
    if(StringUtils.isNotBlank(pMessageType) && StringUtils.isNotBlank(pMessage)) {
      
      if(mMessages.containsKey(pMessageType)) {
        Set<String> currentMessages = mMessages.get(pMessageType);
        currentMessages.add(pMessage);
        mMessages.put(pMessageType, currentMessages);
      }
      else {
        Set<String> newMessages = new HashSet<String>();
        newMessages.add(pMessage);
        mMessages.put(pMessageType, newMessages);
      }
    }
  }
    

}

