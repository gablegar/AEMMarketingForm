<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" %>
<style>
    body{background-color:${properties.color}!important}
</style>
<div id="bodyContent">
    <div class="genericBox" ng-app="registrationForm" ng-controller="registrationFormController" ng-init="init('${currentPage.path}/jcr:content.save.json','${properties.fieldsListUrl}' )">
        <h1>
            ${properties.title}
        </h1>
        <form ng-submit="submitRegistration()" id="registrationForm" ng-hide="hideForm">
            <span ng-repeat="field in fieldList">
                <input class="form-input" type="text" name="{{field.text}}" placeholder="{{field.text}}"><br>
            </span>
            <input type="hidden" name="campaignName" value="${properties.campaignName}"><br>
            <input type="submit" class="submit" value="Submit">
        </form>
        <div ng-show="showSuccess">
            ${properties.successMessage}
            <br>
            <button ng-click="showForm()" class="submit">
                ${properties.sendAnother}
            </button>
        </div>
        <div ng-hide="hideError">
            ${properties.errorMessage}
        </div>
    </div>
</div>
