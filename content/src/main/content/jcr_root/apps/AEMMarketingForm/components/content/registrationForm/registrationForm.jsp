<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" %>
<style>
    body{background-color:${properties.color}!important}
</style>
<div id="bodyContent">
    <div class="genericBox">
        <h1>
            ${properties.title}
        </h1>
        <form action="${currentPage.path}/jcr:content.save.jsonsdfs" method="post">
            <input class="form-input" type="text" name="firstName" placeholder="first name"><br>
            <input class="form-input" type="text" name="lastName" placeholder="last name"><br>
            <input class="form-input" type="text" name="business" placeholder="Business"><br>
            <input class="form-input" type="text" name="phoneNumber" placeholder="phone number"><br>
            <input type="hidden" name="campaignName" value="${properties.campaignName}"><br>
            <input type="submit" class="submit" value="Submit">
        </form>
    </div>
</div>
