'use strict';

angular.module('myApp.slot', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider

      .when('/listslot', {
        templateUrl: 'slot/views/listslot.html',
        controller: 'listSlotCtrl'
      })
      .when('/addslot', {
        templateUrl: 'slot/views/addslot.html',
        controller: 'addSlotCtrl'
      })
      .when('/removeslot', {
        templateUrl: 'slot/views/removeslot.html',
        controller: 'removeSlotCtrl'
      })
}])

.controller('listSlotCtrl', [function() {

}])

    .controller('addSlotCtrl', [function() {

    }])

    .controller('removeSlotCtrl', [function() {

    }]);