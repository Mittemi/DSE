'use strict';

angular.module('myApp.notifications', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/notifications', {
    templateUrl: 'notifications/notifications.html',
    controller: 'notificationsCtrl'
  });
}])

.controller('notificationsCtrl', ['$scope','$http','$location',
  function($scope,$http,$location) {

    $http.get('http://'+$location.host()+':8080/notification/list').
        success(function(data, status, headers, config) {
          $scope.notificationlist = data;
        }).
        error(function(data, status, headers, config) {
          $scope.notificationlist = data;
        });



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
