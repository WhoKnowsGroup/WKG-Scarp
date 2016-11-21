(function() {
    'use strict';

    angular
        .module('webScrapperApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('race-info', {
            parent: 'entity',
            url: '/race-info',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webScrapperApp.raceInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/race-info/race-infos.html',
                    controller: 'RaceInfoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('raceInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('race-info-detail', {
            parent: 'entity',
            url: '/race-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webScrapperApp.raceInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/race-info/race-info-detail.html',
                    controller: 'RaceInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('raceInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RaceInfo', function($stateParams, RaceInfo) {
                    return RaceInfo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'race-info',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('race-info-detail.edit', {
            parent: 'race-info-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-info/race-info-dialog.html',
                    controller: 'RaceInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RaceInfo', function(RaceInfo) {
                            return RaceInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('race-info.new', {
            parent: 'race-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-info/race-info-dialog.html',
                    controller: 'RaceInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                raceName: null,
                                raceDescription: null,
                                createdDate: null,
                                city: null,
                                state: null,
                                startPosition: null,
                                finishPosition: null,
                                horseName: null,
                                trainer: null,
                                jockey: null,
                                margin: null,
                                penalty: null,
                                startingPrice: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('race-info', null, { reload: 'race-info' });
                }, function() {
                    $state.go('race-info');
                });
            }]
        })
        .state('race-info.edit', {
            parent: 'race-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-info/race-info-dialog.html',
                    controller: 'RaceInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RaceInfo', function(RaceInfo) {
                            return RaceInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('race-info', null, { reload: 'race-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('race-info.delete', {
            parent: 'race-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-info/race-info-delete-dialog.html',
                    controller: 'RaceInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RaceInfo', function(RaceInfo) {
                            return RaceInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('race-info', null, { reload: 'race-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
