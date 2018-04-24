'use strict';

angular.module('app.core').controller('menu_controller', ['$scope', '$state','$localStorage',  function($scope, $state, $localStorage ) {

    $scope.clusterId = $localStorage.cluster
    $scope.$watch(function() { return $localStorage.cluster; }, function(newValue) {
            $scope.clusterId = newValue
    });
}]);

angular.module('app.core').controller('kafka_clusters_controller', ['$scope', '$state', '$localStorage', 'kafka_clusters_service',
function($scope, $state, $localStorage, $kafka_clusters_service) {

    $kafka_clusters_service.query(function(data){
        $scope.clusters = data;
        console.log(data)
    }, function(errors){
        console.log(errors)
    })

    $scope.to = function(name, api_url) {

         $localStorage.cluster =  name;
         if(api_url.includes("https")){
            $localStorage.prefix = "https"
         }else{
            $localStorage.prefix = "http"
         }
         $localStorage.api_url = api_url.replace("https","").replace("http","").replace("://","");
         //$state.reload();
         $state.go('kafka_cluster', null, {location: 'replace'})
    };

}]);

angular.module('app.core').controller('kafka_cluster_controller', ['$scope', '$localStorage', 'kafka_cluster_service',function($scope, $localStorage, $kafka_cluster_service) {

	$kafka_cluster_service.get({prefix:$localStorage.prefix, api_url:$localStorage.api_url}, function(data){
        	$scope.res = data;
        	//console.log(data)
    })
}]);

angular.module('app.core').controller('kafka_cluster_brokers_controller', ['$scope', '$localStorage', '$resource' , 'kafka_cluster_brokers_service',function($scope, $localStorage, $resource, $kafka_cluster_brokers_service) {
   $scope.indexed_racks = [];
   $kafka_cluster_brokers_service.get({prefix:$localStorage.prefix, api_url:$localStorage.api_url}).$promise.then(function(res) {
       if (res.error == 0){
          $scope.brokers = res.data
          for (var i = 0; i < $scope.brokers.length; i++) {
              if ($scope.brokers[i].rack == undefined ){
                 $scope.brokers[i].rack = "default"
              }
              var IsNewRack = $scope.indexed_racks.indexOf($scope.brokers[i].rack) == -1;
              if(IsNewRack) {
                 $scope.indexed_racks.push($scope.brokers[i].rack);
              }
          }
          //console.log($scope.brokers)
          //console.log($scope.indexed_racks)
       }else{
         $scope.brokers = []
       }
       $scope.message = res.msg
   });

}]);

angular.module('app.core').controller('kafka_cluster_topics_controller',['$scope', '$localStorage', '$resource' , 'kafka_cluster_topics_service', function($scope, $localStorage, $resource, $kafka_cluster_topics_service) {
   var topics = $kafka_cluster_topics_service.get({prefix:$localStorage.prefix, api_url:$localStorage.api_url});
   topics.$promise.then(function(res) {
       $scope.res = res
   });

}]);

angular.module('app.core').controller('kafka_cluster_topics_offsets_controller',['$scope', '$localStorage', '$resource' , 'kafka_cluster_topics_offsets_service',
function($scope, $localStorage, $resource, $kafka_cluster_topics_offsets_service) {
   var topics = $kafka_cluster_topics_offsets_service.get({prefix:$localStorage.prefix, api_url:$localStorage.api_url});
   topics.$promise.then(function(res) {
       $scope.res = res
   });

   $scope.topic_name_filter = ""
   $scope.filterByTopic = function(topic_name) {
        $scope.topic_name_filter = topic_name;
   };
}]);

angular.module('app.core').controller('kafka_topic_controller',['$scope', '$localStorage', '$resource' , '$stateParams', 'kafka_topic_service', function($scope, $localStorage, $resource, $stateParams, $kafka_topic_service) {
   var t_id = $stateParams.topicId
   $kafka_topic_service.get({prefix:$localStorage.prefix, api_url:$localStorage.api_url, topic_id:t_id}).$promise.then(function(res) {
       //console.log(res)
       $scope.res = res
   });
}]);

angular.module('app.core').controller('kafka_groups_controller', ['$scope', '$localStorage', 'kafka_groups_service',function($scope, $localStorage, $kafka_groups_service) {
		$kafka_groups_service.get({prefix:$localStorage.prefix, api_url:$localStorage.api_url},function(data){
        	$scope.res = data;
        	//console.log(data)
        });

}]);

angular.module('app.core').controller('kafka_groups_acls_controller', ['$scope', '$localStorage', 'kafka_groups_acls_service',function($scope, $localStorage, $kafka_groups_acls_service) {
    $kafka_groups_acls_service.get({prefix:$localStorage.prefix, api_url:$localStorage.api_url},function(data){
        $scope.res = data;
        console.log(data)
    });

}]);

