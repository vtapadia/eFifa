'use strict';

var efifaAppServices = angular.module('efifaAppServices', []);

efifaAppServices.factory("UserService", ['$http',
    function($http, $q) {
        var promise;
        var myService = {
            getUser: function() {
                if ( !promise ) {
                    promise = $http.get("user/home").then(function(response) {
                        return response.data;
                    });
                }
                return promise;
            }
        };
        return myService;
    }
]);

efifaAppServices.factory("TeamService", ['$http','$q',
    function($http, $q) {
        var promise;
        var myService = {
            getTeams: function() {
                if ( !promise ) {
                    promise = $http.get("main/teams").then(function(response) {
                        return response.data;
                    });
                }
                return promise;
            }
        };
        return myService;
    }
]);

efifaAppServices.factory("TournamentService", ['$http','$q',
    function($http, $q) {
        var promise, endedPromise;
        var myService = {
            started: function() {
                if ( !promise ) {
                    promise = $http.get("main/started").then(function(response) {
                        return response.data.status;
                    });
                }
                return promise;
            },
            ended: function(fromCache) {
                if ( !endedPromise || (fromCache != undefined && !fromCache)) {
                    endedPromise = $http.get("main/ended").then(function(response) {
                        return response.data.status;
                    });
                }
                return endedPromise;
            }
        };
        return myService;
    }
]);
