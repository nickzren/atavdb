<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>Contact</title>

        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link rel="shortcut icon" type="image/x-icon" href="img/favicon.ico">

        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" href="css/all-fontawesome.css">
        <link rel="stylesheet" href="css/main.css">
        
        <!-- load jQuery 3.4.1 -->
        <script type="text/javascript" src="js/jquery-3.4.1.slim.min.js"></script>
        <script type="text/javascript">
            var jQuery_3_4_1 = $.noConflict();
        </script>
        <script src="js/bootstrap.min.js"></script>
    </head>

    <body>
        <div class="container">

            <%@include file="base/header.jsp" %>

            <div class="container-main">
                <h3 class="page-header">Contact</h3>

                <address>
                    <strong>Nick Ren</strong><br>
                    <a href="mailto: z.ren@columbia.edu" target="_blank">
                        <i class="far fa-envelope">
                            z.ren@columbia.edu
                        </i>
                    </a>
                </address>

                <address>
                    <strong>Institute for Genomic Medicine</strong><br>
                    Columbia University Medical Center<br>
                    701 W 168th Street<br>
                    Hammer Building 404C<br>
                    New York, NY 10032
                </address>
            </div>
        </div>

        <%@include file="base/footer.jsp" %>
        <%@include file="base/counter.jsp" %>
    </body>
</html>
