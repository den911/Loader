var EDIT_PROFILE_JOB = (function () {
  var $context = null;

  function initCheckAll() {
    var $checkbox = $context.find("#check-all");
    var checkboxList = $context.find(".market-checkbox");
    if (checkboxList.not(":checked").length == 0) {
      $checkbox.prop("checked", true);
    }

    $checkbox.change(function() {
      console.log("change");
      checkboxList.prop("checked", $checkbox.prop("checked"));
    });
  }

  function initLoad() {
    var marketForm = $context.find("#markets_form").get(0);
    var loadForm = $context.find("#load_form").get(0);    
    $("button", loadForm).click(function () {
        marketForm.action = loadForm.action;
        marketForm.submit();
        return false;
    });
  }
  
  function init(context) {
    $context = context;
    initCheckAll();
    initLoad();    
  }

  return {init : init};
}());

$(function () {
  EDIT_PROFILE_JOB.init($('.container'));
});