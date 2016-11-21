(function() {
    'use strict';

    angular
        .module('webScrapperApp')
        .controller('RaceController', RaceController);

    RaceController.$inject = ['$scope', '$state', 'Race'];

    function RaceController ($scope, $state, Race) {
        var vm = this;
        
        vm.races = [];

        loadAll();

        function loadAll() {
            Race.query(function(result) {
                vm.races = result;
            });
        }
    }
})();
