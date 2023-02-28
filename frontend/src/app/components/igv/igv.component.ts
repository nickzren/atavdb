import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import * as igv from 'igv'

@Component({
  selector: 'app-igv',
  templateUrl: './igv.component.html',
  styleUrls: ['./igv.component.css']
})
export class IgvComponent implements AfterViewInit, OnDestroy {
  @ViewChild('igvdiv') igvDiv: ElementRef;
  browser: any;

  options = {
    showCommandBar: true,
    showKaryo: false,
    showCenterGuide: true,
    reference:
    {
      id: "hg19",
      fastaURL: "https://s3.amazonaws.com/igv.broadinstitute.org/genomes/seq/1kg_v37/human_g1k_v37_decoy.fasta",
      cytobandURL: "https://s3.amazonaws.com/igv.broadinstitute.org/genomes/seq/b37/b37_cytoband.txt"
    },
    locus: "8:128,750,948-128,751,025",
    tracks:
      [
        {
          type: 'alignment',
          format: 'cram',
          url: 'https://s3.amazonaws.com/1000genomes/phase3/data/HG00096/exome_alignment/HG00096.mapped.ILLUMINA.bwa.GBR.exome.20120522.bam.cram',
          indexURL: 'https://s3.amazonaws.com/1000genomes/phase3/data/HG00096/exome_alignment/HG00096.mapped.ILLUMINA.bwa.GBR.exome.20120522.bam.cram.crai',
          name: 'HG00096',
          sort: {
            chr: "chr8",
            position: 128750986,
            option: "BASE",
            direction: "ASC"
          },
          height: 500
        }
      ]
  };
  constructor() { }
  ngAfterViewInit() {
    this.createBrowser()
  }
  async createBrowser() {
    try {
      this.browser = await igv.createBrowser(this.igvDiv.nativeElement, this.options)
    } catch (e) {
      console.log(e)
    }
  }
  ngOnDestroy() {
    igv.removeAllBrowsers()
  }
}
