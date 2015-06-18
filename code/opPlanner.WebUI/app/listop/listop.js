'use strict';

angular.module('myApp.listop', ['ngRoute'])

/**
 * Routing Controller
 */
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/listop', {
            templateUrl: 'listop/listop.html',
            controller: 'listopCtrl'
        });
    }])

/**
 * Root-Controller for OPSlots
 */
    .controller('listopCtrl', ['$rootScope', '$scope', '$http', '$location',
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

            // Inits the OP Slot list with getting all op Slots
            $http.get('http://' + $location.host() + ':8080/opslots/list').
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

                $http.delete('http://' + $location.host() + ':8080/opslots/' + id)
                    .success(function (data, status, headers, config) {
                        $scope.getListFromServer();
                    })
                    .error(function (data, status, headers, config) {
                        //alert("Error while trying to delete opSlot. \nPlease check server connection.");
                        $scope.getListFromServer();
                    });
            };

            /**
             * Deletes a reservation
             * @param id
             */
            $scope.removeReservation = function (id) {

                $http.delete('http://' + $location.host() + ':8080/opslots/reservation/' + id)
                    .success(function (data, status, headers, config) {
                        $scope.getListFromServer();
                    })
                    .error(function (data, status, headers, config) {
                        alert("Error while trying to delete reservation. \nPlease check server connection.");
                    });
            };


            /**
             * Controlling over type radio button
             */
            $scope.singleModel = 1;

            $scope.radioModel = undefined;

            $scope.checkModel = {
                All: undefined,
                Free: false
            };


            /**
             * Requests op slot list from server and adds from and to parameters to the requests
             * Afterwards the list will be refreshed
             */
            $rootScope.getListFromServer = function () {
                var params = "";
                if ($rootScope.startInformation != null || $rootScope.endInformation != null) {
                    if ($rootScope.startInformation == null) {
                        $rootScope.startInformation = moment().hour(0).minute(0);
                    }
                    if ($rootScope.endInformation == null) {
                        $rootScope.endInformation = moment().hour(23).minute(59);
                    }
                    params += "?from=" + $rootScope.startInformation.format("YYYY-MM-DD HH:mm");
                    params += "&to=" + $rootScope.endInformation.format("YYYY-MM-DD HH:mm");
                }
                $http.get('http://' + $location.host() + ':8080/opslots/list' + params).
                    success(function (data, status, headers, config) {
                        $scope.oplist = data;
                    }).
                    error(function (data, status, headers, config) {
                        alert("Error while getting Server data. \nPlease check connection.")
                    });

            };

            /**
             * GetDate Formatted
             */
            $scope.formatDate = function (time, format) {
                return moment(time).format(format);
            };
        }])


