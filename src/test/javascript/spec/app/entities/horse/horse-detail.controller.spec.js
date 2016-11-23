'use strict';

describe('Controller Tests', function() {

    describe('Horse Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockHorse, MockRace, MockRaceInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockHorse = jasmine.createSpy('MockHorse');
            MockRace = jasmine.createSpy('MockRace');
            MockRaceInfo = jasmine.createSpy('MockRaceInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Horse': MockHorse,
                'Race': MockRace,
                'RaceInfo': MockRaceInfo
            };
            createController = function() {
                $injector.get('$controller')("HorseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webScrapperApp:horseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
