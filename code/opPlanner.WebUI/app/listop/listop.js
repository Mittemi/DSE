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
           * Sets the filter on the type
           * @param type
           */
          $scope.setSelectedType = function(type){
              $scope.selectedType = type;
              $scope.openType = null;
          };

          /**
           * Sets the filter on the hospital
           * @param khname
           */
          $scope.setSelectedKH = function(khname){
                $scope.selectedKH = khname;
              $scope.openKH = null;
          };

          /**
           * Sets the filter on the doctor
           * @param docname
           */
          $scope.setSelectedDoc = function(docname){
              $scope.selectedDoc = docname;
              $scope.openDoc = null;
          };

          /**
           * Sets the filter on the patient
           * @param docname
           */
          $scope.setSelectedDoc = function(patient){
              $scope.selectedPatient = patient;
              $scope.openPatient = null;
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
    }]);