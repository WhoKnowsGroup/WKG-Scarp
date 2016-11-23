(function() {
    'use strict';

    angular
        .module('webScrapperApp')
        .controller('RenamedHorsesController', RenamedHorsesController);

    RenamedHorsesController.$inject = ['$scope', '$state', 'RenamedHorses'];

    function RenamedHorsesController ($scope, $state, RenamedHorses) {
        var vm = this;
        
        vm.renamedHorses = [];

        loadAll();

        function loadAll() {
            RenamedHorses.query(function(result) {
                vm.renamedHorses = result;
            });
        }
    }
})();
