import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DataTableDirective } from 'angular-datatables';
import { Subject } from 'rxjs';
import { SearchService } from '../../services/search.service';

@Component({
  selector: 'app-region',
  templateUrl: './region.component.html',
  styleUrls: ['./region.component.css']
})
export class RegionComponent implements AfterViewInit, OnDestroy, OnInit {
  error: string;

  // chr:start-end
  query: string;

  // search result
  variants: Array<any>;

  // region datatable
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
    this.route.paramMap.subscribe(params => {
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
          className: 'btn btn-light'
        }
      ]
    };
  }

  ngAfterViewInit(): void {
    this.dtTrigger.next();
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  onSearch() {
    this.searchService.search(
      'region',
      this.query,
      this.route.snapshot.queryParams['phenotype'],
      this.route.snapshot.queryParams['maf'],
      this.route.snapshot.queryParams['isHighQualityVariant'],
      this.route.snapshot.queryParams['isUltraRareVariant'],
      this.route.snapshot.queryParams['isPublicAvailable']).subscribe(
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
