<div class="row">    
    
    <div class="col-md-12">
        <c:if test="${not empty query}" >
            <h4><mark>Query: ${query}</mark></h4>
            <br><br>
            <c:choose>
                <c:when test="${empty variant}" >
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
                                <tr class="text-center">
                                    <td class="align-middle">${variant.getVariantIdStr()}</td>
                                    <td class="align-middle">${variant.getEffect()}</td>
                                    <td class="align-middle">${variant.getGeneName()}</td>
                                    <td class="align-middle">${variant.getAC()}</td>
                                    <td class="align-middle">${variant.getAN()}</td>
                                    <td class="align-middle">${variant.getAF()}</td>
                                    <td class="align-middle">${variant.getNS()}</td>
                                    <td class="align-middle">${variant.getNH()}</td>
                                </tr>
                            </tbody>
                        </table>
                </c:otherwise>
            </c:choose>
        </c:if>
    </div>
</div>

<script type="text/javascript">
    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    });
</script>