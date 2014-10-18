'use strict';

var efifaAppCtrls = angular.module('efifaAppCtrls', []);

/* Controllers */
efifaAppCtrls.controller('MainCtrl',function($scope, $location, $http) {
    $scope.tabs = [
        {
            'name': 'Home',
            'url': '/user/home'
        },
        {
            'name': 'My Predictions',
            'url': '/predictions'
        },
        {
            'name': 'Leaders Board',
            'url': '/leaders'
        },
        {
            'name': 'Team Standings',
            'url': '/teams'
        },
        {
            'name': 'Prediction Meter',
            'url': '/stats'
        },
        {
            'name': 'Rules',
            'url': '/rules'
        }
    ];
    $http.get("main/admin").success(function(data) {
        $scope.admin = data;
        if ($scope.admin.status) {
            $scope.tabs.splice(5,0,{
                'name': 'Admin',
                'url': '/admin/home'
            });
        }
    })
    $http.get("main/leagueowner").success(function(data) {
        $scope.admin = data;
        if ($scope.admin.status) {
            $scope.tabs.splice(5,0,{
                'name': 'League Maintenance',
                'url': '/league'
            });
        }
    })
    $scope.activeTab=1;
    $location.path('/user/home');

    $scope.setActive = function(index) {
        $scope.activeTab=index;
        $location.path($scope.tabs[index].url);
    }

});

efifaAppCtrls.controller('efifaRulesCtrl', function($scope) {
});

efifaAppCtrls.controller('efifaUserCtrl', function($scope, $http, UserService, TeamService, TournamentService) {

    $scope.slides =[
        //    {image:'img/groups.jpg', text:''},
        //{image:'img/brazil-fan2.jpg', text:''},
        //{image:'img/sf1-i1.jpg', text:''},
        //{image:'img/sf1-i2.jpg', text:''},
        //{image:'img/sf1-i3.jpg', text:''}
    ];

    TournamentService.started().then(function(data) {
        $scope.started = data;
    });

    TournamentService.ended(true).then(function(data) {
        $scope.ended = data;
    });

    $scope.finalizedGlobal = false;

    var linkTeams = function() {
        if (angular.isDefined($scope.user) && angular.isDefined($scope.teams)) {
            if (angular.isDefined($scope.user.teamWinner)) {
                for (var i=0;i<$scope.teams.length;i++) {
                    if (angular.equals($scope.teams[i], $scope.user.teamWinner)) {
                        $scope.user.teamWinner=$scope.teams[i];
                    }
                }
            }
            if (angular.isDefined($scope.user.teamRunner)) {
                for (var i=0;i<$scope.teams.length;i++) {
                    if (angular.equals($scope.teams[i], $scope.user.teamRunner)) {
                        $scope.user.teamRunner=$scope.teams[i];
                    }
                }
            }
        }
    };

    TeamService.getTeams().then(function(data) {
        $scope.teams = data;
        linkTeams();
    });
    UserService.getUser().then( function(data) {
        $scope.user = data;
        linkTeams();
    });

    $scope.newpassword="";

    $scope.alerts = [];
    $scope.alerts.push({type:'success',msg:'Lets play Hero ISL and promote Indian football, !! BEST OF LUCK !!.'});

    $http.get("leader/leaders?filterSilent=true").success(function(data) {
        $scope.leaders = data;
    });

    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };
    var clearAlert = function() {
        $scope.alerts = [];
    };

    $scope.changepassword = function(pass) {
        clearAlert();
        $http.post("user/changepassword",pass).success(function(data) {
            $scope.alerts.push({type:'success',msg:data.message});
            $scope.newpassword="";
        });
    };

    $scope.submitGlobalPrediction = function() {
        clearAlert();
        $http.post("user/update",$scope.user).success(function(data) {
            $scope.alerts.push({type:'success',msg:data.message});
        });
    };

});

