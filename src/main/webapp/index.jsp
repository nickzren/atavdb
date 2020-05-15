<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>ATAVDB</title>

        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link rel="shortcut icon" type="image/x-icon" href="img/favicon.ico">

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
                <form id="form-search" class="form-search" action="Search">
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
                                    <input id="query" name="query" class="form-control"
                                           type="text" placeholder="Search for a variant"
                                           <c:if test="${empty username}" >
                                               data-toggle="tooltip" title="Sign In to search"
                                           </c:if>
                                           <c:if test="${not empty query}" >
                                               value="${query}"
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
                                    Variant: <a href="#" onclick="document.getElementById('query').value='12-64875725-C-A'; 
                                        document.getElementById('form-search').submit();">12-64875725-C-A</a>, 
                                    Gene: <a href="#" onclick="document.getElementById('query').value='TBK1'; 
                                        document.getElementById('form-search').submit();">TBK1</a>, 
                                    Region: <a href="#" onclick="document.getElementById('query').value='2:166889788-166895788'; 
                                        document.getElementById('form-search').submit();">2:166889788-166895788</a>
                                </p>

                                <c:set var="phenotype_list" value="${['Not apply','amyotrophic lateral sclerosis',
                                                                     'autoimmune disease','bone disease',
                                                                     'brain malformation','cancer','cardiovascular disease',
                                                                     'congenital disorder','control','control mild neuropsychiatric disease',
                                                                     'dementia','dermatological disease','diseases that affect the ear',
                                                                     'endocrine disorder','epilepsy','febrile seizures','fetal ultrasound anomaly',
                                                                     'gastrointestinal disease','healthy family member','hematological disease',
                                                                     'infectious disease','intellectual disability','kidney and urological disease',
                                                                     'liver disease','metabolic disease','neurodegenerative','nonhuman','obsessive compulsive disorder',
                                                                     'ophthalmic disease','other','other neurodevelopmental disease','other neurological disease',
                                                                     'other neuropsychiatric disease','primary immune deficiency','pulmonary disease',
                                                                     'schizophrenia','sudden death','alzheimers disease','cerebral palsy']}"/>
                                <div class="row align-items-center">
                                    <div class="form-group col-auto" style="margin-left: 5px"
                                         data-toggle="tooltip" title="Search variants by selected phenotype">
                                        <label for="inputPhenotype">Phenotype:</label>
                                        <c:if test="${empty phenotype}" >
                                            <c:set var="phenotype" value="Not apply"/>
                                        </c:if>
                                        <select id="inputPhenotype" name="phenotype" class="form-control">
                                            <c:forEach items="${phenotype_list}" var="p">
                                                <option value="${p}" 
                                                        <c:if test="${phenotype == p}" >
                                                            selected
                                                        </c:if>
                                                        >${p}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    
                                    <div class="form-group col-auto" style="margin-left: 5px" 
                                         data-toggle="tooltip" title="Search variants when its AF is less than selected cutoff">
                                        <label for="inputMaxAF">Max AF:</label>
                                        <c:set var="af_list" value="${['Not apply','0.01','0.005','0.001']}"/>
                                        <c:if test="${empty maxAF}" >
                                            <c:set var="maxAF" value="Not apply"/>
                                        </c:if>
                                        <select id="inputMaxAF" name="maxAF" class="form-control">
                                            <c:forEach items="${af_list}" var="af">
                                                <option value="${af}" 
                                                        <c:if test="${maxAF == af}" >
                                                            selected
                                                        </c:if>
                                                        >${af}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="custom-control custom-switch col-auto" style="margin-left: 18px"
                                         data-toggle="tooltip" title="DP_bin >= 10; GQ >= 20; SNV-SOR <= 3; INDEL-SOR <= 10; 
                                         SNV-FS <= 60; INDEL-FS <= 200; MQ >= 40; QD >= 5; Qual >= 50; RPRS >= -3; 
                                         MQRS >= -10; FILTER = PASS or LIKELY or INTERMEDIATE">
                                        <input type="checkbox" class="custom-control-input" id="input_high_quality_variants" name="isHighQualityVariants" 
                                               <c:if test="${not empty isHighQualityVariants}" >
                                                   checked
                                               </c:if>
                                               >
                                        <label class="custom-control-label" for="input_high_quality_variants">High quality variants</label>
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