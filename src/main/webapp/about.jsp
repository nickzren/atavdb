<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>About</title>

        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <link rel="shortcut icon" type="image/x-icon" href="img/favicon.ico">

        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" href="css/all-fontawesome.css">
        <link rel="stylesheet" href="css/main.css">
    </head>

    <body>
        <div class="container">

            <%@include file="base/header.jsp" %>

            <div class="container-main">
                <h3 class="page-header">About</h3>

                <p>
                    The ATAV data browser is a web user interface that allows everyone within the network to access variant level data directly from the full data set in the ATAV database. 
                    It supports the search of variants by gene, region and variant ID. The gene or region view displays a list of variants with allele count, 
                    allele frequency, number of samples, effect, gene etc. The variant view displays a set of annotations (effect, gene, 
                    transcript, polyphen) and details about variant carriers (gender, phenotype and quality metrics). It includes links to other 
                    public data resources such as Ensembl, gnomAD, ClinVar etc. and directly integrates additional annotations via APIs 
                    (e.g. Genoox Franklin API for clinical variant interpretation). The data browser has advanced filters such as a 
                    maximum allele frequency threshold to only search rare or ultra-rare variants, restriction to high quality variants 
                    or restriction to a certain phenotype. In contrast to many other platforms, the data browser is able to show newly 
                    added sample data in real time and is therefore evolving rapidly as more and more samples are sequenced.
                </p>
                
                <br/>
                
                <h4><i class="fa fa-database"></i> Data Generation</h4>

                <p>Sequencing of DNA was performed by <a href="http://igm.columbia.edu" target="_blank">Institute for Genomic Medicine</a>.
                    Samples were either exome sequenced or whole-genome sequenced 
                    using Illumina NovaSeq 6000 sequencers according to standard protocols.
                </p>

                <p>
                    The Illumina lane-level fastq files were aligned to the Human Reference Genome (NCBI Build 37)
                    using the Illumina DRAGEN Alignment tool. Picard software was used to remove duplicate 
                    reads and process these lane-level SAM files, resulting in a sample-level BAM file that was 
                    used for variant calling. GATK was used to recalibrate base quality scores, realign around 
                    indels, and call variants. All variants were annotated to Ensembl 87 using CLINEFF.
                </p>                
            </div>
        </div>

        <%@include file="base/footer.jsp" %>
        <%@include file="base/counter.jsp" %>
    </body>
</html>