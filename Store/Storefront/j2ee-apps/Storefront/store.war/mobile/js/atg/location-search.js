/**
 * "Locationsearch" Javascript functions.
 * @ignore
 */
CRSMA = window.CRSMA || {};

/**
 * @namespace "Locationsearch" Javascript Module of "Commerce Reference Store Mobile Application"
 * @description Holds functionality related to location search actions in CRSMA.
 */
CRSMA.locationsearch = function() {
  
  /**
   * Removes Ns and nav query parameters in case geocrumb is getting removed.
   * 
   * @param pUrl - String URL to be modified.
   * @param pGeoProperty - Mdex geo-property field that should be removed.
   * 
   * @return uri without Ns and nav parameters.
   *
   * @public
   */
  var clearGeoSort = function(pUrl, pGeoProperty) {
    var uri = decodeURIComponent(pUrl.replace(/\+/g,  " "));
    var query = {};
    if (uri.indexOf("?") != -1) {
      query = getQueryParamsObject(uri.substring(uri.indexOf("?") + 1, uri.length));
      if (query.Ns && query.Ns.indexOf(pGeoProperty) != -1) {
        delete query.Ns;
      }
      query.nav = "true";
      uri = uri.substring(0, uri.indexOf("?"));
    }
    uri += "?" + decodeURIComponent($.param(query));
    return uri;
  };

  /**
   * Removes nav query parameters from current url before navigating to details
   * page. This ensures that when user navigates back, by default result list 
   * tab is displayed.
   *
   * @public
   */  
  var clearNavParamInHistoryBeforeMove = function() {
    var uri = decodeURIComponent(window.location.href.replace(/\+/g,  " "));
    var query = {};
    if (uri.indexOf("?") != -1) {
      query = getQueryParamsObject(uri.substring(uri.indexOf("?") + 1, uri.length));
      if (query.nav) {
        delete query.nav;
      }
      uri = uri.substring(0, uri.indexOf("?"));
    }
    uri += "?" + decodeURIComponent($.param(query));
    
    history.replaceState({},document.title,uri);
  };
  
  /**
   * This method is used in search near me and distance refinements cartridges 
   * to access shopper's current location and submit form. In case location 
   * service is disabled, appropriate errors are displayed.
   * 
   * @param pRefinement - Facet li for animation.
   * @param pNewRadius - New radius to be applied in geo search.
   * @param pGeoProperty - Mdex geo-property field used for Endeca query.
   * 
   * @public
   */
  var setLocationAndSubmit = function(pRefinement, pNewRadius, pGeoProperty) {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(function(pPosition){
        var uri = decodeURIComponent(window.location.href.replace(/\+/g,  " "));
        var parametersObject;
        if (uri.indexOf("?") == -1){
          parametersObject = {
            Nfg: pGeoProperty + "|" + 
                pPosition.coords.latitude + "|" + 
                pPosition.coords.longitude + "|" + 
                pNewRadius,
            Ns: pGeoProperty + "(" + 
                pPosition.coords.latitude + "," + 
                pPosition.coords.longitude + ")|0",
            nav: "true"
          };
        } else {
          var delim = "?";
          parametersObject = getQueryParamsObject(uri.substring(uri.indexOf("?") + 1, uri.length));
          parametersObject.Nfg = pGeoProperty + "|" + 
              pPosition.coords.latitude + "|" + 
              pPosition.coords.longitude + "|" + 
              pNewRadius;
          parametersObject.Ns = pGeoProperty + "(" + 
              pPosition.coords.latitude + "," + 
              pPosition.coords.longitude + ")|0";
          parametersObject.nav = "true";
          uri = uri.substring(0, uri.indexOf("?"));
        }
        uri += "?" + decodeURIComponent($.param(parametersObject));

        CRSMA.search.applyFacet(pRefinement, uri);
      
      },function(error) {
        if(error.PERMISSION_DENIED) {
          $('#GeoNotSupported')[0].style.display='none';
          $('#GeoRequestDenied')[0].style.display='inline';
        }
      });
    }
    else {
      $('#GeoRequestDenied')[0].style.display='none';
      $('#GeoNotSupported')[0].style.display='inline';
    }
  };  

  /**
   * Turns String URL into query parameters object.
   * @param pQueryString - String URL to be transformed.
   *
   * @return parameters object (object[paramName] = paramValue)
   *
   * @private
   */
  var getQueryParamsObject = function(pQueryString) {
    var parametersObject = {};
    if (pQueryString.length != 0) {
      var paramsArray = pQueryString.split("&");
      for (var i = 0, len = paramsArray.length; i < len; i++) {
        var paramArray = paramsArray[i].split("=");
        parametersObject[paramArray[0]] = unescape(paramArray[1]);
      }
    }
    return parametersObject;
  };

  /**
   * List of "CRSMA.locationsearch" public methods.
   */
  return {
    "clearGeoSort"                     : clearGeoSort,
    "clearNavParamInHistoryBeforeMove" : clearNavParamInHistoryBeforeMove,
    "setLocationAndSubmit"             : setLocationAndSubmit
  }
}();