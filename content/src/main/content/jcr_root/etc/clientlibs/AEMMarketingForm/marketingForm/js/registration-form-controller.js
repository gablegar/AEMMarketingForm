app.controller("registrationFormController",['$scope','$http', function($scope, $http) {

    $scope.hideError = true;
    $scope.showSuccess = false;
    $scope.init = function (endpoint,fieldListUrl) {
        $scope.endpoint = endpoint;
        $scope.fieldListUrl = fieldListUrl + '/_jcr_content.list.json';

        $http({
            method: 'GET', url: $scope.fieldListUrl
        }).then(function (response) {
            $scope.fieldList = response.data;
            console.log(response);
        }, function (response) {
            console.log(response);
        });
    };

    $scope.submitRegistration = function () {
        var formData = $("#registrationForm").serialize();
        return $http({
            method: 'POST',
            url: $scope.endpoint,
            data: formData,
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        }).then(function (response) {
            $scope.hideForm = true;
            $scope.showSuccess = true;
            $scope.hideError = true;
            console.log(response);
        }, function (response) {
            $scope.hideError = false;
            console.log(response);
        });

    };

    $scope.showForm = function () {
        $scope.hideForm = false;
        $scope.showSuccess = false;
        $scope.hideError = true;
    };


}]);