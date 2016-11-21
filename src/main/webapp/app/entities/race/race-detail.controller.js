(function() {
    'use strict';

    angular
        .module('webScrapperApp')
        .controller('RaceDetailController', RaceDetailController);

    RaceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Race', 'RaceInfo'];

    function RaceDetailController($scope, $rootScope, $stateParams, previousState, entity, Race, RaceInfo) {
        var vm = this;

        vm.race = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('webScrapperApp:raceUpdate', function(event, result) {
            vm.race = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
