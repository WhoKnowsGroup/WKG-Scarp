(function() {
    'use strict';

    angular
        .module('webScrapperApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('renamed-horses', {
            parent: 'entity',
            url: '/renamed-horses',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webScrapperApp.renamedHorses.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/renamed-horses/renamed-horses.html',
                    controller: 'RenamedHorsesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('renamedHorses');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('renamed-horses-detail', {
            parent: 'entity',
            url: '/renamed-horses/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webScrapperApp.renamedHorses.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/renamed-horses/renamed-horses-detail.html',
                    controller: 'RenamedHorsesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('renamedHorses');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RenamedHorses', function($stateParams, RenamedHorses) {
                    return RenamedHorses.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'renamed-horses',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('renamed-horses-detail.edit', {
            parent: 'renamed-horses-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/renamed-horses/renamed-horses-dialog.html',
                    controller: 'RenamedHorsesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RenamedHorses', function(RenamedHorses) {
                            return RenamedHorses.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('renamed-horses.new', {
            parent: 'renamed-horses',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/renamed-horses/renamed-horses-dialog.html',
                    controller: 'RenamedHorsesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                oldName: null,
                                newName: null,
                                dateChanged: null,
                                ageAndBreed: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('renamed-horses', null, { reload: 'renamed-horses' });
                }, function() {
                    $state.go('renamed-horses');
                });
            }]
        })
        .state('renamed-horses.edit', {
            parent: 'renamed-horses',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/renamed-horses/renamed-horses-dialog.html',
                    controller: 'RenamedHorsesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RenamedHorses', function(RenamedHorses) {
                            return RenamedHorses.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('renamed-horses', null, { reload: 'renamed-horses' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('renamed-horses.delete', {
            parent: 'renamed-horses',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/renamed-horses/renamed-horses-delete-dialog.html',
                    controller: 'RenamedHorsesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RenamedHorses', function(RenamedHorses) {
                            return RenamedHorses.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('renamed-horses', null, { reload: 'renamed-horses' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
