(function() {
    'use strict';

    angular
        .module('webScrapperApp')
        .controller('RaceInfoDeleteController',RaceInfoDeleteController);

    RaceInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'RaceInfo'];

    function RaceInfoDeleteController($uibModalInstance, entity, RaceInfo) {
        var vm = this;

        vm.raceInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RaceInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
