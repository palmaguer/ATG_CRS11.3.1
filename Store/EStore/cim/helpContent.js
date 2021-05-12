/* Commerce Reference Store help */	
{
  "helpItem":
	[


		{"id":"ProductSelectTask.products.store","content":"Commerce Reference Store: Configure all Oracle Commerce Platform products required to support Oracle Commerce Reference Store. See the Commerce Reference Store Installation and Configuration Guide for detailed information."},

		{"id":"AddOnSelectTask.addOns.storefront_demo","content":"Your installation will include one or more Oracle Commerce Reference Store demonstration storefronts. If you choose to configure Commerce Reference Store with the full complement of sample data, this option will result in three storefronts: CRS Store US, CRS Store Germany, and CRS Home. These are full-featured storefronts with catalogs, price lists, promotions, etc. If you choose to configure Commerce Reference Store with the minimum sample data required for start up, this option includes the CRS Basic storefront only, which is a barebones storefront without a catalog, price lists, and so on."},

		{"id":"AddOnSelectTask.addOns.cybersource","content":"Your installation will include Oracle Commerce Reference Store-specific extensions to Oracle Core Commerce's CyberSource integration functionality."},


		{"id":"AddOnSelectTask.addOns.international","content":"Your installation will include the CommerceReferenceStore.Store.EStore.International module. This module is necessary for sites that will support multiple languages or multiple countries. If you do not install the International module, your production instance of Commerce Reference Store will include the English versions of CRS Store US and CRS Home only. You will not see CRS Store Germany or the Spanish translations for CRS Store US and CRS Home."},

		{"id":"AddOnSelectTask.addOns.fulfillment","content":"Your installation will be configured to use Commerce Reference Store fulfillment."},

		{"id":"AddOnSelectTask.addOns.recommendations","content":"Your installation will include Oracle Recommendations on Demand so that it can display recommended products on the category and product detail pages. If you have also chosen to use the Internationalization module, then selecting this option adds the CommerceReferenceStore.Store.Recommendations.International module to your application. If you are not using internationalization, then the CommerceReferenceStore.Store.Recommendations module is added instead."},

		{"id":"AddOnSelectTask.addOns.rightNow-knowledgeBase","content":"Your installation will include Oracle RightNow Knowledge Cloud Service so that it can display knowledge base content on product detail pages. If you have also chosen to use the Internationalization module, then selecting this option adds the CommerceReferenceStore.Store.KnowledgeBase.International module to your application. If you are not using internationalization, then the CommerceReferenceStore.Store.KnowledgeBase module is added instead."},

		{"id":"AddOnSelectTask.addOns.storefront_no_publishing","content":"Use this option to include the CommerceReferenceStore.Store.Storefront.NoPublishing module in your configuration. This module includes all file-based assets, such as targeters and scenarios, for Commerce Reference Store and it allows you to see these assets in the running Commerce Reference Store application without performing a full deployment.\n\nNote that this option should only be used in development environments. Typically, file-based assets should be imported into a Publishing server then deployed to a Production server through the Oracle Commerce Business Control Center. This best-practice process ensures that file-based assets are managed properly through the Content Administration's versioned file store. However, it also requires that you set up Content Administration and run a full deployment. For demonstration purposes, where you don't want the overhead of setting up Content Administration, you can choose to include the NoPublishing module, so that file-based assets appear in your application without a full deployment. This means, however, that the file-based assets are not accessible via the Business Control Center and cannot be easily removed from the site. For this reason, do not use this option for configurations that will ultimately be moved to a production environment."},

		{"id":"AddOnSelectTask.addOns.fluoroscope","content":"Use this option to include a tool for viewing site HTML pages that reveals key JSP elements involved in rendering those pages, such as page includes, servlet beans, scenario events and actions, and form fields."},

		{"id":"AddOnSelectTask.addOns.storefront-full-setup","content":"Use this option to configure Oracle Commerce Reference Store with a full complement of sample data."},

		{"id":"AddOnSelectTask.addOns.storefront-basic-setup","content":"Use this option to configure an instance of Oracle Commerce Reference Store that includes the bare minimum of sample data required for start up (ie, no catalogs, price lists, etc.)."},

		{"id":"/atg/dynamo/service/preview/Localhost_properties.hostName","content":"Enter the host name of the preview server."},

		{"id":"/atg/dynamo/service/preview/Localhost_properties.port","content":"Enter the port number of the preview server."},

		{"id":"AddOnSelectTask.addOns.mobileCRS","content":"Your installation will include the Mobile Reference Store, a version of Oracle Commerce Reference Store for web-enabled mobile devices."},

		{"id":"AddOnSelectTask.addOns.mobilecommerce-REST","content":"Your installation will include the REST Web Services provided by the Mobile Reference Store."},
    ]
}
