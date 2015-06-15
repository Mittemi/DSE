'use strict';

angular.module('myApp.listop', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/listop', {
            templateUrl: 'listop/listop.html',
            controller: 'listopCtrl'
        });
    }])

    .controller('listopCtrl', ['$rootScope','$scope', '$http', '$location',
        function ($rootScope, $scope, $http, $location) {

            $rootScope.startInformation = null;
            $rootScope.endInformation = null;

            $scope.selectKH = function (tmp) {
                $scope.selectedKH = tmp;
            };
            $scope.selectDoc = function (tmp) {
                $scope.selectedDoc = tmp;
            };
            $scope.selectType = function (tmp) {
                $scope.selectedType = tmp;
            };
            $scope.selectPatient = function (tmp) {
                $scope.selectedPatient = tmp;
            };

            $http.get('http://localhost:8080/opslots/list').
                success(function (data, status, headers, config) {
                    $scope.oplist = data;
                }).
                error(function (data, status, headers, config) {
                    alert("Error while recieving data from server. \nPlease check connection.")
                });


            /**
             * Deletes a offered op slot if and only if the slot isn't reserved
             * @param id
             */
            $scope.removeOpSlot = function (id) {

                $http.delete('http://localhost:8080/opslots/' + id)
                    .success(function (data, status, headers, config) {
                        $scope.getListFromServer();
                    })
                    .error(function (data, status, headers, config) {
                        alert("Error while trying to delete. \nPlease check server connection.");
                    });
            };


            /**
             * Requests op slot list from server and adds from and to parameters to the requests
             * Afterwards the list will be refreshed
             */
            $rootScope.getListFromServer = function () {
                var params = "?";
                if ($rootScope.startInformation != null) {
                    params += "from=" + $rootScope.startInformation.valueOf();
                }
                if (params != "?" && $rootScope.startInformation != null) {
                    params += "&";
                }
                if ($rootScope.endInformation != null) {
                    params += "to=" + $rootScope.endInformation.valueOf();
                }

                $http.get('http://localhost:8080/opslots/list' + params).
                    success(function (data, status, headers, config) {
                        $scope.oplist = data;
                    }).
                    error(function (data, status, headers, config) {
                        alert("Error while getting Server data. \nPlease check connection.")
                    });
            };
        }])

    .controller('OpSlotFormController', ['$scope', '$http', function ($scope, $http) {

        $scope.newOpSlot = function () {

            var json = '{"type" : "' + $scope.data.opType + '", "slotStart" : ' + $scope.data.slotStart.getTime() + ', "slotEnd" : ' + $scope.data.slotEnd.getTime() + ' }';
            $http.put('http://localhost:8080/opslots/create', json)
                .success(function (data, status, headers, config) {
                })
                .error(function (data, status, headers, config) {
                    alert("Error while creating new Operation Slot. \nPlease check connection to server.")
                });
            $scope.getListFromServer();
            $scope.showfield = false;
        };

        /**
         * GetDate Formatted
         */
        $scope.formatDate = function (time, format) {
            return moment(time).format(format);
        };

    }])

    .controller('DatepickerCtrl', function ($rootScope,$scope,$log) {


        $scope.today = function () {
            $scope.dt = null;
        };
        $scope.today();
        
        $scope.clear = function () {
            $scope.dt = null;
        };

        $scope.toggleMin = function () {
            $scope.minDate = $scope.minDate ? null : new Date();
        };
        $scope.toggleMin();

        $scope.open = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.opened = true;
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };

        $scope.formats = ['dd.MM.yyyy'];
        $scope.format = $scope.formats[0];

        var tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);
        var afterTomorrow = new Date();
        afterTomorrow.setDate(tomorrow.getDate() + 2);
        $scope.events =
            [
                {
                    date: tomorrow,
                    status: 'full'
                },
                {
                    date: afterTomorrow,
                    status: 'partially'
                }
            ];

        $scope.getDayClass = function (date, mode) {
            if (mode === 'day') {
                var dayToCheck = new Date(date).setHours(0, 0, 0, 0);

                for (var i = 0; i < $scope.events.length; i++) {
                    var currentDay = new Date($scope.events[i].date).setHours(0, 0, 0, 0);

                    if (dayToCheck === currentDay) {
                        return $scope.events[i].status;
                    }
                }
            }

            return '';
        };

        $scope.changed = function(){
            if($scope.dt == null){
                $rootScope.startInformation = null;
                $rootScope.endInformation = null;
                $rootScope.getListFromServer();
                return;
            }
            $log.log('Date changed to: ' + moment($scope.dt.getTime()).format("DD.MM.YYYY"));

            if($rootScope.startInformation == null){
                $rootScope.startInformation = moment();
                $rootScope.startInformation.hour(0);
                $rootScope.startInformation.minute(0);

            }
            $rootScope.startInformation.date(moment($scope.dt.getTime()).date());
            $rootScope.startInformation.month(moment($scope.dt.getTime()).month());
            $rootScope.startInformation.year(moment($scope.dt.getTime()).year());

            if($rootScope.endInformation == null){
                $rootScope.endInformation = moment();
                $rootScope.endInformation.hour(23);
                $rootScope.endInformation.minute(59);
            }
            $rootScope.endInformation.date(moment($scope.dt.getTime()).date());
            $rootScope.endInformation.month(moment($scope.dt.getTime()).month());
            $rootScope.endInformation.year(moment($scope.dt.getTime()).year());
            $rootScope.getListFromServer();
        };

    })


    .controller('StartTimePickerCtrl', function ($rootScope,$scope,$log) {
            $scope.mytime = new Date();
            $scope.mytime.setHours(0);
            $scope.mytime.setMinutes(0);
            $scope.hstep = 1;
            $scope.mstep = 15;

            $scope.options = {
                hstep: [1, 2, 3],
                mstep: [1, 5, 10, 15, 25, 30]
            };

            $scope.ismeridian = false;
            $scope.toggleMode = function() {
                $scope.ismeridian = ! $scope.ismeridian;
            };

            $scope.update = function() {
                var d = new Date();
                d.setHours( 14 );
                d.setMinutes( 0 );
                $scope.mytime = d;
            };

            $scope.changed = function () {
                $log.log('Time changed to: ' + $scope.mytime);
                if($rootScope.startInformation == null){
                    $rootScope.startInformation = moment();
                }
                $rootScope.startInformation.hour($scope.mytime.getHours());
                $rootScope.startInformation.minute($scope.mytime.getMinutes());
                $rootScope.getListFromServer();
            };

            $scope.clear = function() {
                $scope.mytime = null;
        };


    })
    .controller('EndTimePickerCtrl', function ($rootScope,$scope,$log) {
        $scope.mytime = new Date();
        $scope.mytime.setHours(23);
        $scope.mytime.setMinutes(59);
        $scope.hstep = 1;
        $scope.mstep = 15;

        $scope.options = {
            hstep: [1, 2, 3],
            mstep: [1, 5, 10, 15, 25, 30]
        };

        $scope.ismeridian = false;
        $scope.toggleMode = function () {
            $scope.ismeridian = !$scope.ismeridian;
        };

        $scope.update = function () {
            var d = new Date();
            d.setHours(14);
            d.setMinutes(0);
            $scope.mytime = d;
        };

        $scope.changed = function () {
            $log.log('Time changed to: ' + $scope.mytime);
            if($rootScope.endInformation == null){
                $rootScope.endInformation = moment();
            }
            $rootScope.endInformation.hour($scope.mytime.getHours());
            $rootScope.endInformation.minute($scope.mytime.getMinutes());
            $rootScope.getListFromServer();
        };

        $scope.clear = function () {
            $scope.mytime = null;
        };
    });