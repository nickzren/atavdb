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

    jQuery_3_4_1(function () {
        jQuery_3_4_1('[data-toggle="tooltip"]').tooltip();
    });
</script>

<c:if test="${not empty query}" >
    <div class="row">
        <div class="col">
            <h4><mark>Query: ${query}</mark></h4>
        </div>

        <c:if test="${variantList.size() == 1 && !query.contains(',')}" >
            <c:forEach items="${variantList}" var="variant">                
                <c:if test="${variant.getRsNumberStr() != 'NA'}" >
                    <div class="col-1 text-center">
                        <a class="btn btn-outline-secondary btn-sm" href="https://www.ncbi.nlm.nih.gov/clinvar?term=${variant.getRsNumberStr()}" target="_blank">ClinVar</a>
                    </div>
                    <div class="col-1 text-center">
                        <a class="btn btn-outline-secondary btn-sm" href="https://www.ncbi.nlm.nih.gov/snp/${variant.getRsNumberStr()}" target="_blank">dbSNP</a>
                    </div>
                </c:if>
                <div class="col-1 text-center">
                    <a class="btn btn-outline-secondary btn-sm" href="https://franklin.genoox.com/variant/snp/chr${variant.getVariantIdStr()}" target="_blank">Franklin</a>
                </div>
                <div class="col-1 text-center">
                    <a class="btn btn-outline-secondary btn-sm" href="https://gnomad.broadinstitute.org/variant/${variant.getVariantIdStr()}" target="_blank">gnomAD</a>
                </div>
                <div class="col-1 text-center">
                    <a class="btn btn-outline-secondary btn-sm" href="http://myvariant.info/v1/variant/${variant.getVariantIdStr2()}?assembly=hg19&format=html" target="_blank">MyVariant</a>
                </div>
            </c:forEach>
        </c:if>   
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
    <c:forEach items="${variantList}" var="variant">
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
            </tbody>
        </table>

        <br/>

        <gnx-summary></gnx-summary>
        <script src="https://s3.amazonaws.com/resources.genoox.com/assets/1.0/gnx-elements.js"></script>
        <script type="text/javascript">
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