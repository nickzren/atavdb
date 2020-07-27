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
//            buttons: [
//                {
//                    extend: 'csv',
//                    text: '<i class="fas fa-file-csv"></i> Download',
//                    className: 'btn btn-light'
//                },
//            ],
        });

        carrier_table.buttons().container().appendTo('#carrier_table_wrapper .col-md-6:eq(0)');
    });

    jQuery_3_4_1(function () {
        jQuery_3_4_1('[data-toggle="tooltip"]').tooltip({boundary: 'window'});
    });
</script>

<c:if test="${not empty query}" >
    <div class="row">
        <div class="col">
            <h4><mark>${queryType}: ${query}</mark></h4>
        </div>

        <c:if test="${queryType == 'Variant'}" >
            <div class="col-md-auto">
                <a class="btn btn-outline-secondary btn-sm" href="https://franklin.genoox.com/variant/snp/chr${query}" target="_blank">Franklin</a>
            </div>
            <div class="col-md-auto">
                <a class="btn btn-outline-secondary btn-sm" href="https://gnomad.broadinstitute.org/variant/${query}" target="_blank">gnomAD</a>
            </div>
            <div class="col-md-auto">
                <a class="btn btn-outline-secondary btn-sm" href="http://trap-score.org/Search?query=${query}" target="_blank">TraP</a>
            </div>

            <c:forEach items="${variantList}" var="variant">
                <div class="col-md-auto">
                    <a class="btn btn-outline-secondary btn-sm" href="https://www.ncbi.nlm.nih.gov/clinvar/?term=(${variant.getChrStr()}[Chromosome] AND ${variant.getStartPosition()}[Base Position for Assembly GRCh37])" target="_blank">ClinVar</a>
                </div>
                <div class="col-md-auto">
                    <a class="btn btn-outline-secondary btn-sm" href="https://www.ncbi.nlm.nih.gov/snp/?term=(${variant.getChrStr()}[Chromosome] AND ${variant.getStartPosition()}[Base Position Previous])" target="_blank">dbSNP</a>
                </div>

                <div class="col-md-auto">
                    <a class="btn btn-outline-secondary btn-sm" href="http://myvariant.info/v1/variant/${variant.getVariantIdStr2()}?assembly=hg19&format=html" target="_blank">MyVariant</a>
                </div>
                <div class="col-md-auto">
                    <a class="btn btn-outline-secondary btn-sm" href="https://genome.ucsc.edu/cgi-bin/hgTracks?db=hg19&position=chr${variant.getChrStr()}${variant.getStartPosition()}-${variant.getStartPosition()}" target="_blank">UCSC</a>
                </div>
            </c:forEach>
        </c:if>

        <c:if test="${queryType=='Gene'}" >
            <div class="col-md-auto">
                <a class="btn btn-outline-secondary btn-sm" href="https://decipher.sanger.ac.uk/gene/${query}" target="_blank">DECIPHER</a>
            </div>
            <div class="col-md-auto">
                <a class="btn btn-outline-secondary btn-sm" href="https://grch37.ensembl.org/Homo_sapiens/Gene/Summary?g=${query}" target="_blank">Ensembl</a>
            </div>
            <div class="col-md-auto">
                <a class="btn btn-outline-secondary btn-sm" href="https://www.genecards.org/cgi-bin/carddisp.pl?gene=${query}" target="_blank">GeneCards</a>
            </div>
            <div class="col-md-auto">
                <a class="btn btn-outline-secondary btn-sm" href="https://www.genenames.org/tools/search/#!/genes?query=${query}" target="_blank">HGNC</a>
            </div>  
            <div class="col-md-auto">
                <a class="btn btn-outline-secondary btn-sm" href="http://igmdx.org/Search?query=${query}" target="_blank">IGMDx</a>
            </div>  
            <div class="col-md-auto">
                <a class="btn btn-outline-secondary btn-sm" href="https://omim.org/search?search=${query}" target="_blank">OMIM</a>
            </div>
            <div class="col-md-auto">
                <a class="btn btn-outline-secondary btn-sm" href="http://genic-intolerance.org/Search?query=${query}" target="_blank">RVIS</a>
            </div>
        </c:if>
        <c:if test="${queryType=='Region'}" >
            <div class="col-md-auto">
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
                    <i class="fas fa-exclamation-circle"></i>&nbsp;
                    ${message}
                </div>
            </div>
        </div>
    </c:if>

    <c:if test="${not empty flankingRegion}" >
        <div class="row">
            <div class="col-auto">
                <div class="alert alert-info" role="alert">
                    <i class="fas fa-exclamation-circle"></i>&nbsp;
                    Search by flanking region <a href="<c:url value="/region/${flankingRegion}" />">${flankingRegion}</a>
                </div>
            </div>
        </div>
    </c:if>

    <c:if test="${not empty variantList}" >
        <div class="card">
            <div class="card-body">
                <h4 class="card-title">Variant</h4>

                <c:set var = "variant_table" value = "variant_table"/>
                <c:if test="${queryType == 'Variant'}" >
                    <c:set var = "variant_table" value = ""/>
                </c:if>

                <div class='table-responsive'>
                    <table id="${variant_table}" class="table table-sm table-hover text-center">
                        <thead>
                            <tr>
                                <th class="align-middle" data-toggle="tooltip" title="chr-pos-ref-alt">Variant ID</th>
                                <th class="align-middle" data-toggle="tooltip" title="HGVS_p or HGVS_c for most damaging effect (Ensemble 87)">Consequence</th>
                                <th class="align-middle" data-toggle="tooltip" title="Consequence type of this variation for most damaging effect (Ensemble 87)">Effect</th>
                                <th class="align-middle" data-toggle="tooltip" title="HGNC gene for most damaging effect (Ensemble 87)">Gene</th>
                                <th class="align-middle" data-toggle="tooltip" title="Allele Acount">AC</th>
                                <th class="align-middle" data-toggle="tooltip" title="Allele Number (total number of alleles)">AN</th>
                                <th class="align-middle" data-toggle="tooltip" title="Allele Frequency">AF</th>
                                <th class="align-middle" data-toggle="tooltip" title="Number of samples having data">NS</th>
                                <th class="align-middle" data-toggle="tooltip" title="Number of homozygotes">NHOM</th>
                                <th class="align-middle" data-toggle="tooltip" title="Maximum External Allele Frequency">maxEAF</th>
                            </tr>
                        </thead>

                        <tbody>
                        <c:forEach items="${variantList}" var="variant">
                            <tr>
                                <td class="align-middle">
                                    <a href="<c:url value="/variant/${variant.getVariantIdStr()}" />" target='_blank'>
                                       ${variant.getVariantIdStr()}
                                </a>
                            </td>
                            <td class="align-middle">${variant.getConsequence()}</td>
                            <td class="align-middle">${variant.getEffect()}</td>
                            <td class="align-middle">${variant.getGeneName()}</td>
                            <td class="align-middle">${variant.getAC()}</td>
                            <td class="align-middle">${variant.getAN()}</td>
                            <td class="align-middle">${variant.getAF()}</td>
                            <td class="align-middle">${variant.getNS()}</td>
                            <td class="align-middle">${variant.getNH()}</td>
                            <td class="align-middle">${variant.getMaxEAF()}</td>
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
                <h4 class="card-title">Annotation</h4>
                <div class='table-responsive'>
                    <table class="table table-sm table-hover text-center">
                        <thead>
                            <tr>
                                <th class="align-middle" data-toggle="tooltip" title="Consequence type of this variation (Ensemble 87)">Effect</th>
                                <th class="align-middle" data-toggle="tooltip" title="HGNC gene identifier (Ensemble 87)">Gene</th>
                                <th class="align-middle" data-toggle="tooltip" title="Transcript stable id (Ensemble 87)">Transcript</th>
                                <th class="align-middle" data-toggle="tooltip" title="HGVS coding sequence name (Ensemble 87)">HGVS_c</th>
                                <th class="align-middle" data-toggle="tooltip" title="HGVS protein sequence name (Ensemble 87)">HGVS_p</th>
                                <th class="align-middle" data-toggle="tooltip" title="PolyPhen-2 HumDiv Classification for missense variants from Ensembl 87">PolyPhen</th>
                            </tr>
                        </thead>

                        <tbody>    
                        <c:forEach items="${variant.getAllAnnotation()}" var="annotation">
                            <tr>
                                <td class="align-middle">${annotation.getEffect()}</td>
                                <td class="align-middle">${annotation.getGeneName()}</td>
                                <td class="align-middle">${annotation.getStableId()}</td>
                                <td class="align-middle">${annotation.getHGVS_c()}</td>
                                <td class="align-middle">${annotation.getHGVS_p()}</td>
                                <td class="align-middle">${annotation.getPolyphen()}</td>
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
                <h4 class="card-title">External AF</h4>
                <div class='table-responsive'>
                    <table class="table table-sm table-hover text-center">
                        <thead>
                            <tr>
                                <th class="align-middle" data-toggle="tooltip" title="Version 0.3">ExAC</th>
                                <th class="align-middle" data-toggle="tooltip" title="Version 2020-02-19">Genome Asia</th>
                                <th class="align-middle" data-toggle="tooltip" title="Version 2.1">gnomAD Exome</th>
                                <th class="align-middle" data-toggle="tooltip" title="Version 2.1">gnomAD Genome</th>
                                <th class="align-middle" data-toggle="tooltip" title="Version 2016-09-18">GME Variome</th>
                                <th class="align-middle" data-toggle="tooltip" title="Version 2020-02-24">Iranome</th>
                                <th class="align-middle" data-toggle="tooltip" title="Version Freeze3a hg19">TOPMED</th>
                            </tr>
                        </thead>

                        <tbody>    
                            <tr>
                                <td class="align-middle">${variant.getExAC()}</td>
                                <td class="align-middle">${variant.getGenomeAsia()}</td>
                                <td class="align-middle">${variant.getGnomADExome()}</td>
                                <td class="align-middle">${variant.getGnomADGenome()}</td>
                                <td class="align-middle">${variant.getGME()}</td>
                                <td class="align-middle">${variant.getIranme()}</td>
                                <td class="align-middle">${variant.getTopMed()}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <br/>
        <br/>

        <div class="card">
            <div class="card-body">
                <h4 class="card-title">Carrier</h4>

                <div class="row mt-2">
                    <div class="col-md-auto lead"><span class="badge badge-light">Gender count:</span></div>
                    <c:forEach items="${genders}" var="gender">
                        <div class="col-md-auto lead">
                            <span class="badge badge-light">${variant.getGenderCount()[gender.getIndex()]}
                                ${gender.getName()}</span> 
                        </div>
                    </c:forEach>
                </div>

                <div class="row mt-2">
                    <div class="col-md-auto lead"><span class="badge badge-light">Ancestry count:</span></div>
                    <c:forEach items="${ancestries}" var="ancestry">
                        <div class="col-md-auto lead">
                            <span class="badge badge-light">${variant.getAncestryCount()[ancestry.getIndex()]}
                                ${ancestry.getName()}</span> 
                        </div>
                    </c:forEach>
                </div>

                <br/>

                <c:choose>
                    <c:when test="${not empty variant.getCarriers()}" >
                        <div class='table-responsive'>
                            <table id="carrier_table" class="table table-sm table-hover text-center">
                                <thead>
                                    <tr>
                                <c:if test="${not empty sequence_authorized}" >
                                    <th class="align-middle" data-toggle="tooltip" title="Sample experiment_id in sequenceDB">Experiment ID</th>
                                </c:if>
                                <th class="align-middle" data-toggle="tooltip" title="AvaiContUsed in sequenceDB">Public Available</th>
                                <th class="align-middle" data-toggle="tooltip" title="seqGender in sequenceDB">Gender</th>
                                <th class="align-middle" data-toggle="tooltip" title="Broad Phenotype in sequenceDB">Phenotype</th>
                                <th class="align-middle" data-toggle="tooltip" title="Ancestry probability >= 0.5 in sequenceDB">Ancestry</th>
                                <th class="align-middle" data-toggle="tooltip" title="Genotype">GT</th>
                                <th class="align-middle" data-toggle="tooltip" title="Read Depth">DP</th>
                                <th class="align-middle" data-toggle="tooltip" title="Percentage of all the reads at the site that support the alternative allele">Percent Alt Read</th>
                                <th class="align-middle" data-toggle="tooltip" title="Genotype Quality">GQ</th>
                                <th class="align-middle" data-toggle="tooltip" title="PASS->PASS, VQSRTrancheSNP90.00to99.00->LIKELY, VQSRTrancheSNP99.00to99.90->INTERMEDIATE, VQSRTrancheSNP99.90to100.00->FAIL">FILTER</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${variant.getCarriers()}" var="carrier">                                
                                    <tr>
                                    <c:if test="${not empty sequence_authorized}" >
                                        <td class="align-middle">
                                            <a href="https://sequence.igm.cumc.columbia.edu/search.php?action=viewSample&experiment_id=${carrier.getExperimentId()}" target="_blank">
                                                ${carrier.getExperimentId()}
                                            </a>
                                        </td>
                                    </c:if>
                                    <td class="align-middle">${carrier.getAvailableControlUse()}</td>
                                    <td class="align-middle">${carrier.getGender()}</td>
                                    <td class="align-middle">${carrier.getPhenotype()}</td>
                                    <td class="align-middle">${carrier.getAncestry()}</td>
                                    <td class="align-middle">${carrier.getGTStr()}</td>
                                    <td class="align-middle">${carrier.getDP()}</td>
                                    <td class="align-middle">${carrier.getPercAltRead()}</td>
                                    <td class="align-middle">${carrier.getGQ()}</td>
                                    <td class="align-middle">${carrier.getFILTER()}</td>
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
        ref: '${variant.getRef()}',
        alt: '${variant.getAlt()}',
        chr: '${variant.getChrStr()}',
        pos: ${variant.getStartPosition()},
    };
        </script>
    </c:forEach>
</c:if>