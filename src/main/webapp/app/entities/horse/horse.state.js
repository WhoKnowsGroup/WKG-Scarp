(function() {
    'use strict';

    angular
        .module('webScrapperApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('horse', {
            parent: 'entity',
            url: '/horse',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webScrapperApp.horse.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/horse/horses.html',
                    controller: 'HorseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('horse');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('horse-detail', {
            parent: 'entity',
            url: '/horse/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webScrapperApp.horse.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/horse/horse-detail.html',
                    controller: 'HorseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('horse');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Horse', function($stateParams, Horse) {
                    return Horse.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'horse',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('horse-detail.edit', {
            parent: 'horse-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/horse/horse-dialog.html',
                    controller: 'HorseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Horse', function(Horse) {
                            return Horse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('horse.new', {
            parent: 'horse',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/horse/horse-dialog.html',
                    controller: 'HorseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                horseName: null,
                                horseStatus: null,
                                birthDate: null,
                                owner: null,
                                stewardsEmbargoes: null,
                                emergencyVaccinationRecordURL: null,
                                lastGearChange: null,
                                trainer: null,
                                prize: null,
                                bonus: null,
                                mimMaxDistWin: null,
                                firstUpData: null,
                                secondUpData: null,
                                horseTrack: null,
                                horseDist: null,
                                horseClass: null,
                                positionInLastRace: null,
                                firm: null,
                                good: null,
                                soft: null,
                                heavy: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('horse', null, { reload: 'horse' });
                }, function() {
                    $state.go('horse');
                });
            }]
        })
        .state('horse.edit', {
            parent: 'horse',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/horse/horse-dialog.html',
                    controller: 'HorseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Horse', function(Horse) {
                            return Horse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('horse', null, { reload: 'horse' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('horse.delete', {
            parent: 'horse',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/horse/horse-delete-dialog.html',
                    controller: 'HorseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Horse', function(Horse) {
                            return Horse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('horse', null, { reload: 'horse' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
