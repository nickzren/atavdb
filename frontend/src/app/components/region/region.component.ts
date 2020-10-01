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
      this.query = params.get('region');
      if (this.query) {
        this.onSearch(this.query);
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

  onSearch(query: string) {
    this.searchService.search(query).subscribe(
      data => {
        this.variants = data;

        this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
          dtInstance.destroy();
          this.dtTrigger.next();
        });
      }
    );
  }
}
