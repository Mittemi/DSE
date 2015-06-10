'use strict';

angular.module('myApp.slot', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider
      .when('/listslot', {
        templateUrl: 'slot/listslot.html',
        controller: 'listSlotCtrl'
      })
}])
.controller('listSlotCtrl', ['$scope','$http','$location',
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
    }]);
