'use strict';

angular.module('myApp.slot', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider
      .when('/listslot', {
        templateUrl: 'slot/listslot.html',
        controller: 'listSlotCtrl'
      })
}])

.controller('listSlotCtrl', [function() {

}])