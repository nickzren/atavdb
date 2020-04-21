<script type="text/javascript">
    jQuery_3_3_1(document).ready(function () {
        var table = jQuery_3_3_1('#variant_table').DataTable({
            searching: false,
            paging: false,
            info: false,
            lengthChange: false,
            buttons: [
                {
                    extend: 'csv',
                    text: '<i class="fas fa-file-csv"></i> Download',
                    className: 'btn btn-info'
                },
            ],
        });

        table.buttons().container().appendTo('#variant_table_wrapper .col-md-6:eq(0)');
    });
</script>

<c:if test="${not empty query}" >
    <div class="row">
        <div class="col">
            <h4><mark>Query: ${query}</mark></h4>
        </div>
    </div>
    <br>
    <c:choose>
        <c:when test="${empty variantList}" >
            <div class="alert alert-warning" style="width:50%">
                <i class="fas fa-exclamation-circle"></i>&nbsp;No results found from search query.
            </div>
        </c:when>
        <c:otherwise>        
            <table id="variant_table" class="table text-center align-middle">
                <thead>
                    <tr>
                        <th>Variant ID</th>
                        <th>
                            <div data-toggle="tooltip" title="Function Effect (Ensemble 87)">
                                Effect
                            </div>
                        </th>
                        <th>
                            <div data-toggle="tooltip" title="HGNC Gene (Ensemble 87)">
                                Gene
                            </div>
                        </th>
                        <th>
                            <div data-toggle="tooltip" title="Allele Acount">
                                AC
                            </div>
                        </th>
                        <th>
                            <div data-toggle="tooltip" title="Allele Number (total number of alleles)">
                                AN
                            </div>
                        </th>
                        <th>
                            <div data-toggle="tooltip" title="Allele Frequency">
                                AF
                            </div>
                        </th>
                        <th>
                            <div data-toggle="tooltip" title="Number of samples having data">
                                NS
                            </div>
                        </th>
                        <th>Number of homozygotes</th>
                    </tr>
                </thead>
                <tbody>

                <c:forEach items="${variantList}" var="variant">
                    <tr>
                        <td>
                            <a href="Search?query=${variant.getVariantIdStr()}">${variant.getVariantIdStr()}</a>                            
                        </td>
                        <td>${variant.getEffect()}</td>
                        <td>${variant.getGeneName()}</td>
                        <td>${variant.getAC()}</td>
                        <td>${variant.getAN()}</td>
                        <td>${variant.getAF()}</td>
                        <td>${variant.getNS()}</td>
                        <td>${variant.getNH()}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</c:if>

<br/>

<c:if test="${variantList.size() == 1 && !query.contains(',')}" >
    <div class="row">
        <div class="col-2">
            <h4>Annotations</h4> &nbsp;&nbsp;
        </div>

        <c:forEach items="${variantList}" var="variant">
            <c:if test="${variant.getRsNumberStr() != 'NA'}" >
                <div class="col-1">
                    <a href="https://www.ncbi.nlm.nih.gov/snp/${variant.getRsNumberStr()}" target="_blank">dbSNP</a>
                </div>
            </c:if>
            <div class="col-1">
                <a href="https://gnomad.broadinstitute.org/variant/${variant.getVariantIdStr()}" target="_blank">gnomAD</a>
            </div>
        </c:forEach>
    </div>

    <table class="table text-center align-middle">
        <thead>
            <tr>
                <th>Effect</th>
                <th>Gene</th>
                <th>Transcript</th>
                <th>HGVS_c</th>
                <th>HGVS_p</th>
                <th>Polyphen</th>
            </tr>
        </thead>

        <tbody>    
        <c:forEach items="${variantList}" var="variant">
            <c:forEach items="${variant.getAllAnnotation()}" var="annotation">
                <tr>
                    <td>${annotation.getEffect()}</td>
                    <td>${annotation.getGeneName()}</td>
                    <td>${annotation.getStableId()}</td>
                    <td>${annotation.getHGVS_c()}</td>
                    <td>${annotation.getHGVS_p()}</td>
                    <td>${annotation.getPolyphen()}</td>
                </tr>
            </c:forEach>
        </c:forEach>
        </tbody>
    </table>

    <br/>

    <c:forEach items="${variantList}" var="variant">
        <gnx-summary></gnx-summary>
        <script src="https://s3.amazonaws.com/resources.genoox.com/assets/1.0/gnx-elements.js"></script>
        <script>
            let elem = document.querySelector('gnx-summary');
            elem.variantId = {
                ref: '${variant.getRefAllele()}',
                alt: '${variant.getAllele()}',
                chr: '${variant.getChrStr()}',
                pos: ${variant.getStartPosition()},
            };
        </script>
    </c:forEach>
</c:if>        

<script type="text/javascript">
    jQuery_3_4_1(function () {
        jQuery_3_4_1('[data-toggle="tooltip"]').tooltip();
    });
</script>