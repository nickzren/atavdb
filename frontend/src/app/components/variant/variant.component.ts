import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DataTableDirective } from 'angular-datatables';
import { Subject } from 'rxjs';
import { AccountService } from '../../services/account.service';
import { SearchService } from '../../services/search.service';

@Component({
  selector: 'app-variant',
  templateUrl: './variant.component.html',
  styleUrls: ['./variant.component.css']
})
export class VariantComponent implements AfterViewInit, OnDestroy, OnInit {
  error: string;
  flankingRegion: string;

  // variant id (chr-pos-ref-alt)
  query: string;

  // search result
  variant: any;

  // predefined list
  GENDER_LIST = ["Male", "Female", "Ambiguous", "NA"];
  ANCESTRY_LIST = ["African", "Caucasian", "EastAsian", "Hispanic", "MiddleEastern", "SouthAsian", "NA"];

  // carrier datatable
  @ViewChild(DataTableDirective, { static: false })
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false;
  dtOptions: any = {};
  dtTrigger: Subject<any> = new Subject();

  constructor(
    private route: ActivatedRoute,
    public accountService: AccountService,
    private searchService: SearchService) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.query = params.get('variant');
      if (this.query) {
        this.onSearch(this.query);
      }
    });

    this.dtOptions = {
      pagingType: 'simple_numbers',
      pageLength: 10,
      processing: true,
      dom: 'lfrtip',
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
        this.variant = Object.values(data)[0];

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

        // no results found --> show search flanking region
        var tmp = this.query.split('-');
        if (tmp.length == 4) {
          const regex = '^[atcgxymtATCGXYMT0-9-]+$';
          if (this.query.match(regex)) {
            var start = Number(tmp[1]) - 10;
            var end = Number(tmp[1]) + 10;
            this.flankingRegion = tmp[0] + ":" + start + "-" + end;
          }
        }
      }
    );
  }
}
