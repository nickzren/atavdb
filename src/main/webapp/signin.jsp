<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>ATAVDB Sign In</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link rel="shortcut icon" type="image/x-icon" href="favicon.ico">

        <link rel="stylesheet" href="css/bootstrap.css">
        <link rel="stylesheet" href="css/all-fontawesome.css">
        <link rel="stylesheet" href="css/main.css">
        <link rel="stylesheet" href="css/signin.css">
    </head>

    <body class="text-center">
        <div>
            <form class="form-signin" action="SignIn" method="post">
                <h1 class="h3 mb-3 font-weight-normal">Please sign in</h1>
                <label for="username" class="sr-only">MC domain username</label>
                <input type="text" name="username" id="username" class="form-control" placeholder="MC domain username" required="" autofocus="">
                <label for="password" class="sr-only">MC domain password</label>
                <input type="password" name="password" id="password" class="form-control" placeholder="MC domain password" required="">
                <button class="btn btn-lg btn-primary btn-block" type="submit"><i class="fas fa-sign-in-alt"></i>&nbsp;Sign in</button>

                <br/>

                <c:if test="${not empty error}" >
                    <div class="alert alert-danger" role="alert">
                        <i class="fas fa-exclamation-circle"></i>&nbsp;<strong>${error}</strong>
                    </div>
                </c:if>

                <br/>

                <div class="alert alert-info">
                    New account registration required to be added from <a href="https://core.igm.cumc.columbia.edu/" target="_blank">IGM Core</a>.
                </div>

                <small class="form-text text-muted">
                    By signing in to IGM's ATAVDB, you agree to 
                    <a href="http://policylibrary.columbia.edu/acceptable-usage-information-resources-policy" target="_blank">Columbia University?s 
                        Acceptable Use Policy.</a>
                </small>
            </form>
        </div>    
    </body>
</html>