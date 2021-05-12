// Copyright (C) 1994, 2018, Oracle and/or its affiliates. All rights reserved.
define(["app/container/event/MainSwitchboard", "app/xmgr/event/XmgrEvents", "app/xmgr/view/editors/Editor", "core/TemplateEngine", "template!editors/custom/StringEditor/editor.html", "ojs/ojcomponents", "ojs/ojknockout", "ojs/ojinputtext"], function (MainSwitchboard, XmgrEvents, Editor, TemplateEngine, EditorTemplate) {
  "use strict";

  var TOOL_TIP = "tooltip";

  /**
   * @alias StringEditor
   * @classdesc Special Editor for editing properties whose value is a String.
   * Editor allows values only those which are in a specific pattern.
   * This pattern can be configured using a regular expression in editor's
   * configuration file i.e. _.json.
   * @extends app/xmgr/view/editors/Editor
   * @constructor
   */
  var StringEditor = function (pConfig) {
    var self = this;
    Editor.call(self, pConfig);
    /**
     * Pattern allowed to enter through editor. Default is null.
     * @type {null}
     */
    self.requiredPattern = pConfig.editorConfig.pattern;

    self.template(TemplateEngine.addSource(EditorTemplate));
  };

  StringEditor.prototype = Object.create(Editor.prototype);

  /**
   * This method initializes the editor instance. This should not be called explicitly,
   * as framework internally calls the same as part of editor's life cycle.
   * Should call initialize method of it's super class, passing the configuration object.
   * Will override this method to instantiate and initialize any custom controllers,
   * subscribe for any custom notifications etc.
   * @param pTemplateConfig Configuration object that holds configuration specified in cartridge template for this editor.
   * @param pContentItem ContentItem object which has the property that this editor works with.
   */
  StringEditor.prototype.initialize = function (pTemplateConfig, pContentItem) {
    var self = this;
    pTemplateConfig[TOOL_TIP] = self.editorBundle.getMessage("tooltip.information", self.requiredPattern);
    Editor.prototype.initialize.call(this, pTemplateConfig, pContentItem);
  };

  /**
   * This method binds the property object, whose value will be edited through this editor.
   * This should not be called explicitly, as framework internally calls the same as part of
   * editor's life cycle. Based on the type that you give for a property in cartridge template,
   * framework wraps property in an appropriate property class :
   * Allowed types are String, Boolean, Item, List, ContentItem, ContentItemList. Corresponding to these types framework wraps property using
   * StringProperty, BooleanProperty, JSONProperty (for both Item and List), ContentItemProperty, ContentItemListProperty.
   * Can override this method, if there is a need to a wrap property in a custom extension to the Property API.
   * For example in case of NumericStepperEditor, StringProperty can be wrapped using NumberProperty in editorReady.
   * If overridden, should call editorReady method of it's super class, passing the property object.
   */
  StringEditor.prototype.editorReady = function (pProperty) {
    var self = this;
    Editor.prototype.editorReady.call(self, pProperty);
  };

  /**
   * This method is to handle enabling save and converting preview to save and preview
   * @param data
   * @returns {boolean}
   */
  StringEditor.prototype.handleInputKey = function (pEvent) {
    if(pEvent.originalEvent) {
      MainSwitchboard.trigger(XmgrEvents.CONTENT_CHANGED, "true", this);
    }
  };

  return StringEditor;

});
