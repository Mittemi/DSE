'use strict';

angular.module('myApp.listop', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/listop', {
    templateUrl: 'listop/listop.html',
    controller: 'listopCtrl'
  });
}])

.controller('listopCtrl', [function() {

}]);