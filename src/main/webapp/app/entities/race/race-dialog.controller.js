(function() {
    'use strict';

    angular
        .module('webScrapperApp')
        .controller('RaceDialogController', RaceDialogController);

    RaceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Race', 'RaceInfo'];

    function RaceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Race, RaceInfo) {
        var vm = this;

        vm.race = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.raceinfos = RaceInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.race.id !== null) {
                Race.update(vm.race, onSaveSuccess, onSaveError);
            } else {
                Race.save(vm.race, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('webScrapperApp:raceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.raceDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
