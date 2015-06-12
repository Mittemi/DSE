'use strict';

angular.module('myApp.listop', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/listop', {
    templateUrl: 'listop/listop.html',
    controller: 'listopCtrl'
  });
}])

.controller('listopCtrl', ['$scope','$http','$location',
      function($scope,$http,$location) {

          $scope.selectKH = function(tmp){
              $scope.selectedKH = tmp;
          };
          $scope.selectDoc = function(tmp){
              $scope.selectedDoc = tmp;
          };
          $scope.selectType = function(tmp){
              $scope.selectedType = tmp;
          };
          $scope.selectPatient = function(tmp){
              $scope.selectedPatient = tmp;
          };

          $http.get('http://localhost:8080/opslots/list').
              success(function(data, status, headers, config) {
                  $scope.oplist = data;
              }).
              error(function(data, status, headers, config) {
                  alert("Error while recieving data from server. \nPlease check connection.")
              });


          /**
           * Deletes a offered op slot if and only if the slot isn't reserved
           * @param id
           */
          $scope.removeOpSlot = function(id) {

              $http.delete('http://localhost:8080/opslots/' + id)
                  .success(function (data, status, headers, config) {
                     $scope.getListFromServer();
                  })
                  .error(function (data, status, headers, config) {
                      alert("Error while trying to delete. \nPlease check server connection.");
                  });
          };

           /**
           * Sets the StartDateFilter
           * @param start
           */
          $scope.setSelectedStartDate = function(start){
              $scope.selectedStart = start;
              $scope.getListFromServer();
              $scope.openstartTime = null;
          };

          /**
           * Sets the EndDateFilter
           * @param end
           */
          $scope.setSelectedEndDate = function(end){
              $scope.selectedEnd = end;
              $scope.getListFromServer();
              $scope.openendTime2 = null;
          };

          /**
           * Requests op slot list from server and adds from and to parameters to the requests
           * Afterwards the list will be refreshed
           */
          $scope.getListFromServer = function () {
              var params = "?";
              if($scope.selectedStart != null){
                  params += "from=" + $scope.selectedStart.getTime();
              }
              if(params != "?" && $scope.selectedEnd != null){
                  params += "&";
              }
              if($scope.selectedEnd != null){
                  params += "to=" + $scope.selectedEnd.getTime();
              }

              $http.get('http://localhost:8080/opslots/list' + params).
                  success(function (data, status, headers, config) {
                      $scope.oplist = data;
                  }).
                  error(function (data, status, headers, config) {
                      alert("Error while getting Server data. \nPlease check connection.")
                  });
          };
}])

.controller('OpSlotFormController', ['$scope','$http', function($scope,$http) {

        $scope.newOpSlot = function(){

            var json = '{"type" : "' + $scope.data.opType  + '", "slotStart" : ' + $scope.data.slotStart.getTime() + ', "slotEnd" : ' + $scope.data.slotEnd.getTime() + ' }';
            $http.put('http://localhost:8080/opslots/create', json)
                .success(function (data, status, headers, config) {
                })
                .error(function (data, status, headers, config) {
                    alert("Error while creating new Operation Slot. \nPlease check connection to server.")
                });
            $scope.getListFromServer();
            $scope.showfield = false;
        };

        /**
         * GetDate Formatted
         */
        $scope.formatDate = function(time,format){
            return moment(time).format(format);
        };

    }])

.controller('DatepickerCtrl', function ($scope) {
    $scope.today = function() {
        $scope.dt = new Date();
    };
    $scope.today();

    $scope.clear = function () {
        $scope.dt = null;
    };

    // Disable weekend selection
    $scope.disabled = function(date, mode) {
        return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
    };

    $scope.toggleMin = function() {
        $scope.minDate = $scope.minDate ? null : new Date();
    };
    $scope.toggleMin();

    $scope.open = function($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    };

    $scope.dateOptions = {
        formatYear: 'yy',
        startingDay: 1
    };

    $scope.formats = ['dd.MM.yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[0];

    var tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    var afterTomorrow = new Date();
    afterTomorrow.setDate(tomorrow.getDate() + 2);
    $scope.events =
        [
            {
                date: tomorrow,
                status: 'full'
            },
            {
                date: afterTomorrow,
                status: 'partially'
            }
        ];

    $scope.getDayClass = function(date, mode) {
        if (mode === 'day') {
            var dayToCheck = new Date(date).setHours(0,0,0,0);

            for (var i=0;i<$scope.events.length;i++){
                var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);

                if (dayToCheck === currentDay) {
                    return $scope.events[i].status;
                }
            }
        }

        return '';
    };



    });
