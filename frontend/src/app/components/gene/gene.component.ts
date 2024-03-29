import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DataTableDirective } from 'angular-datatables';
import { Subject } from 'rxjs';
import { SearchService } from '../../services/search.service';
import { Tooltip } from '../../../assets/js/bootstrap.bundle.min.js';

@Component({
  selector: 'app-gene',
  templateUrl: './gene.component.html',
  styleUrls: ['./gene.component.css']
})
export class GeneComponent implements AfterViewInit, OnDestroy, OnInit {
  error: string;

  // hgnc gene name
  query: string;
  experimentId: string;

  // search result
  variants: Array<any>;

  // gene datatable
  @ViewChild(DataTableDirective, { static: false })
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false;
  dtOptions: any = {};
  dtTrigger: Subject<any> = new Subject();

  constructor(
    private route: ActivatedRoute,
    private searchService: SearchService) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(
      params => {
        this.query = params.get('query');
        if (this.query) {
          this.onSearch();
        }
      });

    this.dtOptions = {
      pagingType: 'simple_numbers',
      pageLength: 10,
      processing: true,
      dom: 'Blfrtip',
      buttons: [
        {
          extend: 'csv',
          text: '<i class="fas fa-file-csv"></i> Download',
          className: 'btn btn-light',
          filename: "gene"
        }
      ]
    };
  }

  ngAfterViewInit(): void {
    Array.from(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    .forEach(tooltipNode => new Tooltip(tooltipNode));

    this.dtTrigger.next();
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  onSearch() {
    this.experimentId = this.route.snapshot.queryParams['experimentId'];

    this.searchService.search(
      'gene',
      this.query,
      this.route.snapshot.queryParams['phenotype'],
      this.route.snapshot.queryParams['maf'],
      this.route.snapshot.queryParams['isHighQualityVariant'],
      this.route.snapshot.queryParams['isUltraRareVariant'],
      this.route.snapshot.queryParams['isPubliclyAvailable'],
      this.experimentId).subscribe(
        data => {
          this.variants = data;

          this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
            dtInstance.destroy();
            this.dtTrigger.next();
          });
        },
        error => {
          if (error.error) {
            this.error = error.error.message;
          } else {
            this.error = "Unexpected error";
          }
        }
      );
  }
}