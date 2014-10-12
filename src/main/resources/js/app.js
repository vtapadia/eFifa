'use strict';

/* Controllers */

var efifaApp = angular.module('efifaApp', [
    'ui.bootstrap',
    'efifaAppCtrls',
    'efifaAppServices',
    'ngRoute',
    'angularCharts'
]);

efifaApp.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/user/home', {
                templateUrl: 'partials/user-home.html',
                controller: 'efifaUserCtrl'
            }).
            when('/predictions', {
                templateUrl: 'partials/predictions-list.html',
                controller: 'efifaPredictionsCtrl'
            }).
            when('/admin/home', {
                templateUrl: 'partials/admin-home.html',
                controller: 'efifaAdminCtrl'
            }).
            when('/leaders', {
                templateUrl: 'partials/leaders-home.html',
                controller: 'efifaLeaderCtrl'
            }).
            when('/rules', {
                templateUrl: 'partials/rules-home.html',
                controller: 'efifaRulesCtrl'
            }).
            when('/stats', {
                templateUrl: 'partials/stats-home.html',
                controller: 'efifaStatsCtrl'
            }).
            when('/teams', {
                templateUrl: 'partials/teams-home.html',
                controller: 'efifaTeamsCtrl'
            }).
            when('/league', {
                templateUrl: 'partials/league-home.html',
                controller: 'efifaLeagueCtrl'
            }).
            otherwise({
                redirectTo: '/user/home'
            });
    }]);

