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
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                                <tr class="text-center">
                                    <th class="align-middle" scope="col">Variant ID</th>
                                    <th class="align-middle" scope="col">Effect</th>
                                    <th class="align-middle" scope="col">Gene</th>
                                    <th class="align-middle" scope="col">AC</th>
                                    <th class="align-middle" scope="col">AN</th>
                                    <th class="align-middle" scope="col">AF</th>
                                    <th class="align-middle" scope="col">NS</th>
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
                    </div>
                </c:otherwise>
            </c:choose>
        </c:if>
    </div>
</div>
