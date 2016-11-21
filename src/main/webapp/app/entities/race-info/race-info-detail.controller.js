(function() {
    'use strict';

    angular
        .module('webScrapperApp')
        .controller('RaceInfoDetailController', RaceInfoDetailController);

    RaceInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RaceInfo'];

    function RaceInfoDetailController($scope, $rootScope, $stateParams, previousState, entity, RaceInfo) {
        var vm = this;

        vm.raceInfo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('webScrapperApp:raceInfoUpdate', function(event, result) {
            vm.raceInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
