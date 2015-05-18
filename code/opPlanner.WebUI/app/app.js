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
    'myApp.view1',
    'myApp.view2',
    'myApp.home',
    'myApp.version'
])
.config(function($routeProvider, $locationProvider) {
  $routeProvider
      .when('/home', {
          controller: 'homeCtrl',
          templateUrl: 'home/home.html'
      })
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
        /*
            $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in
            if ($location.path() !== '/login' && !$rootScope.globals.currentUser) {
                $location.path('/login');
            }
        });*/
    }
]);


