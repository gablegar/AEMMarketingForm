<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" %>
<style>
    body{background-color:${properties.color}!important}
</style>
<div id="bodyContent">
    <div class="genericBox">
        <div>
            You want to receive new benefits? Easy. Just fill in the fields below.
        </div>
        <form action="${currentPage.path}/jcr:content.save.json" method="post">
            First name: <input type="text" name="firstName" placeholder="first name"><br>
            Last name: <input type="text" name="lastName" placeholder="last name"><br>
            Business: <input type="text" name="business" placeholder="Business"><br>
            Phone: <input type="text" name="phoneNumber" placeholder="phone number"><br>
            <input type="submit" value="Submit">
        </form>
    </div>
</div>
