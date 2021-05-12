
CKEDITOR.editorConfig = function( config )
{
    config.enterMode=CKEDITOR.ENTER_DIV;
    config.DocType = '' ;

    config.BaseHref = '' ;

    config.fullPage = false ;

	config.extraPlugins = 'fwlinknewasset,fwlinkasset,fwincludenewasset,fwincludeasset,fwimagepicker,fwnoneditable,fweditcontextmenu';

	

	// If no customized toolbar specified then this is System Wide Global default toolbar
	config.toolbar_Default =
	[
			['Source','DocProps','-','Save','NewPage','Preview','-','Templates'],
			['Cut','Copy','Paste','PasteText','PasteWord','-','Print','SpellCheck'],
			['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
			['Form','Checkbox','Radio','TextField','Textarea','Select','Button','ImageButton','HiddenField'],
			'/',
			['Bold','Italic','Underline','StrikeThrough','-','Subscript','Superscript'],
			['NumberedList','BulletedList','-','Outdent','Indent','CreateDiv'],
			['JustifyLeft','JustifyCenter','JustifyRight','JustifyFull'],
			['Link','Unlink','Anchor'],
			['Image','Flash','Table','Rule','Smiley','SpecialChar','PageBreak'],
			'/',
			['Style','FontFormat','FontName','FontSize'],
			['TextColor','BGColor'],
			['FitWindow','ShowBlocks','-','About']		// No comma for the last row.
	] ;


	// Two FW Cutomized with ASSET Plugins ToolBar Configurations
	config.toolbar_FWToolbar =
	[
				['NewPage','Preview','Smiley'],
				['Cut','Copy','Paste','PasteText','PasteFromWord','-','Scayt'],
				['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
				['Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
				'/',
				['Styles','Format'],  ['fwfont']
				['Bold','Italic','Strike'],
				['NumberedList','BulletedList','-','Outdent','Indent'],
				,['Source'],['fwincludenewasset','fwincludeasset','fweditcontextmenu','fwimagepicker']
				
	];


	/*
	 *  Modified: June 22, 2010
		By:       JAG
		Reason:   CS toolbar added buttons, layout and re-ordering
	*/
	config.toolbar_CS =
	[
			['Source'],['Cut','Copy','Paste','PasteText','PasteFromWord','-','SpellChecker', 'Scayt'],
			['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],['Link','Unlink','Anchor','Table'],
			'/',
			['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
			['NumberedList','BulletedList','-','Outdent','Indent','CreateDiv'],
			['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
			['Styles','Format','Font','FontSize'],
			['TextColor','BGColor'],
			['Maximize', 'ShowBlocks'],
			'/',
			['fwlinknewasset','fwlinkasset','fwincludenewasset','fwincludeasset','fweditcontextmenu','fwimagepicker']
	];

	config.toolbar_SITES=
	[
		['Source'],['Cut','Copy','Paste','PasteText','PasteFromWord','-','SpellChecker', 'Scayt'],
		['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],['Link','Unlink','Anchor','Table'],
		'/',
		['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
		['NumberedList','BulletedList','-','Outdent','Indent','CreateDiv'],
		['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
		['Styles','Format','Font','FontSize'],
		['TextColor','BGColor'],
		['Maximize', 'ShowBlocks'],
		'/',
		['fwlinkasset','fwincludeasset','fweditcontextmenu','fwimagepicker']
	];

	// The default toolbar used by CKEditor widgets in Web Mode.
	config.toolbar_SITES_WEB =
	[
		['Bold','Italic','Underline','Strike', 'PasteText','PasteFromWord', '-', 'Maximize'],
		['Link','Unlink','Anchor'],
		['fwlinkasset','fwincludeasset','fweditcontextmenu','fwimagepicker'],
		['TextColor','Font','FontSize']
	];
	
	// A simpler CKEditor toolbar
	config.toolbar_SITES_WEB_SIMPLE = 
	[
		['Bold','Italic','Underline'], ['fwlinkasset','fwincludeasset','fweditcontextmenu']
	];
	
	config.urlFCKEditorRenderer= CKEDITOR.basePath + "../ContentServer?pagename=OpenMarket%2FXcelerate%2FActions%2FFCKEditorRenderer";
	config.entities = false;
};
