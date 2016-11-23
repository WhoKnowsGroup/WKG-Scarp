(function() {
    'use strict';

    angular
        .module('webScrapperApp')
        .controller('RenamedHorsesDetailController', RenamedHorsesDetailController);

    RenamedHorsesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RenamedHorses'];

    function RenamedHorsesDetailController($scope, $rootScope, $stateParams, previousState, entity, RenamedHorses) {
        var vm = this;

        vm.renamedHorses = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('webScrapperApp:renamedHorsesUpdate', function(event, result) {
            vm.renamedHorses = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
