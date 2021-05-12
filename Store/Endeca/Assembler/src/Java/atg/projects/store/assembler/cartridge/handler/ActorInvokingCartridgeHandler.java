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

package atg.projects.store.assembler.cartridge.handler;


import atg.nucleus.ResolvingMap;
import atg.service.actor.ActorExecutor;
import atg.service.actor.Actor;

import com.endeca.infront.assembler.BasicContentItem;
import com.endeca.infront.assembler.ContentItem;
import com.endeca.infront.cartridge.NavigationCartridgeHandler;

/**
 * <p>
 *   ActorInvokingCartridgeHandler - this cartridge invokes a user specified Actor in the process() method and passes the Actor parameters defined in the contentItemAdditionsMap property
 * </p>
 * <p>
 *   Properties:
 *   <ul>
 *     <li>
 *       actorToInvoke - the Actor that will be invoked in the process method.  ex: /atg/endeca/assembler/cartridge/actor/HomepageTargetedItemsActor
 *     </li>
 *     <li>
 *       actorExecutor - the ActorExecutor that will invoke the actorToInvoke
 *     </li>
 *     <li>
 *       contentItemAdditionsMap - ResolvingMap of properties that will be put into the ContentItem passed to the wrapConfig method.  Essentially, these are properties
 *       that will be passed to actorToInvoke through the ContentItem
 *     </li>
 *   </ul>
 * </p>
 * 
 * @author Yekaterina Kostenevich
 * @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Endeca/Assembler/src/atg/projects/store/assembler/cartridge/handler/ActorInvokingCartridgeHandler.java#3 $$Change: 1536476 $
 * @updated $DateTime: 2018/04/13 08:11:14 $$Author: releng $
 */
public class ActorInvokingCartridgeHandler extends NavigationCartridgeHandler<ContentItem, ContentItem> {
  
  public static final String CLASS_VERSION = "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Endeca/Assembler/src/atg/projects/store/assembler/cartridge/handler/ActorInvokingCartridgeHandler.java#3 $$Change: 1536476 $";
  
  //---------------------------------------------------------------------------
  // PROPERTIES
  //---------------------------------------------------------------------------
  
  //-----------------------------------
  // property actorExecutor
  //-----------------------------------
  private ActorExecutor mActorExecutor = null;

  /**
   * @return the actorExecutor
   */
  public ActorExecutor getActorExecutor() {
    return mActorExecutor;
  }

  /**
   * @param pActorExecutor the actorExecutor to set
   */
  public void setActorExecutor(ActorExecutor pActorExecutor) {
    mActorExecutor = pActorExecutor;
  }
  
  //-----------------------------------
  // property actorToInvoke
  //-----------------------------------
  private Actor mActorToInvoke = null;

  /**
   * @return the ActorToInvoke
   */
  public Actor getActorToInvoke() {
    return mActorToInvoke;
  }

  /**
   * @param pActorToInvoke the actor that will be invoked in the process method
   */
  public void setActorToInvoke(Actor pActorToInvoke) {
    mActorToInvoke = pActorToInvoke;
  } 
  
  //-----------------------------------
  // property: ContentItemAdditionsMap
  private ResolvingMap mContentItemAdditionsMap = new ResolvingMap();
  
  /**
   * @return A map whose keys are cartridge types and whose values are handler
   * components.
   */
  public ResolvingMap getContentItemAdditionsMap() {
    return mContentItemAdditionsMap;
  }
  
  /**
   * @param pContentItemAdditionsMap Set a new ContentItemAdditionsMap map
   */
  public void setContentItemAdditionsMap(ResolvingMap pContentItemAdditionsMap) {
    mContentItemAdditionsMap = pContentItemAdditionsMap;
  }

  //-----------------------------------
  // property: outputModelPropertyName
  private String mOutputModelPropertyName = "atg:contents";

  /**
   * The property name in ContentItem of the actor output
   *
   * @return the outputModelPropertyName
   */
  public String getOutputModelPropertyName() {
    return mOutputModelPropertyName;
  }

  /**
   * Sets the property name in ContentItem of the actor output
   *
   * @param pOutputModelPropertyName the outputModelPropertyName to set
   */
  public void setOutputModelPropertyName(String pOutputModelPropertyName) {
    mOutputModelPropertyName = pOutputModelPropertyName;
  }

  //---------------------------------------------------------------------------
  // METHODS
  //---------------------------------------------------------------------------
  
  /**
   * Create a new BasicContentItem using the passed in ContentItem.  The map defined in
   * the properties file will be put into the BasicContentItem
   * 
   * @param pContentItem
   *   The cartridge content item to be wrapped.
   * @return a new TargetedItems configuration.
   */
  @Override
  protected ContentItem wrapConfig(ContentItem pContentItem) {
    BasicContentItem contentItem = new BasicContentItem(pContentItem);
    contentItem.putAll(getContentItemAdditionsMap());
    return contentItem;
  }

  /**
   * <p>
   *   This method invokes the actor "actorToInvoke."  The properties set in "contentItemsAdditionsMap" will be passed
   *   to the actor in "currentContentItem."
   * </p>
   * 
   * @param pCartridgeConfig ContentItem to pass to the actor
   * @return 
   *   A fully configured ContentItem.
   */
  @Override
  public ContentItem process(ContentItem pCartridgeConfig) 
  {
    if(getActorExecutor() != null){
      Object modelMap = getActorExecutor().invokeActor(getActorToInvoke(), pCartridgeConfig);
      pCartridgeConfig.put(getOutputModelPropertyName(), modelMap);
    }

    return pCartridgeConfig;
  }
}
