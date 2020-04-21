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
            <table class="table">
                <thead>
                    <tr class="text-center">
                        <th class="align-middle" scope="col">Variant ID</th>
                        <th class="align-middle" scope="col">
                            <div data-toggle="tooltip" title="Function Effect (Ensemble 87)">
                                Effect
                            </div>
                        </th>
                        <th class="align-middle" scope="col">
                            <div data-toggle="tooltip" title="HGNC Gene (Ensemble 87)">
                                Gene
                            </div>
                        </th>
                        <th class="align-middle" scope="col">
                            <div data-toggle="tooltip" title="Allele Acount">
                                AC
                            </div>
                        </th>
                        <th class="align-middle" scope="col">
                            <div data-toggle="tooltip" title="Allele Number (total number of alleles)">
                                AN
                            </div>
                        </th>
                        <th class="align-middle" scope="col">
                            <div data-toggle="tooltip" title="Allele Frequency">
                                AF
                            </div>
                        </th>
                        <th class="align-middle" scope="col">
                            <div data-toggle="tooltip" title="Number of samples having data">
                                NS
                            </div>
                        </th>
                        <th class="align-middle" scope="col">Number of homozygotes</th>
                    </tr>
                </thead>
                <tbody>

                <c:forEach items="${variantList}" var="variant">
                    <tr class="text-center">
                        <td class="align-middle">
                            <a href="Search?query=${variant.getVariantIdStr()}">${variant.getVariantIdStr()}</a>                            
                        </td>
                        <td class="align-middle">${variant.getEffect()}</td>
                        <td class="align-middle">${variant.getGeneName()}</td>
                        <td class="align-middle">${variant.getAC()}</td>
                        <td class="align-middle">${variant.getAN()}</td>
                        <td class="align-middle">${variant.getAF()}</td>
                        <td class="align-middle">${variant.getNS()}</td>
                        <td class="align-middle">${variant.getNH()}</td>
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

    <table class="table">
        <thead>
            <tr class="text-center">
                <th class="align-middle" scope="col">Effect</th>
                <th class="align-middle" scope="col">Gene</th>
                <th class="align-middle" scope="col">Transcript</th>
                <th class="align-middle" scope="col">HGVS_c</th>
                <th class="align-middle" scope="col">HGVS_p</th>
                <th class="align-middle" scope="col">Polyphen</th>
            </tr>
        </thead>

        <tbody>    
        <c:forEach items="${variantList}" var="variant">
            <c:forEach items="${variant.getAllAnnotation()}" var="annotation">
                <tr class="text-center">
                    <td class="align-middle">${annotation.getEffect()}</td>
                    <td class="align-middle">${annotation.getGeneName()}</td>
                    <td class="align-middle">${annotation.getStableId()}</td>
                    <td class="align-middle">${annotation.getHGVS_c()}</td>
                    <td class="align-middle">${annotation.getHGVS_p()}</td>
                    <td class="align-middle">${annotation.getPolyphen()}</td>
                </tr>
            </c:forEach>
        </c:forEach>
        </tbody>
    </table>
</c:if>        

<script type="text/javascript">
    $(function () {
    $('[data-toggle="tooltip"]').tooltip();
    });
</script>