angular.module('app.core').controller('kafka_groups_lag_controller', ['$scope', '$localStorage', '$interval', 'kafka_groups_lag_service',function($scope, $localStorage, $interval, $kafka_groups_lag_service) {

        $scope.lag = 60;
        $scope.refresh_time = 30;
        $scope.on_refresh = false;

        $scope.group_name_filter = ""
        $scope.filterByGroup = function(group_name) {
            $scope.group_name_filter = group_name;
        };

        $scope.greaterThan = function(prop, val){
            return function(item){
              return item[prop] >= val;
            }
        }

        this.loadNotifications = function (){
            $scope.on_refresh = true;
            $kafka_groups_lag_service.get({prefix:$localStorage.prefix, api_url:$localStorage.api_url, lag:10}, function(data){
                $scope.res = data;
                //console.log(data)
                $scope.on_refresh = false;
            });
        };
        //Put in interval, first trigger after 10 seconds
        var theInterval = $interval(function(){
            this.loadNotifications();
        }.bind(this), $scope.refresh_time * 1000);

        $scope.$on('$destroy', function () {
            $interval.cancel(theInterval)
        });

        //invoke initialy
        this.loadNotifications();
}]);


angular.module('app.routes').config(auth_config);
function auth_config ($stateProvider, $urlRouterProvider) {
  $stateProvider
        .state('kafka_clusters', {
          	url: "/",
          	views: {
            "main": {
            	templateUrl: "/modules/kafka/kafka_clusters.tpl.html",
           	    controller: 'kafka_clusters_controller'
            }
          }
  });
  $stateProvider
      .state('kafka_cluster', {
        	url: "/cluster",
        	views: {
          "main": {
          	templateUrl: "/modules/kafka/kafka_cluster.tpl.html",
         	controller: 'kafka_cluster_controller'
          }
        }
  });

    $stateProvider
        .state('kafka_cluster_brokers', {
          	url: "/brokers",
          	views: {
            "main": {
            	templateUrl: "/modules/kafka/kafka_cluster_brokers.tpl.html",
           	    controller: 'kafka_cluster_brokers_controller'
            }
          }

    });

     $stateProvider.state('kafka_cluster_topics', {
              	url: "/topics",
              	views: {
                "main": {
                	templateUrl: "/modules/kafka/kafka_cluster_topics.tpl.html",
               	    controller: 'kafka_cluster_topics_controller'
                }
              }
     });
     $stateProvider.state('kafka_cluster_topics_offsets', {
              	url: "/topics/offsets",
              	views: {
                "main": {
                	templateUrl: "/modules/kafka/kafka_cluster_topics_offsets.tpl.html",
               	    controller: 'kafka_cluster_topics_offsets_controller'
                }
              }
     });


     $stateProvider.state('kafka_topic', {
        url: "/topic/:topicId",
        views: {
        "main": {
             templateUrl: "/modules/kafka/kafka_topic.tpl.html",
             controller: 'kafka_topic_controller'
           }
        }

     });

     $stateProvider.state('kafka_groups', {
         url: "/groups",
         views: {
         "main": {
             templateUrl: "/modules/kafka/kafka_cluster_groups.tpl.html",
             controller: 'kafka_groups_controller'
             }
         }
     });
     $stateProvider.state('kafka_groups_acls', {
         url: "/groups/acls",
         views: {
         "main": {
             templateUrl: "/modules/kafka/kafka_cluster_groups_acls.tpl.html",
             controller: 'kafka_groups_acls_controller'
             }
         }
     });

     $stateProvider.state('kafka_groups_lag', {
         url: "/groups/lag",
         views: {
         "main": {
             templateUrl: "/modules/kafka/kafka_cluster_groups_lag.tpl.html",
             controller: 'kafka_groups_lag_controller'
             }
         }
     });
}

angular.module('app.services').factory('kafka_clusters_service', ['$resource', function($resource) {
   return $resource('/clusters.json');
}]);

angular.module('app.services').service('kafka_cluster_service', ['$resource', function($resource) {
    var res = $resource(':prefix://:api_url/api/cluster/status');
    return res;
}]);

angular.module('app.services').service('kafka_cluster_brokers_service', ['$resource', '$localStorage', function($resource, $localStorage) {
    return $resource(':prefix://:api_url/api/brokers');
}]);

angular.module('app.services').factory('kafka_cluster_topics_service', ['$resource', '$localStorage', function($resource, $localStorage) {
    return $resource(':prefix://:api_url/api/topics');
}]);

angular.module('app.services').factory('kafka_cluster_topics_offsets_service', ['$resource', '$localStorage', function($resource, $localStorage) {
    return $resource(':prefix://:api_url/api/topics/offsets');
}]);

angular.module('app.services').factory('kafka_topic_service', ['$resource', '$localStorage', function($resource, $localStorage) {
    return $resource(':prefix://:api_url/api/topic/:topic_id/ ', {topic_id:'@id'});
}]);

angular.module('app.services').factory('kafka_groups_service', ['$resource', '$localStorage', function($resource, $localStorage) {
    return $resource(':prefix://:api_url/api/groups');
}]);

angular.module('app.services').factory('kafka_groups_acls_service', ['$resource', '$localStorage', function($resource, $localStorage) {
    return $resource(':prefix://:api_url/api/groups/acls');
}]);

angular.module('app.services').factory('kafka_groups_lag_service', ['$resource', '$localStorage', function($resource, $localStorage) {
    return $resource(':prefix://:api_url/api/groups/lag/:lag', {lag:'@id'});
}]);