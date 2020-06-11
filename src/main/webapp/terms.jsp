<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>Terms</title>

        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <link rel="shortcut icon" type="image/x-icon" href="img/favicon.ico">

        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" href="css/all-fontawesome.css">
        <link rel="stylesheet" href="css/main.css">
    </head>

    <body>
        <div class="container">

            <%@include file="base/header.jsp" %>

            <div class="container-main">
                <h3 class="page-header">Terms</h3>

                <h4><i class="fa fa-gavel"></i> Terms of use</h4>
                <p>
                    The content of the ATAVDB is intended strictly for educational and research purposes. 
                    The data derived from this website may not be used for any commercial purpose. 
                    The data from this website may not be replicated on any other website without written consent.
                </p>
                
                <br/>

                <h4><i class="fa fa-star"></i> Citation</h4>                
                <p>                    
                    Zhong Ren, Gundula Povysil, David B. Goldstein. (2020) ATAV - a comprehensive platform for population-scale genomic analyses. 
                    <em>bioRxiv <a href="https://doi.org/10.1101/2020.06.08.136507" 
                                   target="_blank">DOI: 10.1101/2020.06.08.136507</a></em>.
                </p>
            </div>

        </div>

        <%@include file="base/footer.jsp" %>
        <%@include file="base/counter.jsp" %>
    </body>
</html>