<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>ATAVDB</title>

        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link rel="shortcut icon" type="image/x-icon" href="favicon.ico">

        <link rel="stylesheet" type="text/css" href="https://s3.amazonaws.com/resources.genoox.com/assets/1.0/gnx-elements.css">
        
        <!-- datatables css start-->
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.20/css/dataTables.bootstrap4.min.css"/>
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/buttons/1.6.1/css/buttons.bootstrap4.min.css"/>
        <!-- datatables css end-->
        
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" href="css/all-fontawesome.css">
        <link rel="stylesheet" href="css/main.css">

        <!-- load jQuery 3.4.1 -->
        <script type="text/javascript" src="js/jquery-3.4.1.slim.min.js"></script>
        <script type="text/javascript">
            var jQuery_3_4_1 = $.noConflict();
        </script>
        <script src="js/popper.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        
        <!-- datatables js start-->
        <!-- load jQuery 3.3.1 -->
        <script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.js"></script>
        <script type="text/javascript">
            var jQuery_3_3_1 = $.noConflict();
        </script>
        <script type="text/javascript" src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" src="https://cdn.datatables.net/1.10.20/js/dataTables.bootstrap4.min.js"></script>
        <script type="text/javascript" src="https://cdn.datatables.net/buttons/1.6.1/js/dataTables.buttons.min.js"></script>
        <script type="text/javascript" src="https://cdn.datatables.net/buttons/1.6.1/js/buttons.bootstrap4.min.js"></script>
        <script type="text/javascript" src="https://cdn.datatables.net/buttons/1.6.1/js/buttons.html5.min.js"></script>
        <!-- datatables js end-->
    </head>

    <body>
        <div class="container">

            <%@include file="base/header.jsp" %>

            <div class="container-main">

                <div class="jumbotron" style="padding:20px 40px 20px 50px">
                    <h2>Data Browser 
                        <small>
                            <span class="badge badge-pill badge-secondary">beta</span>
                        </small>
                    </h2>

                    <div class="row">
                        <div class="col-9">
                            <form class="form-search" action="Search">
                                <div class="input-group">
                                    <input name="query" class="form-control"
                                           type="text" placeholder="Search for a variant"
                                           <c:if test="${empty username}" >
                                               data-toggle="tooltip" title="Sign In to search"
                                           </c:if>
                                           >
                                    <div class="input-group-append">
                                        <button class="btn btn-primary" type="submit">
                                            <i class="fas fa-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>

                        <div class="col-3 text-center">
                            <c:import url="/SampleCount" />
                            <c:if test="${not empty sampleCount && not empty username}" >
                                <h2 data-toggle="tooltip" title="Total available samples in ATAVDB">
                                    <span class="badge badge-success">${sampleCount} Samples</span>
                                </h2>
                            </c:if>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-9">
                            <p class="text-muted" style="margin-left: 5px">
                                Examples - 
                                Variant: <a href="Search?query=1-13273-G-C">1-13273-G-C</a>, 
                                Variants: <a href="Search?query=2-1010474-G-A,12-64849716-T-C,21-9411609-G-T">2-1010474-G-A,12-64849716-T-C,21-9411609-G-T</a>
                            </p>
                        </div>
                    </div>
                </div>

                <%@include file="result.jsp" %>  
            </div>
        </div>
        <%@include file="base/footer.jsp" %>
    </body>
</html>