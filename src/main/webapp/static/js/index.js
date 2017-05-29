jQuery(document).ready(function () {
    var enableAllCron = $(".enableAllCron");
    var checkboxList = $(".enableCron");
    if (checkboxList.not(":checked").length === 0) {
      enableAllCron.prop("checked", true);
    }

    enableAllCron.change( function () {
        $(".enableCron").prop("checked", this.checked);            
    }); 
});