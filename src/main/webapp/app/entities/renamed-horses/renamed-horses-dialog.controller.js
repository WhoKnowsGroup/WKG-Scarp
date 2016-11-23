(function() {
    'use strict';

    angular
        .module('webScrapperApp')
        .controller('RenamedHorsesDialogController', RenamedHorsesDialogController);

    RenamedHorsesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RenamedHorses'];

    function RenamedHorsesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RenamedHorses) {
        var vm = this;

        vm.renamedHorses = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.renamedHorses.id !== null) {
                RenamedHorses.update(vm.renamedHorses, onSaveSuccess, onSaveError);
            } else {
                RenamedHorses.save(vm.renamedHorses, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('webScrapperApp:renamedHorsesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateChanged = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
