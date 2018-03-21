'use strict';

angular.module('app', 
	[
		'ui.router',
		'ngStorage', 
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

angular.module('app.core', []);

angular
    .module('app.config', [])
    .config(configs)
    .run(runs);

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
                    alert("no connection: "+rejection.config.url)
                }
                 return $q.reject(rejection);
              }
         }


    };
    $httpProvider.interceptors.push(interceptor);
}

function runs($rootScope) {
    $rootScope.$on('$routeChangeStart', function() {

    });
    $rootScope.$on('$routeChangeSuccess', function() {
        
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


angular.module('app').constant('api_url', 'http://localhost:9090');

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

/*angular.module('app').filter('groupBy', function(){
    return function(list, group_by) {

	var filtered = [];
	var prev_item = null;
	var group_changed = false;
	// this is a new field which is added to each item where we append "_CHANGED"
	// to indicate a field change in the list
	var new_field = group_by + '_CHANGED';

	// loop through each item in the list
	angular.forEach(list, function(item) {

	    group_changed = false;

		// if not the first item
		if (prev_item !== null) {

			// check if the group by field changed
			if (prev_item[group_by] !== item[group_by]) {
				group_changed = true;
			}

			// otherwise we have the first item in the list which is new
		} else {
			group_changed = true;
		}

		// if the group changed, then add a new field to the item
		// to indicate this
		if (group_changed) {
			item[new_field] = true;
		} else {
			item[new_field] = false;
		}

		filtered.push(item);
		prev_item = item;

	});

	return filtered;
	};
})*/
