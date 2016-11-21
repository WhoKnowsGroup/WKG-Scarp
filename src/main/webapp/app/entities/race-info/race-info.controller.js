(function() {
    'use strict';

    angular
        .module('webScrapperApp')
        .controller('RaceInfoController', RaceInfoController);

    RaceInfoController.$inject = ['$scope', '$state', 'RaceInfo'];

    function RaceInfoController ($scope, $state, RaceInfo) {
        var vm = this;
        
        vm.raceInfos = [];

        loadAll();

        function loadAll() {
            RaceInfo.query(function(result) {
                vm.raceInfos = result;
            });
        }
    }
})();
