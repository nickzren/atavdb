<script type="text/javascript">
    jQuery_3_3_1(document).ready(function () {
    var variant_table = jQuery_3_3_1('#variant_table').DataTable({
    //            searching: false,
    //            paging: false,
    //            info: false,
    //            lengthChange: false,
    buttons: [
    {
    extend: 'csv',
    text: '<i class="fas fa-file-csv"></i> Download',
    className: 'btn btn-light'
    },
    ],
    });

    variant_table.buttons().container().appendTo('#variant_table_wrapper .col-md-6:eq(0)');

    var carrier_table = jQuery_3_3_1('#carrier_table').DataTable({
    //            lengthChange: false,
    buttons: [
    {
    extend: 'csv',
    text: '<i class="fas fa-file-csv"></i> Download',
    className: 'btn btn-light'
    },
    ],
    });

    carrier_table.buttons().container().appendTo('#carrier_table_wrapper .col-md-6:eq(0)');
    });

    jQuery_3_4_1(function () {
    jQuery_3_4_1('[data-toggle="tooltip"]').tooltip();
    });
</script>

<c:if test="${not empty query}" >
    <div class="row">
        <div class="col">
            <h4><mark>${queryType}: ${query}</mark></h4>
        </div>

        <c:if test="${queryType == 'Variant'}" >
            <c:forEach items="${variantList}" var="variant">                
                <c:if test="${variant.getRsNumberStr() != 'NA'}" >
                    <div class="col-auto">
                        <a class="btn btn-outline-secondary btn-sm" href="https://www.ncbi.nlm.nih.gov/clinvar?term=${variant.getRsNumberStr()}" target="_blank">ClinVar</a>
                    </div>
                    <div class="col-auto">
                        <a class="btn btn-outline-secondary btn-sm" href="https://www.ncbi.nlm.nih.gov/snp/${variant.getRsNumberStr()}" target="_blank">dbSNP</a>
                    </div>
                </c:if>
                <div class="col-auto">
                    <a class="btn btn-outline-secondary btn-sm" href="https://franklin.genoox.com/variant/snp/chr${variant.getVariantIdStr()}" target="_blank">Franklin</a>
                </div>
                <div class="col-auto">
                    <a class="btn btn-outline-secondary btn-sm" href="https://gnomad.broadinstitute.org/variant/${variant.getVariantIdStr()}" target="_blank">gnomAD</a>
                </div>
                <div class="col-auto">
                    <a class="btn btn-outline-secondary btn-sm" href="http://myvariant.info/v1/variant/${variant.getVariantIdStr2()}?assembly=hg19&format=html" target="_blank">MyVariant</a>
                </div>
                <div class="col-auto">
                    <a class="btn btn-outline-secondary btn-sm" href="http://trap-score.org/Search?query=${variant.getVariantIdStr()}" target="_blank">TraP</a>
                </div>
                <div class="col-auto">
                    <a class="btn btn-outline-secondary btn-sm" href="https://genome.ucsc.edu/cgi-bin/hgTracks?db=hg19&position=chr${variant.getChrStr()}${variant.getStartPosition()}-${variant.getStartPosition()}" target="_blank">UCSC</a>
                </div>
            </c:forEach>
        </c:if>

        <c:if test="${queryType=='Gene'}" >
            <div class="col-auto">
                <a class="btn btn-outline-secondary btn-sm" href="https://decipher.sanger.ac.uk/gene/${query}" target="_blank">DECIPHER</a>
            </div>
            <div class="col-auto">
                <a class="btn btn-outline-secondary btn-sm" href="https://grch37.ensembl.org/Homo_sapiens/Gene/Summary?g=${query}" target="_blank">Ensembl</a>
            </div>
            <div class="col-auto">
                <a class="btn btn-outline-secondary btn-sm" href="https://www.genecards.org/cgi-bin/carddisp.pl?gene=${query}" target="_blank">GeneCards</a>
            </div>
            <div class="col-auto">
                <a class="btn btn-outline-secondary btn-sm" href="https://www.genenames.org/tools/search/#!/genes?query=${query}" target="_blank">HGNC</a>
            </div>       
            <div class="col-auto">
                <a class="btn btn-outline-secondary btn-sm" href="https://omim.org/search?search=${query}" target="_blank">OMIM</a>
            </div>
            <div class="col-auto">
                <a class="btn btn-outline-secondary btn-sm" href="http://genic-intolerance.org/Search?query=${query}" target="_blank">RVIS</a>
            </div>
        </c:if>
        <c:if test="${queryType=='Region'}" >
            <div class="col-auto">
                <a class="btn btn-outline-secondary btn-sm" href="https://genome.ucsc.edu/cgi-bin/hgTracks?db=hg19&position=chr${query}" target="_blank">UCSC</a>
            </div>
        </c:if>
    </div>

    <br/>
    <br/>

    <c:if test="${not empty error}" >
        <div class="row">
            <div class="col-auto">
                <div class="alert alert-warning" role="alert">
                    <i class="fas fa-exclamation-circle"></i>&nbsp;${error}
                </div>
            </div>
        </div>
    </c:if>

    <c:if test="${not empty message}" >
        <div class="row">
            <div class="col-auto">
                <div class="alert alert-warning" role="alert">
                    <i class="fas fa-exclamation-circle"></i>&nbsp;${message}
                </div>
            </div>
        </div>
    </c:if>

    <c:if test="${not empty variantList}" >
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Variants</h5>

                <c:set var = "variant_table" value = "variant_table"/>
                <c:if test="${queryType == 'Variant'}" >
                    <c:set var = "variant_table" value = ""/>
                </c:if>

                <div class='table-responsive'>
                    <table id="${variant_table}" class="table table-hover text-center align-middle">
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
                                <th>
                                    <div data-toggle="tooltip" title="Number of samples are over 10x coverage">
                                        10x Sample
                                    </div>
                                </th>
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
                                <td>${variant.get10xSample()}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </c:if>
