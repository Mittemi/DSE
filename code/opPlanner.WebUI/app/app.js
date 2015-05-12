'use strict';

// Declare app level module which depends on views, and components
angular.module('Authentication', []);
angular.module('myApp', [
    'Authentication',
    'myApp',
    'ngRoute',
    'ngCookies',
    'myApp.view1',
    'myApp.view2',
    'myApp.version'
])
.config(['$routeProvider', function($routeProvider) {
  $routeProvider
    .when('/login', {
        controller: 'LoginController',
        templateUrl: 'authentication/views/login.html'
    })
    .when('/',{
        redirectTo: '/login'
    })
  .otherwise({redirectTo: '/login'});
}])
.run(['$rootScope', '$location', '$cookieStore', '$http',
    function ($rootScope, $location, $cookieStore, $http) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookieStore.get('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
        }
  
        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in
            if ($location.path() !== '/login' && !$rootScope.globals.currentUser) {
                $location.path('/login');
            }
        });
    }
]);


