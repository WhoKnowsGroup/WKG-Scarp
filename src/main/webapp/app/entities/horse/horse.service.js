(function() {
    'use strict';
    angular
        .module('webScrapperApp')
        .factory('Horse', Horse);

    Horse.$inject = ['$resource', 'DateUtils'];

    function Horse ($resource, DateUtils) {
        var resourceUrl =  'api/horses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.birthDate = DateUtils.convertLocalDateFromServer(data.birthDate);
                        data.lastGearChange = DateUtils.convertLocalDateFromServer(data.lastGearChange);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.birthDate = DateUtils.convertLocalDateToServer(copy.birthDate);
                    copy.lastGearChange = DateUtils.convertLocalDateToServer(copy.lastGearChange);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.birthDate = DateUtils.convertLocalDateToServer(copy.birthDate);
                    copy.lastGearChange = DateUtils.convertLocalDateToServer(copy.lastGearChange);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
