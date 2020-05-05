<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

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
                <form class="form-search" action="Search">
                    <div class="jumbotron" style="padding:20px 40px 20px 50px">
                        <h2>Data Browser 
                            <small>
                                <span class="badge badge-pill badge-secondary">hg19</span>
                                <span class="badge badge-pill badge-secondary">beta</span>
                            </small>
                        </h2>

                        <div class="row">
                            <div class="col-9">
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

                                <p class="text-muted" style="margin-left: 5px">
                                    Examples - 
                                    Variant: <a href="Search?query=12-64849716-T-C">12-64849716-T-C</a>, 
                                    Gene: <a href="Search?query=TBK1">TBK1</a>, 
                                    Region: <a href="Search?query=21:33032075-33040899">21:33032075-33040899</a>
                                </p>

                                <div class="row align-items-center">
                                    <!--<label class="col-sm-1 col-form-label col-form-label-lg">Filters:</label>-->

                                    <div class="btn-group col-auto " style="margin-left: 5px">
                                        <button type="button" class="btn btn-light dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                            Max AF
                                        </button>
                                        <div class="dropdown-menu">
                                            <a class="dropdown-item" href="#">Not apply</a>
                                            <div class="dropdown-divider"></div>
                                            <a class="dropdown-item" href="#">0.01</a>
                                            <a class="dropdown-item" href="#">0.005</a>
                                            <a class="dropdown-item" href="#">0.001</a>
                                        </div>
                                    </div>

                                    <div class="form-check col-auto">
                                        <input class="form-check-input" type="checkbox" value="" id="defaultCheck1">
                                        <label class="form-check-label" for="defaultCheck1">
                                            High quality variants
                                        </label>
                                    </div>
                                </div>


                            </div>

                            <div class="col-3 text-center">
                                <c:import url="/SampleCount" />
                                <c:if test="${not empty sampleCount && not empty username}" >
                                    <div class="bg-light">
                                        <h2><fmt:formatNumber type = "number" value = "${sampleCount}"/></h2>
                                        <p><i class="fas fa-dna"></i> NGS Samples</p>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </form>
                <%@include file="result.jsp" %>  
            </div>
        </div>
        <%@include file="base/footer.jsp" %>
    </body>
</html>