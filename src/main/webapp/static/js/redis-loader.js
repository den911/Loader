var REDIS_LOADER = (function () {
  'use strict';
  var socket = new WebSocket('ws://' + window.location.host + APP_CONFIG.contextPath + '/socketserver');
  var $context = null;

  setInterval(function () { socket.send("ping") }, 600000);

  socket.onmessage = function (event) {
    var message = JSON.parse(event.data);
    console.log(message);

    if (message.messageType == "jobStatus") {
      if (message.jobName.indexOf("MarketJob") == -1) {
        switch (message.status) {
          case 'started':
            onJobStarted(message);
            break;
          case 'finished':
            onJobFinished(message);
            break;
          case 'updated':
            onJobUpdated(message);
            break;
        }
      } else {
        switch (message.status) {
          case 'started':
            onJobStarted(message);
            onProfileJobMarketStarted(message);
            break;
          case 'updated':
            onProfileJobUpdated(message);
            break;
          case 'market-started':
            onProfileJobMarketStarted(message);
            break;
          case 'market-finished':
            break;
          case 'finished':
            onProfileJobFinished(message);
            break;
        }
      }
    }

    if (message.messageType == "queueStatus"){
      if ($context != null) {
        var queueClass = "." + message.queueName;
        if (message.stringQueue != null && message.stringQueue != '')
          $context.find(queueClass).html("<span>" + message.stringQueue + "</span>");
        else
          $context.find(queueClass).html("<span>Queue is empty</span>");
      }
    }
  };

  function onProfileJobFinished(message) {
    if ($context != null) {
      var $jobRow = $context.find("#job-" + message.jobName);
      $jobRow.find(".progress-indicator").addClass("hide");
      if (APP_CONFIG.userIsAuthorized) {
        $jobRow.find(".run-job-btn").prop("disabled", false);
        $jobRow.find(".stop-job-btn").prop("disabled", true);
        $jobRow.find(".stop-scan-queue").prop("disabled", true);
      }
      $jobRow.find(".finished .date").text(message.date);
      $jobRow.find(".finished .time").text(message.time);
      $jobRow.find(".records-number").text(message.totalRecordsNumber);
      var exitCode = "";
      if (message.completedMarkets > 0) {
        exitCode += "<span class=\"label label-success\">C: " + message.completedMarkets  + "</span>";
      }
      if (message.failedMarkets > 0) {
        exitCode += "<span class=\"label label-important\">F: " + message.failedMarkets  + "</span>";
      }
      if (message.stoppedMarkets > 0) {
        exitCode += "<span class=\"label label-info\">S: " + message.stoppedMarkets  + "</span>";
      }

      $jobRow.find(".exit-code").html(exitCode);
    }
  }

  function onProfileJobMarketStarted(message) {
    if ($context != null) {
      var $jobRow = $context.find("#job-" + message.jobName);
      $jobRow.find(".progress-indicator").removeClass("hide");
      if (APP_CONFIG.userIsAuthorized) {
        $jobRow.find(".run-job-btn").prop("disabled", true)
        $jobRow.find(".stop-job-btn").prop("disabled", false);
        $jobRow.find(".stop-scan-queue").prop("disabled", false);
      }
      $jobRow.find(".started .date").text(message.date);
      $jobRow.find(".started .time").text(message.time);
      $jobRow.find(".finished span").text("");
      $jobRow.find(".records-number").html("<span class='market-records-number'>0</span> (<span class='market'>"
      + (message.market===undefined? "": message.market)
      + "</span>) <span class='total-records-number'>"
      + (message.totalRecordsNumber===undefined? "": message.totalRecordsNumber)
      + "</span> (total)");
      $jobRow.find(".exit-code").html("<span class=\"label label-warning\">Started</span>");
    }
  }

  function onProfileJobUpdated(message) {
    if ($context != null) {
      var $jobRow = $context.find("#job-" + message.jobName);
      $jobRow.find(".market-records-number").text(message.recordsNumber);
    }
  }

  function onJobStarted(message) {
    if ($context != null) {
      var $jobRow = $context.find("#job-" + message.jobName);
      $jobRow.find(".progress-indicator").removeClass("hide");
      if (APP_CONFIG.userIsAuthorized) {
        $jobRow.find(".run-job-btn").prop("disabled", true)
        $jobRow.find(".stop-job-btn").prop("disabled", false);
        $jobRow.find(".stop-scan-queue").prop("disabled", false);
      }
      $jobRow.find(".started .date").text(message.date);
      $jobRow.find(".started .time").text(message.time);
      $jobRow.find(".finished span").text("");
      $jobRow.find(".records-number").text(message.recordsNumber);
      $jobRow.find(".exit-code").html("<span class=\"label label-warning\">Started</span>");
    }
  }

  function onJobFinished(message) {
    if ($context != null) {
      var $jobRow = $context.find("#job-" + message.jobName);
      $jobRow.find(".progress-indicator").addClass("hide");
      if (APP_CONFIG.userIsAuthorized) {
        $jobRow.find(".run-job-btn").prop("disabled", false);
        $jobRow.find(".stop-job-btn").prop("disabled", true);
        $jobRow.find(".stop-scan-queue").prop("disabled", true);
      }
      $jobRow.find(".finished .date").text(message.date);
      $jobRow.find(".finished .time").text(message.time);
      $jobRow.find(".records-number").text(message.recordsNumber);
      if (message.exitCode == "COMPLETED") {
        $jobRow.find(".exit-code").html("<span class=\"label label-success\">Completed</span>");
      } else if (message.exitCode == "STOPPED") {
        $jobRow.find(".exit-code").html("<span class=\"label label-info\">Stopped</span>");
      } else if (message.exitCode == "FAILED") {
        $jobRow.find(".exit-code").html("<span class=\"label label-important\">Failed</span>");
      } else {
        $jobRow.find(".exit-code").html(message.exitCode);
      }
    }
  }

  function onJobUpdated(message) {
    if ($context != null) {
      var $jobRow = $context.find("#job-" + message.jobName);
      $jobRow.find(".records-number").text(message.recordsNumber);
    }
  }

  function initButtons() {
    if (APP_CONFIG.userIsAuthorized) {
      $context.on("click", ".run-job-btn", function () {
        var jobName = $(this).closest("tr").attr("data-job-name");
        $.post("/run", {jobName : jobName});
        return false;
      });

      $context.on("click", ".stop-job-btn", function () {
        var jobName = $(this).closest("tr").attr("data-job-name");
        $.post("/stop", {jobName : jobName});
        alert("Внимание! Отправлен команда на остановку задачи. Процесс остановки может занять некоторое время.");
        return false;
      });

      $context.on("click", ".stop-scan-queue", function () {
        var jobName = $(this).closest("tr").attr("data-job-name");
        $.post("/stopQueueJob", {jobName : jobName});
        alert("Внимание! Отправлен команда на остановку задачи. Процесс остановки может занять некоторое время.");
        return false;
      });
    }

    $context.on("click", ".infoLink", function () {
      var p = $(this).parent();
      $("#infoWindow .modal-header h3").text($(".jobName", p).text());
      $("#infoWindow .desc").text($(".desc", p).val());
      $("#infoWindow .sql").text($(".sql", p).val());
      $("#infoWindow").modal("show");
      return false;
    });

  }

  function init(context) {
    $context = context;
    initButtons();
  }

  return {init : init};
}());

$(function () {
  REDIS_LOADER.init($('.jobs-table'));
});