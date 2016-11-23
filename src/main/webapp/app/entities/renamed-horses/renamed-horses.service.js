(function() {
    'use strict';
    angular
        .module('webScrapperApp')
        .factory('RenamedHorses', RenamedHorses);

    RenamedHorses.$inject = ['$resource', 'DateUtils'];

    function RenamedHorses ($resource, DateUtils) {
        var resourceUrl =  'api/renamed-horses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateChanged = DateUtils.convertLocalDateFromServer(data.dateChanged);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateChanged = DateUtils.convertLocalDateToServer(copy.dateChanged);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateChanged = DateUtils.convertLocalDateToServer(copy.dateChanged);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