efifaAppCtrls.controller('efifaPredictionsCtrl', function($scope, $http, TournamentService, UserService) {
    $scope.loading = true;

    UserService.getUser().then( function(data) {
        $scope.user = data;
        if (data.subscription == 'FULL') {
            $http.get("predictions/forupdate").success(function(data) {
                $scope.predictions = data;
                $scope.predictionsCopy = angular.copy(data);
                $scope.loading = false;
            });
            $http.get("predictions/forview").success(function(data) {
                $scope.viewPredictions = data;
                if (angular.isArray($scope.viewPredictions) && $scope.viewPredictions.length > 0) {
                    $scope.viewPredictionsAvailable=true;
                } else {
                    $scope.viewPredictionsAvailable=false;
                }
            });
        }
    });

    TournamentService.started().then(function (data) {

    });

    $scope.submitVisible = function(data) {
        var ret = false;
        for (var i=0;i<$scope.predictionsCopy.length;i++) {
            if($scope.predictionsCopy[i].id == data.id) {
                ret = $scope.predictionsCopy[i].teamAPrediction != data.teamAPrediction ||
                    $scope.predictionsCopy[i].teamBPrediction != data.teamBPrediction;
            }
        }
        return ret;
    };
    $scope.submitAllVisible = function() {
        var ret = false;
        for (var i=0; $scope.predictions != undefined && i<$scope.predictions.length; i++) {
            ret = $scope.submitVisible($scope.predictions[i]);
            if (ret == true) {
                break;
            }
        }
        return ret;
    }

    $scope.submitEnabled = true;
    $scope.submit = function(data) {
        $scope.submitEnabled = false;
        $http.post("predictions/update", data).success(function(response) {
            if (response.status==true) {
                for (var i=0;i<$scope.predictionsCopy.length;i++) {
                    if ($scope.predictionsCopy[i].id == data.id) {
                        $scope.predictionsCopy[i] = angular.copy(data);
                        break;
                    }
                }
                clearAlert();
                $scope.alerts.push({type:'success',msg:response.message});
            } else {
                clearAlert();
                $scope.alerts.push({type:'danger',msg:response.message});
            }
            $scope.submitEnabled = true;
        });
    };

    $scope.submitAll = function() {
        $scope.submitEnabled = false;
        $http.post("predictions/update/all", $scope.predictions).success(function(response) {
            clearAlert();
            if (response.status==true) {
                $scope.predictionsCopy = angular.copy($scope.predictions);
                $scope.alerts.push({type:'success',msg:response.message});
            } else {
                $scope.alerts.push({type:'danger',msg:response.message});
            }
            $scope.submitEnabled = true;
        });
    };

    var d= new Date();
    d.setHours(0,0,0,0); /* Removes today time */
    $scope.date = d.getTime();

    $scope.alerts = [];
    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };
    var clearAlert = function() {
        $scope.alerts = [];
    };
});

efifaAppCtrls.controller('efifaLeaderCtrl', function($scope, $http, $filter, TournamentService) {
    TournamentService.started().then(function(data) {
        $scope.started = data;
    });

    $http.get("leader/leaders").success(function(data) {
        $scope.leaders = data;
        $scope.userCount = $scope.leaders.length;
        $scope.totalAmount = $scope.leaders.length*100;
        $scope.totalWeight=0;
        for (var i=0;i<$scope.leaders.length;i++) {
            $scope.totalWeight = $scope.totalWeight + getWeight($scope.leaders[i]);
        }
    });

    $scope.hasProgress = false;

    $http.get("leader/progress").success(function(data) {
        $scope.progress=data;
        $scope.config = {
            title: 'Progress',
            tooltips: true,
            labels: false,
            mouseover: function() {},
            mouseout: function() {},
            click: function() {},
            lineLegend: 'traditional',
            legend: {
                display: true,
                //could be 'left, right'
                position: 'right'
            }
        };
        $scope.progressData = {
            series: [],
            data: []
        };
        for (var i=0;i<$scope.progress.matches.length;i++) {
            $scope.progressData.data.push({'x':$scope.progress.matches[i].shortName, 'y':[]});
        }
        for (var i=0;i<$scope.progress.userProgress.length;i++) {
            $scope.progressData.series.push($scope.progress.userProgress[i].userName);
            for (var j=0;j<$scope.progress.userProgress[i].progression.length;j++) {
                $scope.progressData.data[j].y.push($scope.progress.userProgress[i].progression[j]);
            }
        }
        $scope.hasProgress = true;
    });

    var getWeight = function(user) {
        switch (user.rank) {
            case 1: return 5; break;
            case 2: return 3; break;
            case 3: return 2; break;
            default: return 0; break;
        }
    }

    $scope.getPrizeAmount = function(user) {
        return $filter('number')((($scope.totalAmount/$scope.totalWeight) * getWeight(user)),0);
    };
});

efifaAppCtrls.controller('efifaLeagueCtrl', function($scope, $http) {
    $scope.validateUser = function() {
        $http.post("league/validateuser", $scope.user).success(function(data) {
            $scope.validatedUserData = data;
            //alert(data.message);
        });
    }

});

