(function() {
    'use strict';

    angular
        .module('webScrapperApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('race', {
            parent: 'entity',
            url: '/race',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webScrapperApp.race.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/race/races.html',
                    controller: 'RaceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('race');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('race-detail', {
            parent: 'entity',
            url: '/race/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webScrapperApp.race.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/race/race-detail.html',
                    controller: 'RaceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('race');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Race', function($stateParams, Race) {
                    return Race.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'race',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('race-detail.edit', {
            parent: 'race-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race/race-dialog.html',
                    controller: 'RaceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Race', function(Race) {
                            return Race.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('race.new', {
            parent: 'race',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race/race-dialog.html',
                    controller: 'RaceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                raceName: null,
                                city: null,
                                raceDescription: null,
                                state: null,
                                raceDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('race', null, { reload: 'race' });
                }, function() {
                    $state.go('race');
                });
            }]
        })
        .state('race.edit', {
            parent: 'race',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race/race-dialog.html',
                    controller: 'RaceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Race', function(Race) {
                            return Race.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('race', null, { reload: 'race' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('race.delete', {
            parent: 'race',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race/race-delete-dialog.html',
                    controller: 'RaceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Race', function(Race) {
                            return Race.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('race', null, { reload: 'race' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
