(function() {
    'use strict';

    angular
        .module('webScrapperApp')
        .controller('RenamedHorsesDeleteController',RenamedHorsesDeleteController);

    RenamedHorsesDeleteController.$inject = ['$uibModalInstance', 'entity', 'RenamedHorses'];

    function RenamedHorsesDeleteController($uibModalInstance, entity, RenamedHorses) {
        var vm = this;

        vm.renamedHorses = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RenamedHorses.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
