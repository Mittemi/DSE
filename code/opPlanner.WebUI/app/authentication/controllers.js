'use strict';
  
angular.module('Authentication')


.controller('LoginController',
    ['$scope', '$rootScope', '$location', 'AuthenticationService',
    function ($scope, $rootScope, $location, AuthenticationService) {
        AuthenticationService.ClearCredentials();

        /**
         * Performs a new Login for given username and password scope
         */
        $scope.login = function () {
            $scope.dataLoading = true;
            AuthenticationService.Login($scope.username, $scope.password,

                function(response) {
                if(response.error){
                    $scope.error = response.message;
                    $scope.dataLoading = false;
                }
                else {
                    $scope.error = response.message;
                    console.log(response);
                    AuthenticationService.SetCredentials($scope.username, $scope.password, response.roles);
                    $location.path('/');
                }
            });
        };
    }])


.controller('LogoutController',
    ['$scope', '$rootScope', '$location', 'AuthenticationService',
        function ($scope, $rootScope, $location, AuthenticationService) {
            AuthenticationService.ClearCredentials();
}]);

