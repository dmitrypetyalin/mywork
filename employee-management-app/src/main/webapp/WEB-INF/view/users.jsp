<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>

<head>
    <meta charset="utf-8">
    <title>My test page</title>
    <link rel="stylesheet" href="css/test.css">
    <link rel="stylesheet" href="css/button.css">
</head>

<h1>USERS</h1>

<body>
<c:choose>
    <c:when test="${empty users}">
        <p>There are no users in the system yet</p>
    </c:when>
    <c:otherwise>
        <table width="100%">
            <tr>
                <td width="50%">
                    <table class="container">
                        <thead>
                        <tr>
                            <th><h1>ID</h1></th>
                            <th><h1>NAME</h1></th>
                            <th><h1>AGE</h1></th>
                            <th><h1>SALARY</h1></th>
                            <th><h1>DUTY</h1></th>
                            <th><h1>ACTION</h1></th>
                        </tr>
                        </thead>
                        <c:forEach items="${users}" var="user" varStatus="status">
                            <tr valign="top">
                                <td>${user.id}</td>
                                <td>${user.name}</td>
                                <td>${user.age}</td>
                                <td>${user.salary}</td>
                                <td>${user.role.duty}</td>
                                <td>
                                    <form:form action="/deleteUser.action" method="get" modelAttribute="userForm">
                                        <input name="id" type="hidden" value="${user.id}"/>
                                        <input class="btn-delete" type="submit" value="Delete"/>
                                    </form:form>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </td>
                <td width="50%" style="top: 0; vertical-align: top; text-align: center; margin: 0px auto; width: 40%;">
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                    <div style="width: 300px; display: inline-block;">
                        <form:form method="POST" action="/addUser.action" modelAttribute="userForm">
                            <table width="100%">
                                <tr>
                                    <td><form:label path="name">Name</form:label></td>
                                    <td><form:input path="name"/></td>
                                </tr>
                                <tr><td colspan="2"><form:errors path='name' style="color: #ff806c"/></td></tr>
                                <tr>
                                    <td><form:label path="age">Age</form:label></td>
                                    <td><form:input path="age"/></td>
                                </tr>
                                <tr><td colspan="2"><form:errors path='age' style="color: #ff806c"/></td></tr>
                                <tr>
                                    <td><form:label path="salary">Salary</form:label></td>
                                    <td><form:input path="salary"/></td>
                                </tr>
                                <tr><td colspan="2"><form:errors path='salary' style="color: #ff806c"/></td></tr>
                                <tr>
                                    <td>Duty</td>
                                    <td>
                                        <select name="roleId">
                                            <option hidden>Select duty</option>
                                            <option value="1">Admin</option>
                                            <option value="2">Supervisor</option>
                                            <option value="3">Accounter</option>
                                            <option value="4">adminTop</option>
                                            <option value="5">Employee</option>
                                        </select>
                                    </td>
                                <tr>
                                </tr><tr><td colspan="2"><form:errors path='roleId' style="color: #ff806c"/></td></tr><tr>
                                <tr>
                                    <td style="padding-top: 10px;" colspan="2">
                                        <input class="user-form-submit" type="submit" value="Submit"/>
                                    </td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </td>
            </tr>
        </table>
    </c:otherwise>
</c:choose>
</body>

<h2>Created by PetSoft</h2>

</html>