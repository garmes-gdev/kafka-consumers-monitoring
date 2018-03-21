'use strict';
angular.module('app.core').controller('kafka_cluster_controller', ['$scope', '$rootScope','kafka_cluster_service',function($scope, $rootScope, $kafka_cluster_service) {
		$kafka_cluster_service.get(function(data){
        	$scope.res = data;
        	//console.log(data)
        	$rootScope.cluster_id = $scope.res.data.clusterId.id
        	$scope.clusterId = $rootScope.cluster_id
        })
}]);

angular.module('app.core').controller('kafka_cluster_brokers_controller', ['$scope', '$rootScope', '$resource' , 'kafka_cluster_brokers_service',function($scope, $rootScope, $resource, $kafka_cluster_brokers_service) {
   $scope.indexed_racks = [];
   $kafka_cluster_brokers_service.get().$promise.then(function(res) {
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
   $scope.clusterId = $rootScope.cluster_id
}]);

angular.module('app.core').controller('kafka_cluster_topics_controller',['$scope', '$rootScope', '$resource' , 'kafka_cluster_topics_service', function($scope, $rootScope, $resource, $kafka_cluster_topics_service) {
   var topics = $kafka_cluster_topics_service.get();
   topics.$promise.then(function(res) {
       $scope.res = res
   });
   $scope.clusterId = $rootScope.cluster_id
}]);

angular.module('app.core').controller('kafka_cluster_topics_offsets_controller',['$scope', '$rootScope', '$resource' , 'kafka_cluster_topics_offsets_service',
function($scope, $rootScope, $resource, $kafka_cluster_topics_offsets_service) {
   var topics = $kafka_cluster_topics_offsets_service.get();
   topics.$promise.then(function(res) {
       $scope.res = res
   });
   $scope.clusterId = $rootScope.cluster_id

   $scope.topic_name_filter = ""
   $scope.filterByTopic = function(topic_name) {
        $scope.topic_name_filter = topic_name;
   };
}]);

angular.module('app.core').controller('kafka_topic_controller',['$scope', '$rootScope', '$resource' , '$stateParams', 'kafka_topic_service', function($scope, $rootScope, $resource, $stateParams, $kafka_topic_service) {
   var t_id = $stateParams.topicId
   $kafka_topic_service.get({topic_id:t_id}).$promise.then(function(res) {
       //console.log(res)
       $scope.res = res
   });
   $scope.clusterId = $rootScope.cluster_id
}]);

angular.module('app.core').controller('kafka_groups_controller', ['$scope', '$rootScope', 'kafka_groups_service',function($scope, $rootScope, $kafka_cluster_service) {
		$kafka_cluster_service.get(function(data){
        	$scope.res = data;
        	//console.log(data)
        });
        $scope.clusterId = $rootScope.cluster_id
}]);

angular.module('app.core').controller('kafka_groups_acls_controller', ['$scope', '$rootScope', 'kafka_groups_acls_service',function($scope, $rootScope, $kafka_groups_acls_service) {
		$kafka_groups_acls_service.get(function(data){
        	$scope.res = data;
        	console.log(data)
        });
        $scope.clusterId = $rootScope.cluster_id
}]);

angular.module('app.core').controller('kafka_groups_lag_controller', ['$scope', '$rootScope', 'kafka_groups_lag_service',function($scope, $rootScope, $kafka_groups_lag_service) {

        $scope.lag = 60
		$kafka_groups_lag_service.get({lag:10}, function(data){
        	$scope.res = data;
        	//console.log(data)
        });
        $scope.group_name_filter = ""
        $scope.filterByGroup = function(group_name) {
            $scope.group_name_filter = group_name;
        };

        $scope.greaterThan = function(prop, val){
            return function(item){
              return item[prop] >= val;
            }
        }
        $scope.clusterId = $rootScope.cluster_id
}]);




angular.module('app.routes').config(auth_config);
function auth_config ($stateProvider, $urlRouterProvider) {
  $stateProvider
      .state('kafka_cluster', {
        	url: "/",
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

angular.module('app.services').factory('kafka_cluster_service', ['$resource','api_url', function($resource, api_url) {
  	return $resource(api_url+'/api/cluster/status');
}]);

angular.module('app.services').factory('kafka_cluster_brokers_service', ['$resource', 'api_url', function($resource, api_url) {
   return $resource(api_url+'/api/brokers');
}]);

angular.module('app.services').factory('kafka_cluster_topics_service', ['$resource', 'api_url', function($resource, api_url) {
   return $resource(api_url+'/api/topics');
}]);

angular.module('app.services').factory('kafka_cluster_topics_offsets_service', ['$resource', 'api_url', function($resource, api_url) {
   return $resource(api_url+'/api/topics/offsets');
}]);

angular.module('app.services').factory('kafka_topic_service', ['$resource', 'api_url', function($resource, api_url) {
   return $resource(api_url+'/api/topic/:topic_id/ ', {topic_id:'@id'});
}]);

angular.module('app.services').factory('kafka_groups_service', ['$resource', 'api_url', function($resource, api_url) {
   return $resource(api_url+'/api/groups');
}]);

angular.module('app.services').factory('kafka_groups_acls_service', ['$resource', 'api_url', function($resource, api_url) {
   return $resource(api_url+'/api/groups/acls');
}]);

angular.module('app.services').factory('kafka_groups_lag_service', ['$resource', 'api_url', function($resource, api_url) {
   return $resource(api_url+'/api/groups/lag/:lag', {lag:'@id'});
}]);