'use strict';

angular.module('myApp.notifications', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/notifications', {
    templateUrl: 'notifications/notifications.html',
    controller: 'notificationsCtrl'
  });
}])

.controller('notificationsCtrl', [function() {

}]);