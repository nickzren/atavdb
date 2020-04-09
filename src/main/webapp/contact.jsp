<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>Contact</title>

        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link rel="shortcut icon" type="image/x-icon" href="favicon.ico">

        <link rel="stylesheet" href="css/bootstrap.css">
        <link rel="stylesheet" href="css/main.css">
    </head>

    <body>
        <div class="container">

            <%@include file="base/header.jsp" %>

            <div class="container-main">
                <h3 class="page-header">Contact</h3>

                <address>
                    <strong>Nick Ren</strong><br>
                    Email: 
                    <a href="mailto: z.ren@columbia.edu" target="_blank">
                        <i class="fa fa-envelope-o">
                            z.ren@columbia.edu
                        </i>
                    </a>
                </address>

                <address>
                    <strong>
                        <a class="black" href="http://igm.columbia.edu" target="_blank">
                            Institute for Genomic Medicine
                        </a>
                    </strong><br>
                    Columbia University Medical Center<br>
                    701 W 168th Street<br>
                    Hammer Building 616<br>
                    New York, NY 10032
                </address>
            </div>
        </div>

        <%@include file="base/footer.jsp" %>  
    </body>
</html>
