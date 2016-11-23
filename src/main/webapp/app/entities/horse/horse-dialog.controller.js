(function() {
    'use strict';

    angular
        .module('webScrapperApp')
        .controller('HorseDialogController', HorseDialogController);

    HorseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Horse', 'Race', 'RaceInfo'];

    function HorseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Horse, Race, RaceInfo) {
        var vm = this;

        vm.horse = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.races = Race.query();
        vm.raceinfos = RaceInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.horse.id !== null) {
                Horse.update(vm.horse, onSaveSuccess, onSaveError);
            } else {
                Horse.save(vm.horse, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('webScrapperApp:horseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.birthDate = false;
        vm.datePickerOpenStatus.lastGearChange = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
