'use strict';

angular.module('app', 
	[
		'ui.router',
		'ngStorage',
		'ngCookies',
		'ngAnimate', 
		'angularMoment', 
		'angular-preload-image', 
		'truncate',
		'ngResource', 
		'app.routes', 
		'app.core', 
		'app.services', 
		'app.config',
		'app.directives'
	]);

angular.module('app.core', ['ngCookies']);

angular.module('app.config', [])
angular.module('app.config').config(configs).run(runs);

function configs($httpProvider) {
    var interceptor = function($location, $log, $q) {
         console.log('interceptor');

         return {
              request: function(config) {
                  //console.log('interceptor request : '+config);
                  return config;
              },
              response: function(response) {
                  //console.log('interceptor response : '+response);
                  return response;
              },
              requestError: function(rejection) {
                 //console.log('interceptor requestError: '+rejection);
                 return $q.reject(rejection);
              },
              responseError: function(rejection) {
                //console.log('interceptor responseError: '+rejection);

                if (rejection.status === 401) {
                    $log.error('You are unauthorised to access the requested resource (401)');
                } else if (rejection.status === 404) {
                    $log.error('The requested resource could not be found (404)');
                } else if (rejection.status === 500) {
                    $log.error('Internal server error (500)');
                } else if (rejection.status <= 0){
                    //console.log(JSON.stringify(rejection, null, 4));
                    //console.log('interceptor responseError: '+rejection);
                    console.log(rejection);
                    //alert("no connection: "+rejection.config.url)
                }
                 return $q.reject(rejection);
              }
         }


    };
    $httpProvider.interceptors.push(interceptor);
}

function runs($rootScope, $localStorage, $state) {

    $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams){

        if(toState.name != "kafka_clusters" ){
            if($localStorage.cluster == undefined || $localStorage.api_url == undefined){
                event.preventDefault();
                $state.go('kafka_clusters', null, {location: 'replace'})
            }
        }

    });

    $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){

    });
}

angular.module('app.directives', []);
angular.module('app.directives').directive('activeLink', ['$location', function (location) {
    return {
      restrict: 'A',
      link: function(scope, element, attrs, controller) {
        var clazz = attrs.activeLink;
        var path = attrs.href;
        path = path.substring(1); //hack because path does not return including hashbang
        scope.location = location;
        scope.$watch('location.path()', function (newPath) {
          if (path === newPath) {
            element.addClass(clazz);
          } else {
            element.removeClass(clazz);
          }
        });
      }
    };
}]);

angular.module('app.routes', ['ui.router']).config(configuration);
function configuration ($stateProvider, $urlRouterProvider) {
  $urlRouterProvider.otherwise("/");
}
angular.module('app.services', []);

angular.module('app').filter("prettyJSON", () => json => JSON.stringify(json, null, " "));

angular.module('app').filter('timeFilter', function(){
  return function(d){
      if(d==0){return "0 second"}

      d = Number(d);
      var h = Math.floor(d / 3600);
      var m = Math.floor(d % 3600 / 60);
      var s = Math.floor(d % 3600 % 60);

      var hDisplay = h > 0 ? h + (h == 1 ? " hour, " : " hours, ") : "";
      var mDisplay = m > 0 ? m + (m == 1 ? " minute, " : " minutes, ") : "";
      var sDisplay = s > 0 ? s + (s == 1 ? " second" : " seconds") : "";
      return hDisplay + mDisplay + sDisplay;
  }
});