/**
 * Controller for DatePicker in Main-Page Filter
 */
    .controller('DatepickerCtrl', function ($rootScope, $scope, $log) {


        $scope.today = function () {
            $scope.dt = null;
        };
        $scope.today();

        /**
         * Clears the Field and resets the time so all entries are shown
         */
        $scope.clear = function () {
            $rootScope.startInformation = null;
            $rootScope.endInformation = null;
            $rootScope.getListFromServer();
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

        $scope.formats = ['dd.MM.'];
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

        /**
         * Events on changing date
         */
        $scope.changed = function () {
            if ($scope.dt == null) {
                $rootScope.startInformation = null;
                $rootScope.endInformation = null;
                $rootScope.getListFromServer();
                return;
            }
            $log.log('Date changed to: ' + moment($scope.dt.getTime()).format("DD.MM.YYYY"));

            if ($rootScope.startInformation == null) {
                $rootScope.startInformation = moment();
                $rootScope.startInformation.hour(0);
                $rootScope.startInformation.minute(0);

            }
            $rootScope.startInformation.date(moment($scope.dt.getTime()).date());
            $rootScope.startInformation.month(moment($scope.dt.getTime()).month());
            $rootScope.startInformation.year(moment($scope.dt.getTime()).year());

            if ($rootScope.endInformation == null) {
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

/**
 * Controller for choosing start time (filter)
 */
    .controller('StartTimePickerCtrl', function ($rootScope, $scope, $log) {
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
            if ($rootScope.startInformation == null) {
                $rootScope.startInformation = moment();
            }
            $rootScope.startInformation.hour($scope.mytime.getHours());
            $rootScope.startInformation.minute($scope.mytime.getMinutes());
            $rootScope.getListFromServer();
        };

        $scope.clear = function () {
            $scope.mytime = null;
        };


    })
/**
 *  Controller for choosing end time (filter)
 */
    .controller('EndTimePickerCtrl', function ($rootScope, $scope, $log) {
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
            if ($rootScope.endInformation == null) {
                $rootScope.endInformation = moment();
            }
            $rootScope.endInformation.hour($scope.mytime.getHours());
            $rootScope.endInformation.minute($scope.mytime.getMinutes());
            $rootScope.getListFromServer();
        };

        $scope.clear = function () {
            $scope.mytime = null;
        };
    })

/**
 * Controller for creating new OP Slot - MODAL
 */
    .controller('NewSlotCtrl', function ($scope, $modal, $log) {

        $scope.items = ['item1', 'item2', 'item3'];

        $scope.animationsEnabled = true;

        $scope.open = function (size) {

            var modalInstance = $modal.open({
                animation: $scope.animationsEnabled,
                templateUrl: 'modals/createModal.html',
                controller: 'ModalInstanceCtrl',
                size: size,
                resolve: {
                    items: function () {
                        return $scope.items;
                    }
                }
            });

            modalInstance.result.then(function (selectedItem) {
                $scope.selected = selectedItem;
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.toggleAnimation = function () {
            $scope.animationsEnabled = !$scope.animationsEnabled;
        };


    })

/**
 * Controller for the actual instance of the creation of the new OP Slot
 */
    .controller('ModalInstanceCtrl', function ($scope, $modalInstance, $http, $location, items) {

        $scope.items = items;
        $scope.selected = {
            item: $scope.items[0]
        };


        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.today = function () {
            $scope.dt = new Date();
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

        $scope.startingTime = new Date();
        $scope.startingTime.setHours(9);
        $scope.startingTime.setMinutes(0);
        $scope.endingTime = new Date();
        $scope.endingTime.setHours(9);
        $scope.endingTime.setMinutes(15);
        $scope.hstep = 1;
        $scope.mstep = 15;

        $scope.options = {
            hstep: [1, 2, 3],
            mstep: [1, 5, 10, 15, 25, 30]
        };

        $scope.ismeridian = false;

        $scope.update = function () {
            var d = new Date();
            d.setHours(14);
            d.setMinutes(0);
            $scope.startingTime = d;
        };

        /**
         * Creates a new OP Slot and sends request to server
         */
        $scope.newOpSlot = function () {

            var start = $scope.dt;
            start.setHours($scope.startingTime.getHours());
            start.setMinutes($scope.startingTime.getMinutes());

            var end = $scope.dt;
            end.setHours($scope.endingTime.getHours());
            end.setMinutes($scope.endingTime.getMinutes());


            var insertMessage = {
                "type": $scope.data.opType,
                "slotStart": moment(start).format("YYYY-MM-DDTHH:mm:ss.SSSZZ"),
                "slotEnd": moment(end).format("YYYY-MM-DDTHH:mm:ss.SSSZZ")
            };
            $http.put('http://' + $location.host() + ':8080/opslots/create', insertMessage)
                .success(function (data, status, headers, config) {
                    $scope.getListFromServer();
                })
                .error(function (data, status, headers, config) {
                    $scope.getListFromServer();
                });

        };

        $scope.ok = function () {
            $modalInstance.close($scope.selected.item);
            $scope.newOpSlot();

        };

    })


/**
 * Reservation MODAL
 */
    .controller('NewPatientReservationCtrl', function ($scope, $modal, $log) {

        $scope.items = ['item1', 'item2', 'item3'];

        $scope.animationsEnabled = true;

        $scope.open = function (size) {

            var modalInstance = $modal.open({
                animation: $scope.animationsEnabled,
                templateUrl: 'modals/createPatientReservation.html',
                controller: 'PatientModalInstanceCtrl',
                size: size,
                resolve: {
                    items: function () {
                        return $scope.items;
                    }
                }
            });

            modalInstance.result.then(function (selectedItem) {
                $scope.selected = selectedItem;
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.toggleAnimation = function () {
            $scope.animationsEnabled = !$scope.animationsEnabled;
        };


    })

/**
 * Controller for the actual instance of the creation of the new Reservation
 */
    .controller('PatientModalInstanceCtrl', function ($scope, $modalInstance, $http, items, $location) {

        $scope.items = items;
        $scope.selected = {
            item: $scope.items[0]
        };


        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.today = function () {
            $scope.dt = new Date();
            $scope.dt2 = new Date();
        };
        $scope.today();

        $scope.clear = function () {
            $scope.dt = null;
            $scope.dt2 = null;
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
        /**
         * Creates a new reservation and sends request to server
         */
        $scope.newReservation = function () {

            var start = $scope.dt;
            start.setHours(0, 0, 0);

            var end = $scope.dt2;
            start.setHours(23, 59, 59);

            var insertMessage = {
                "patientId": $scope.data.patientId,
                "preferredStart": moment(start).format("YYYY-MM-DD HH:mm"),
                "preferredEnd": moment(end).format("YYYY-MM-DD HH:mm"),
                "preferredPerimeter": parseInt($scope.data.preferredPerimeter),
                "opSlotType": $scope.data.opSlotType
            };
            $http.post('http://' + $location.host() + ':8080/opslots/reservation', insertMessage)
                .success(function (data, status, headers, config) {
                    $scope.getListFromServer();
                })
                .error(function (data, status, headers, config) {
                    alert("Error while creating new Operation Slot. \nPlease check connection to server.")
                });

        };

        $scope.ok = function () {
            $modalInstance.close($scope.selected.item);
            $scope.newReservation();
        };

    });