</c:if>

<br/>
<br/>

<c:if test="${queryType == 'Variant'}" >
    <c:forEach items="${variantList}" var="variant">
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Annotations</h5>

                <div class='table-responsive'>
                    <table class="table table-hover text-center align-middle">
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
                </div>
            </div>
        </div>

        <br/>
        <br/>

        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Carriers</h5>

                <div class="row">      
                    <c:forEach items="${genders}" var="gender">
                        <div class="col-auto">
                            <span class="badge badge-light">${variant.getGenderCount()[gender.getIndex()]}
                                ${gender.getName()}</span> 
                        </div>
                    </c:forEach>
                </div>

                <br/>

                <c:choose>
                    <c:when test="${not empty variant.getCarriers()}" >
                        <div class='table-responsive'>
                            <table id="carrier_table" class="table table-hover text-center align-middle">
                                <thead>
                                    <tr>
                                <c:if test="${not empty is_authorized}" >
                                    <th>
                                        <div data-toggle="tooltip" title="Sample experiment_id in sequenceDB">
                                            Experiment ID
                                        </div>
                                    </th>
                                </c:if>
                                <th>
                                    <div data-toggle="tooltip" title="seqGender in sequenceDB">
                                        Gender
                                    </div>
                                </th>
                                <th>
                                    <div data-toggle="tooltip" title="Borad Phenotype in sequenceDB">
                                        Phenotype
                                    </div>
                                </th>
                                <th>
                                    <div data-toggle="tooltip" title="Genotype">
                                        GT
                                    </div>
                                </th>
                                <th>
                                    <div data-toggle="tooltip" title="Read Depth">
                                        DP
                                    </div>
                                </th>
                                <th>
                                    <div data-toggle="tooltip" title="Genotype Quality">
                                        GQ
                                    </div>
                                </th>
                                <th>
                                    <div data-toggle="tooltip" title="">
                                        FILTER
                                    </div>
                                </th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${variant.getCarriers()}" var="carrier">                                
                                    <tr>
                                    <c:if test="${not empty is_authorized}" >
                                        <td>
                                            <a href="https://sequence.igm.cumc.columbia.edu/search.php?action=viewSample&experiment_id=${carrier.getExperimentId()}" target="_blank">
                                                ${carrier.getExperimentId()}
                                            </a>
                                        </td>
                                    </c:if>
                                    <td>${carrier.getGender()}</td>
                                    <td>${carrier.getPhenotype()}</td>
                                    <td>${carrier.getGTStr()}</td>
                                    <td>${carrier.getDP()}</td>
                                    <td>${carrier.getGQ()}</td>
                                    <td>${carrier.getFILTER()}</td>
                                    </tr>
                                </c:forEach>

                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-warning" role="alert">
                            <i class="fas fa-exclamation-circle"></i>&nbsp;
                            <strong>Not displaying carriers data when AF > 0.01</strong>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <br/>
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