efifaAppCtrls.controller('efifaAdminCtrl', function($scope, $http, TournamentService) {
    $scope.alerts = [];

    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };
    var clearAlert = function() {
        $scope.alerts = [];
    };

    $scope.league = {};

    $scope.registerLeague = function() {
        $http.post("league/register", $scope.league).success(function(data) {
            $scope.registerUserData = data;
            $scope.alerts.push({type:'success',msg:data.message});
        });
    };

    TournamentService.name().then(function(data) {
        $scope.league.tournamentName = data;
    });

    $scope.subscriptions=['BASIC','FULL'];
    $scope.user = {subscription: 'FULL'};

    $scope.registerUser = function() {
        $http.post("admin/register", $scope.user).success(function(data) {
            $scope.registerUserData = data;
            alert(data.message);
        });
    };
    $http.get("match/frozen").success(function(data) {
        $scope.frozenMatches = data;
    });
    $scope.finalizeMatch = function(data, index) {
        $http.post("admin/finalize/match", data).success(function(dataReturned) {
            $scope.frozenMatches[index].finalized = dataReturned.finalized;
            alert("match finalized");
        });
    };
    $scope.userid="";
    $scope.setDefaultPassword = function() {
        $http.post("admin/set/password",$scope.userid).success(function(data) {
            alert(data.message);
        });
    };
    $scope.setDefaultPredictions = function() {
        $http.post("admin/set/prediction",$scope.userid).success(function(data) {
            alert(data.message);
        });
    }
    $scope.setFuturePredictions = function() {
        $http.post("admin/set/futureprediction").success(function(data) {
            alert(data.message);
        });
    }
    $scope.finalizeTournament = function() {
        $http.post("admin/finalize/tournament").success(function(data) {
            alert(data.message);
        });
    }
});

efifaAppCtrls.controller('efifaTeamsCtrl', function($scope, $http, UserService, TeamService, TournamentService) {
    //$scope.myOptions = [{name:'League Stage'}, {name:'Group Stage'}, {name:'Second Stage'}];
    $scope.myOptions = [{name:'League Stage'}];
    $scope.mySelectedOption = $scope.myOptions[0];

    $http.get("main/teams?time=" + (new Date()).getTime()).then(function(response) {
        $scope.teams = response.data;
    });
    $http.get("match/secondstage?time=" + (new Date()).getTime()).success(function(data) {
        $scope.secondstageMatches = data;
    });
});

efifaAppCtrls.controller('efifaStatsCtrl', function($scope, $http) {

    $scope.myOptions = [{name:'Future Matches Predictions'}, {name:'Global Predictions'}];
    $scope.mySelectedOption = $scope.myOptions[0];

    $scope.config = [];
    $scope.data = [];

    $http.get("predictions/stats").success(function(data) {
        $scope.statsData = data;
        for (var i=0;i<$scope.statsData.length;i++) {
            $scope.config[i] = {
                title: $scope.statsData[i].teamAName + ' vs ' + $scope.statsData[i].teamBName,
                tooltips: true,
                labels: false,
                mouseover: function() {},
                mouseout: function() {},
                click: function() {},
                legend: {
                    display: true,
                    //could be 'left, right'
                    position: 'left'
                }/*,
                colors: [$scope.statsData[i].teamAColor,
                        'grey',
                        $scope.statsData[i].teamBColor]*/
            };
            $scope.data[i] = {
                series: [$scope.statsData[i].teamAName, 'Draw', $scope.statsData[i].teamBName],
                data: [{
                    x: $scope.statsData[i].teamAName,
                    y: [$scope.statsData[i].teamAWin]
                }, {
                    x: "Draw",
                    y: [$scope.statsData[i].draw]
                }, {
                    x: $scope.statsData[i].teamBName,
                    y: [$scope.statsData[i].teamBWin]
                }]
            };
        }
    });

    $scope.globalReady = false;
    $scope.globalTeamConfig = {
        title: 'Global Team Predictions',
        tooltips: true,
        labels: false,
        mouseover: function() {},
        mouseout: function() {},
        click: function() {},
        legend: {
            display: false,
            //could be 'left, right'
            position: 'left'
        }
    };
    $scope.globalGoalsConfig = {
        title: 'Global Goals Prediction',
        tooltips: true,
        labels: false,
        mouseover: function() {},
        mouseout: function() {},
        click: function() {},
        legend: {
            display: false,
            //could be 'left, right'
            position: 'left'
        }
    };
    $scope.globalTeamData = {
        series: [],
        data: []
    };
    $scope.globalGoalsData = {
        series: [],
        data: []
    };
    $http.get("user/globalpredictions").success(function(data) {
        $scope.global = data;
        for (var g in $scope.global.goals) {
            $scope.globalGoalsData.series.push(g);
            $scope.globalGoalsData.data.push({x:g,y:[$scope.global.goals[g]]});
        }
        for (var t in $scope.global.teams) {
            $scope.globalTeamData.series.push(t);
            $scope.globalTeamData.data.push({x:t,y:[$scope.global.teams[t]]});
        }
        $scope.globalReady = true;
    });
});

