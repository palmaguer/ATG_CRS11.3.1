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

import atg.endeca.assembler.AssemblerTools;
import atg.repository.Repository;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryException;

import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.cartridge.RecordDetails;
import com.endeca.infront.cartridge.RecordDetailsConfig;
import com.endeca.infront.cartridge.RecordDetailsHandler;
import com.endeca.infront.cartridge.model.Attribute;
import com.endeca.infront.cartridge.model.Record;


/**
 * This handler populates the content item with the details from repository
 * 
 * @author Oracle
 * @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Endeca/Assembler/src/atg/projects/store/assembler/cartridge/handler/StoreRecordDetailsHandler.java#3 $ 
 * @updated $DateTime: 2018/04/13 08:11:14 $
 */
public class StoreRecordDetailsHandler extends RecordDetailsHandler {

  //-------------------------------------
  /** Class version string */

  public static String CLASS_VERSION = "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Endeca/Assembler/src/atg/projects/store/assembler/cartridge/handler/StoreRecordDetailsHandler.java#3 $$Change: 1536476 $";

  

  //----------------------------------------------------------------------------
  // STATIC
  //----------------------------------------------------------------------------
  
  /**
   *  Property name to retrieve store repository id.
   */
  public static final String STORE_REPOSITORY_ID = "store.repositoryId";
  
  //----------------------------------------------------------------------------
  // PROPERTIES
  //----------------------------------------------------------------------------

  //------------------------------------
  //   property repository
  //------------------------------------
  private Repository mRepository = null;

  /**
   * @param pRepository
   *   Set the repository that contains store details.
   */
  public void setRepository( Repository pRepository ) {
    mRepository = pRepository;
  }

  /**
   * @return mRepository
   *   Get the respository that contains the store details. 
   */
  public Repository getRepository() {
    return mRepository;
  }
  
  //------------------------------------
  //   property itemDescriptorName
  //------------------------------------
  private String mItemDescriptorName = null;

  /**
   * @param pItemDescriptorName
   *   Name of the itemDescriptor to get the store details from repository.
   */
  public void setItemDescriptorName( String pItemDescriptorName ) {
    mItemDescriptorName = pItemDescriptorName;
  }

  /**
   * @return mItemDescriptorName
   *   Name of the itemDescriptor to get the store details from repository.
   */
  public String getItemDescriptorName() {
    return mItemDescriptorName;
  }

  //------------------------------------
  //   property storeProperties
  //------------------------------------
  
  String[] mStoreProperties = null;
  
  /** 
   * @param pStoreProperties 
   *   Sets the store Properties.
   */
  public void setStoreProperties( String[] pStoreProperties ) {
     mStoreProperties = pStoreProperties;
  }

  /**
   * @return mStoreProperties
   *   Returns the store properties.
   */
  public String[] getStoreProperties() {
    return mStoreProperties;
  }

  //----------------------------------------------------------------------------
  // METHODS
  //----------------------------------------------------------------------------  
  
  /**
   * Populates the data from repository to content item.
   * 
   * @param cartridgeConfig
   *   StoreDetails cartridge configuration.
   * @return recorddetails
   *   Return the details of the store.
   * @throws CartridgeHandlerException
   *   If an error occurs that is scoped to an individual cartridge
   *   instance. This exception will not halt the entire assembly process,
   *   which occurs across multiple cartridges; instead, this exception will
   *   be packaged in the overall response model. If an unchecked exception
   *   is thrown, then the entire assembly process will be halted.
   */
  public RecordDetails process(RecordDetailsConfig pCartridgeConfig)
    throws CartridgeHandlerException {
    
    RecordDetails recordDetails = super.process(pCartridgeConfig);
    Record record = recordDetails.getRecord();
    try {
      Repository repository = getRepository();
      RepositoryItem storeItem = repository.getItem(record.getAttributes()
        .get(STORE_REPOSITORY_ID).toString(), getItemDescriptorName());
      for(String propertyName : getStoreProperties()) {
        Attribute<Object> storeAttribute = new Attribute<>();
        Object property = storeItem.getPropertyValue(propertyName);
        storeAttribute.add(property);
        record.getAttributes().put(propertyName,storeAttribute);
      }
    }
    catch(RepositoryException re) {
      AssemblerTools.getApplicationLogging().vlogError(re,
        "There was a problem populating the content item : ", record.getAttributes().get(STORE_REPOSITORY_ID));
    }
    return recordDetails;
  }
}
