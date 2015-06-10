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
    'myApp.listop',
    'myApp.notifications',
    'myApp.slot',
    'myApp.home',
    'myApp.version',
    'ui.bootstrap.datetimepicker',
    'angular.filter'
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
        redirectTo: '/home'
    });
})

.run(['$rootScope', '$location', '$cookieStore', '$http',
    function ($rootScope, $location, $cookieStore, $http) {
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
        }


        /**
         * Forces Login before entering any other page -> because of
         */
        /*
            $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in
            if ($location.path() !== '/login' && !$rootScope.globals.currentUser) {
                $location.path('/login');
            }
        });*/
    }
]);




