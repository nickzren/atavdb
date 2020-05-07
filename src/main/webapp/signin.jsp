<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>ATAVDB Sign In</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link rel="shortcut icon" type="image/x-icon" href="img/favicon.ico">

        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" href="css/all-fontawesome.css">
        <link rel="stylesheet" href="css/main.css">
        <link rel="stylesheet" href="css/signin.css">
    </head>

    <body>
        <div class="form-signin">
            <form action="SignIn" method="post">
                <h1 class="h3 mb-3 font-weight-normal">Please sign in</h1>

                <div class="form-label-group">
                    <input type="text" id="inputUsername" name="username" class="form-control" placeholder="MC username (usually your UNI)" required autofocus>
                    <label for="inputUsername">MC username (usually your UNI)</label>
                </div>

                <div class="form-label-group">
                    <input type="password" id="inputPassword" name="password" class="form-control" placeholder="MC password" required>
                    <label for="inputPassword">MC password</label>
                </div>

                <button class="btn btn-lg btn-primary btn-block" type="submit"><i class="fas fa-sign-in-alt"></i>&nbsp;Sign in</button>

                <br/>

                <c:if test="${not empty error}" >
                    <div class="alert alert-warning" role="alert">
                        <i class="fas fa-exclamation-circle"></i>&nbsp;<strong>${error}</strong>
                    </div>
                    <br/>
                </c:if>

<!--                <div class="alert alert-info">
                    <i class="fas fa-info-circle"></i>&nbsp;New account registration from <a href="https://core.igm.cumc.columbia.edu/" target="_blank">IGM Core</a>.
                </div>-->

                <small class="form-text text-muted">
                    By using these resources, you agree to abide by Columbia University's 
                    <a href="http://policylibrary.columbia.edu/acceptable-usage-information-resources-policy" target="_blank">Acceptable Usage of Information Resources Policy.</a>
                </small>
            </form>
        </div>    
    </body>
</html>