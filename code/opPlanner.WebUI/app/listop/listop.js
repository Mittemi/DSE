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
                  $scope.oplist = data;
              });


          /**
           * Deletes a offered op slot if and only if the slot isn't reserved
           * @param id
           */
          $scope.removeOpSlot = function(id) {

              $http.delete('http://localhost:8080/opslots/' + id)
                  .success(function (data, status, headers, config) {
                      // this callback will be called asynchronously
                      // when the response is available
                  })
                  .error(function (data, status, headers, config) {
                      $location.path('/#/listop');
                  });
          };



          /**
           * Supress the build-in sorting in ng-repeat, JSON format is preserved
           * @param obj
           * @returns {Array}
           */
          $scope.notSorted = function(obj){
              if (!obj) {
                  return [];
              }
              return Object.keys(obj);
          };
}])

.controller('OpSlotFormController', ['$scope','$http', function($scope,$http) {

        $scope.newOpSlot = function(){

            var json = " { 'type' : '" + $scope.data.opType  + "', 'slotStart' : " + $scope.data.slotStart.getTime() + ", 'slotEnd' : " + $scope.data.slotEnd.getTime() + " }";
            $http.put('http://localhost:8080/opslots/create', json)
                .success(function (data, status, headers, config) {

                })
                .error(function (data, status, headers, config) {

                });
        };
    }]);
