'use strict';

// Declare app level module which depends on views, and components
angular.module('Authentication', [
    'Authentication'
]);
angular.module('myApp', [
    'myApp',
    'ngRoute',
    'Authentication',
    'ngCookies',
    'myApp.notifications',
    'myApp.listop',
    'myApp.home',
    'myApp.version',
    'ui.bootstrap.datetimepicker',
    'angular.filter',
    'ui.bootstrap'
])
.config(function($routeProvider, $locationProvider) {
  $routeProvider

    .when('/login', {
        controller: 'LoginController',
        templateUrl: 'authentication/views/login.html'
    })
      .when('/logout', {
          controller: 'LogoutController',
          templateUrl: 'authentication/views/logout.html'
      })
    .otherwise(
    {
        redirectTo: '/listop'
    });
})

.run(['$rootScope', '$location', '$cookieStore', '$http',
    function ($rootScope, $location, $cookieStore, $http) {
        // check server connection
        $http.get('http://'+$location.host()+':8080/').
            error(function(data, status, headers, config) {
                alert("Error while trying to communicate with APIGateway \nPlease check connection.")
            });


        // keep user logged in after page refresh
        $rootScope.globals = $cookieStore.get('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata;
        }


        /**
         * Highlighting of Menue Items in Bootstrap`s navbar
         * See also http://stackoverflow.com/questions/12592472/how-to-highlight-a-current-menu-item
         * @param path
         * @returns {*}
         */
        $rootScope.getClass = function (path) {
            if ($location.path().substr(0, path.length) == path) {
                return "active"
            } else {
                return ""
            }
        };
    }
]);




