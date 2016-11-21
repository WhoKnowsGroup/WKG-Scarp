(function() {
    'use strict';

    angular
        .module('webScrapperApp')
        .controller('RaceInfoDialogController', RaceInfoDialogController);

    RaceInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RaceInfo'];

    function RaceInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RaceInfo) {
        var vm = this;

        vm.raceInfo = entity;
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
            if (vm.raceInfo.id !== null) {
                RaceInfo.update(vm.raceInfo, onSaveSuccess, onSaveError);
            } else {
                RaceInfo.save(vm.raceInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('webScrapperApp:raceInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
