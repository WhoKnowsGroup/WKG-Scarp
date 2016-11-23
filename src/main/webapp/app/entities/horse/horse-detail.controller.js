(function() {
    'use strict';

    angular
        .module('webScrapperApp')
        .controller('HorseDetailController', HorseDetailController);

    HorseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Horse', 'Race', 'RaceInfo'];

    function HorseDetailController($scope, $rootScope, $stateParams, previousState, entity, Horse, Race, RaceInfo) {
        var vm = this;

        vm.horse = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('webScrapperApp:horseUpdate', function(event, result) {
            vm.horse = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
