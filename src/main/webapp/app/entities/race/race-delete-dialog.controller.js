(function() {
    'use strict';

    angular
        .module('webScrapperApp')
        .controller('RaceDeleteController',RaceDeleteController);

    RaceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Race'];

    function RaceDeleteController($uibModalInstance, entity, Race) {
        var vm = this;

        vm.race = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Race.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
